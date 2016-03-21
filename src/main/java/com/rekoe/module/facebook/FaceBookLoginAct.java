package com.rekoe.module.facebook;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.brickred.socialauth.util.OAuthConfig;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.lang.Times;
import org.nutz.mvc.View;
import org.nutz.mvc.adaptor.JsonAdaptor;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.view.ViewWrapper;
import org.nutz.mvc.view.VoidView;
import org.nutz.plugins.view.freemarker.FreeMarkerConfigurer;
import org.nutz.plugins.view.freemarker.FreemarkerView;
import org.nutz.web.ajax.Ajax;
import org.nutz.web.ajax.AjaxReturn;

import com.rekoe.domain.OfficialServer;
import com.rekoe.domain.PlatformUser;
import com.rekoe.domain.ServerHistory;
import com.rekoe.module.facebook.pay.PayObject;
import com.rekoe.service.IdWorkerService;
import com.rekoe.service.OfficialServerService;
import com.rekoe.service.PayFefundService;
import com.rekoe.service.PlatformUserService;
import com.rekoe.service.ServerHistoryService;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.User;

@IocBean
@At("/facebook")
public class FaceBookLoginAct {

	public static final String OAUTH_ID = "facebook";
	private final String FB_URL = "https://apps.facebook.com/";
	private final String FB_TYPE_USER = "me";
	@Inject("java:conf.get('facebook.pid')")
	private static int PLARFORM_ID = 1002;
	@Inject("java:conf.get('facebook.pfid')")
	private static String PFID;
	@Inject("java:conf.get('facebook.namespace')")
	private static String NAMESPACE;
	@Inject("java:conf.get('facebook.feed.upstream.url')")
	private static String FEED_UPSTREAM_URL;
	@Inject
	private PlatformUserService platformUserService;

	@Inject
	private IdWorkerService idFactory;

	@Inject
	private FreeMarkerConfigurer freeMarkerConfigurer;

	@Inject
	private ServerHistoryService serverHistoryService;

	@Inject
	private OfficialServerService officialServerService;

	@Inject
	private PropertiesProxy conf;
	@Inject
	private PayFefundService payFefundService;

	@At
	public View login(@Param("signed_request") String code, HttpServletRequest req, HttpServletResponse res, HttpSession session) throws Exception {
		OAuthConfig oAuthConfig = payFefundService.getConfig().getProviderConfig(OAUTH_ID);
		String appId = oAuthConfig.get_consumerKey();
		boolean isRight = true;
		if (StringUtils.isBlank(code)) {
			isRight = false;
		}
		if (isRight) {
			FacebookSignedRequest facebookSR = FacebookSignedRequest.getFacebookSignedRequest(code, FacebookSignedRequest.class);
			String accessToken = facebookSR.getOauth_token();
			if (StringUtils.isBlank(accessToken)) {
				isRight = false;
			} else {
				OfficialServer server = officialServerService.getRecommendServer(PLARFORM_ID);
				FacebookClient facebookClient = new DefaultFacebookClient(accessToken);
				User user = facebookClient.fetchObject(FB_TYPE_USER, User.class);
				PlatformUser platformUser = platformUserService.getPlatformUser(PLARFORM_ID, user.getId(), PFID);
				if (Lang.isEmpty(platformUser)) {
					long openid = idFactory.nextId();
					platformUser = platformUserService.add(PLARFORM_ID, user.getId(), openid, PFID);
					ServerHistory entity = new ServerHistory(platformUser.getOpenid(), PLARFORM_ID, server.getSid());
					serverHistoryService.add(entity);
					req.setAttribute("sid", server.getSid());
				} else {
					if (platformUser.isLocked()) {
						return new ViewWrapper(new FreemarkerView(freeMarkerConfigurer, "template/admin/common/lock"), "");
					}
					ServerHistory entity = serverHistoryService.getLastLoginServer(platformUser.getOpenid(), PLARFORM_ID);
					if (!Lang.isEmpty(entity)) {
						req.setAttribute("sid", entity.getSid());
					}
				}
				ServerHistory hisroty = serverHistoryService.getLoginServerList(platformUser.getOpenid(), PLARFORM_ID, 1);
				req.setAttribute("top", server);
				req.setAttribute("userHistory", hisroty);
				req.setAttribute("user", user);
				req.setAttribute("code", code);
				req.setAttribute("ads", req.getQueryString());
				req.setAttribute("zone", server);
			}
		}
		if (isRight) {
			res.addHeader("P3P", "CP=CAO PSA OUR IDC DSP COR ADM DEVi TAIi PSD IVAi IVDi CONi HIS IND CNT");
			List<OfficialServer> zones = officialServerService.getShowList(PLARFORM_ID);
			req.setAttribute("zones", zones);
			req.setAttribute("namespace", NAMESPACE);
			req.setAttribute("appId", appId);
			req.setAttribute("feed_upstream_url", FEED_UPSTREAM_URL);
			return new ViewWrapper(new FreemarkerView(freeMarkerConfigurer, "template/front/zone/in"), zones);
		} else {
			String url = "http://www.facebook.com/dialog/oauth?client_id=" + appId + "&scope=email,publish_actions&redirect_uri=" + FB_URL + NAMESPACE + "/?" + req.getQueryString();
			res.setContentType("text/html");
			PrintWriter writer = res.getWriter();
			writer.print("<script> top.location.href='" + url + "'</script>");
			writer.close();
			return new VoidView();
		}
	}

	@At("/change/?/?")
	@Ok("json")
	public AjaxReturn change(int sid, String passportid) {
		PlatformUser platformUser = platformUserService.getPlatformUser(PLARFORM_ID, passportid, PFID);
		if (Lang.isEmpty(platformUser)) {
			return Ajax.fail();
		}
		if (platformUser.isLocked()) {
			return Ajax.fail();
		}
		OfficialServer officialServer = officialServerService.getOfficialServer(PLARFORM_ID, sid);
		if (Lang.isEmpty(officialServer)) {
			return Ajax.fail();
		}
		ServerHistory entity = serverHistoryService.getServerHistory(platformUser.getOpenid(), PLARFORM_ID, sid);
		if (Lang.isEmpty(entity)) {
			entity = new ServerHistory(platformUser.getOpenid(), PLARFORM_ID, sid);
			serverHistoryService.add(entity);
		} else {
			entity.setModifyTime(Times.now());
			serverHistoryService.update(entity);
		}
		return Ajax.ok().setData(officialServer);
	}

	@At
	@Ok("redirect:${obj}")
	public String upstream() {
		return "https://apps.facebook.com/" + NAMESPACE + "/";
	}

	@At
	@AdaptBy(type = JsonAdaptor.class)
	@Ok("raw")
	public String refund(PayObject pay, @Param("::hub.") ReFund reFund) {
		payFefundService.push(pay);
		if (Lang.isEmpty(reFund)) {
			return "ok";
		}
		return reFund.getChallenge();
	}
}
