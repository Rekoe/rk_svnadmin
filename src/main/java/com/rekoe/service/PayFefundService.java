package com.rekoe.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.brickred.socialauth.SocialAuthConfig;
import org.brickred.socialauth.util.OAuthConfig;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.pager.Pager;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.lang.Files;
import org.nutz.lang.Lang;
import org.nutz.lang.Times;
import org.nutz.lang.stream.NullInputStream;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import com.rekoe.common.page.Pagination;
import com.rekoe.domain.PayRefund;
import com.rekoe.module.facebook.FaceBookLoginAct;
import com.rekoe.module.facebook.pay.Entry;
import com.rekoe.module.facebook.pay.FacebookRequestResult;
import com.rekoe.module.facebook.pay.Order;
import com.rekoe.module.facebook.pay.PayObject;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.batch.BatchRequest;
import com.restfb.batch.BatchRequest.BatchRequestBuilder;
import com.restfb.batch.BatchResponse;

import freemarker.template.Configuration;
import freemarker.template.Template;

@IocBean(fields = { "dao" }, create = "init", depose = "depose")
public class PayFefundService extends BaseService<PayRefund> {

	private static final Log log = Logs.get();
	protected ExecutorService es;
	protected FacebookClient facebookClient;

	private SocialAuthConfig config;

	public void init() throws Exception {

		SocialAuthConfig config = new SocialAuthConfig();
		File devConfig = Files.findFile("oauth_consumer.properties_dev"); // 开发期所使用的配置文件
		if (devConfig == null)
			devConfig = Files.findFile("oauth_consumer.properties"); // 真实环境所使用的配置文件
		if (devConfig == null)
			config.load(new NullInputStream());
		else
			config.load(new FileInputStream(devConfig));
		this.config = config;
		try {
			OAuthConfig oAuthConfig = config.getProviderConfig(FaceBookLoginAct.OAUTH_ID);
			facebookClient = new DefaultFacebookClient(oAuthConfig.get_consumerKey() + "|" + oAuthConfig.get_consumerSecret());
		} catch (Exception e) {
			log.error(e);
		}
		es = Executors.newFixedThreadPool(16);
	}

	public SocialAuthConfig getConfig() {
		return config;
	}

	public void depose() {
		es.shutdown();
		try {
			es.awaitTermination(30, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			log.info("PayFefundService shutdown", e);
		}
		es = null;
	}

	public PayFefundService() {
		super();
	}

	public PayFefundService(Dao dao) {
		super(dao);
	}

	public boolean insert(PayRefund payRefund) {
		dao().insert(payRefund);
		return true;
	}

	public void update(PayRefund payRefund) {
		dao().update(payRefund);
	}

	public String facebookFetch(String id) {
		return facebookClient.fetchObject(id, String.class);
	}

	public PayRefund fetchByID(String paymentid) {
		PayRefund payRefund = fetch(Cnd.where("paymentid", "=", paymentid));
		return payRefund;
	}

	public PayRefund fetchByKeyId(String appid) {
		PayRefund payRefund = fetch(Cnd.where("id", "=", appid));
		return payRefund;
	}

	public FacebookClient getFacebookClient() {
		return facebookClient;
	}

	public void push(final PayObject pay) {
		ExecutorService es = this.es;
		if (es == null || es.isShutdown()) {
			log.info("PayFefundService.queue is shutdown, ignore push");
			return;
		}
		if (!Lang.isEmpty(pay)) {
			es.execute(new Runnable() {
				@Override
				public void run() {
					List<Entry> entryList = pay.getEntryList();
					for (Entry entry : entryList) {
						String id = entry.getId();
						PayRefund payRefund = new PayRefund();
						payRefund.setCreateTime(Times.now());
						payRefund.setPaymentid(id);
						payRefund.setChangedFields(Json.toJson(entry.getChangedFields(), JsonFormat.compact()));
						boolean isRight = entry.getChangedFields().contains("disputes");
						String http = facebookFetch(id);
						Order order = Json.fromJson(Order.class, http);
						if (!Lang.isEmpty(order) && isRight) {
							payRefund.setName(order.getUser().getName());
							payRefund.setPassportid(order.getUser().getId());
							payRefund.setRequestId(order.getRequestID());
							payRefund.setStatus(!isRight);
							insert(payRefund);
						}
					}

				}
			});
		}
	}

	@Inject
	private Configuration configuration;

	public void sendOrderEmail(final Order order) {
		es.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Map<String, Object> root = new HashMap<String, Object>();
					root.put("obj", order);
					root.put("base", "/");
					String str = processTemplateIntoString(configuration, "/template/admin/pay_refund/view.ftl", root);
					HtmlEmail email = new HtmlEmail();
					email.setHostName("smtp.163.com");// 邮件服务器
					email.setSmtpPort(25);
					email.setAuthentication("koukou890", "rekoenet");// smtp认证的用户名和密码
					List<InternetAddress> inter = new ArrayList<InternetAddress>();
					inter.add(new InternetAddress("koukou890@qq.com"));
					email.setTo(inter);
					email.setFrom("koukou890@163.com", "Hero-Defense");// 发信者
					email.setSubject("Order Disputes[Hero-Defense]");// 标题
					email.setCharset("UTF-8");// 编码格式
					// email.setMsg("这是一封用户发起Disputs的提醒邮件");// 内容
					email.setHtmlMsg(str);
					email.send();// 发送
				} catch (AddressException | EmailException e) {
					log.error(e.getMessage(), e);
				}
			}
		});
	}

	public String processTemplateIntoString(Configuration cfg, String path, Map<String, Object> root) {
		String result = null;
		try {
			Template template = cfg.getTemplate(path);
			cfg.setDefaultEncoding("UTF-8");
			java.io.StringWriter writer = new java.io.StringWriter();
			template.process(root, writer);
			result = writer.toString();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public Pagination getPayRefundListByPager(Integer pageNumber) {
		pageNumber = getPageNumber(pageNumber);
		Pager pager = dao().createPager(pageNumber, 20);
		List<PayRefund> list = dao().query(getEntityClass(), Cnd.orderBy().desc("createTime"), pager);
		pager.setRecordCount(dao().count(getEntityClass(), null));
		return new Pagination(pageNumber, 20, pager.getRecordCount(), list);
	}

	public static void main(String[] args) {
		FacebookClient facebookClient = new DefaultFacebookClient("1440728696165353|3a4e87ac4f8ea6eb4d0bfcc266cd618c");
		BatchRequest postRequest = new BatchRequestBuilder("617649511680969" + "/dispute").method("POST").body(Parameter.with("reason", "GRANTED_REPLACEMENT_ITEM"), Parameter.with("currency", "USD"), Parameter.with("amount", 1.9)).build();
		List<BatchResponse> list = facebookClient.executeBatch(postRequest);
		System.out.println(list);
		BatchResponse res = list.get(0);
		String result = res.getBody();
		System.out.println(result);
	}

	public void publish(final List<String> passportidList, final String message) {
		es.execute(new Runnable() {
			@Override
			public void run() {
				for (String uid : passportidList)
					publish(uid, message);
			}
		});
	}

	public void publish(String passportid, String message) {
		try {
			FacebookRequestResult publishEventResponse = facebookClient.publish(passportid + "/notifications", FacebookRequestResult.class, Parameter.with("template", message));
			log.infof("Published event ID: %s", publishEventResponse.isSuccess());
		} catch (Exception e) {
			// accountService.update(passportid, Chain.make("install", false),
			// Cnd.where("passportid", "=", passportid));
			log.errorf("passportid[%s] ,msg [%s]", passportid, e.getMessage());
		}
	}
}
