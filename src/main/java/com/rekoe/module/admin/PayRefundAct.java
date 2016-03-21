package com.rekoe.module.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.rekoe.annotation.PermissionTag;
import com.rekoe.common.Message;
import com.rekoe.common.page.Pagination;
import com.rekoe.domain.PayRefund;
import com.rekoe.domain.PlatformUser;
import com.rekoe.module.facebook.pay.Order;
import com.rekoe.service.PayFefundService;
import com.rekoe.service.PlatformUserService;
import com.restfb.Parameter;
import com.restfb.batch.BatchRequest;
import com.restfb.batch.BatchRequest.BatchRequestBuilder;
import com.restfb.batch.BatchResponse;

@IocBean
@At("/admin/pay_refund")
public class PayRefundAct {

	private final static Log log = Logs.get();
	/**
	 * /PAYMENT_ID/refunds
	 */
	@Inject
	private PayFefundService payFefundService;

	@At
	@Ok("fm:template.admin.pay_refund.list")
	@RequiresPermissions("admin.pay:refund")
	@PermissionTag(name = "退单管理", tag = "退单管理", enable = true)
	public Pagination list(@Param(value = "pageNumber", df = "1") Integer pageNumber) {
		return payFefundService.getPayRefundListByPager(pageNumber);
	}

	@At
	@Ok("fm:template.admin.pay_refund.view")
	@RequiresPermissions("admin.pay:refund")
	@PermissionTag(name = "退单管理", tag = "退单管理", enable = false)
	public Order view(@Param("id") String id, @Param("appid") String appid, HttpServletRequest req) throws Exception {
		return edit(id, appid, req);
	}

	@At
	@Ok("fm:template.admin.pay_refund.edit")
	@RequiresPermissions("admin.pay:refund")
	@PermissionTag(name = "退单管理", tag = "退单管理", enable = false)
	public Order edit(@Param("id") String id, @Param("appid") String appid, HttpServletRequest req) throws Exception {
		req.setAttribute("appid", appid);
		return Json.fromJson(Order.class, payFefundService.facebookFetch(id));
	}

	@At
	@Ok("json")
	@RequiresPermissions("admin.pay:refund")
	@PermissionTag(name = "退单管理", tag = "退单管理", enable = false)
	public Message refund(@Param("appid") String appid, @Param("id") String id, @Param("currency") String currency, @Param("amount") String amount, @Param("reason") String reason, HttpServletRequest req) throws Exception {
		BatchRequest postRequest = new BatchRequestBuilder(id + "/refunds").method("POST").body(Parameter.with("reason", reason), Parameter.with("currency", currency), Parameter.with("amount", amount)).build();
		List<BatchResponse> list = payFefundService.getFacebookClient().executeBatch(postRequest);
		BatchResponse res = list.get(0);
		String result = res.getBody();
		disputeResult disputeResult = Json.fromJson(disputeResult.class, result);
		if (!Lang.isEmpty(disputeResult) && disputeResult.isSuccess()) {
			PayRefund payRefund = payFefundService.fetchByKeyId(appid);
			payRefund.setStatus(true);
			payFefundService.update(payRefund);
			return Message.success("admin.message.success", req);
		}
		com.rekoe.module.facebook.pay.Error pay = Json.fromJson(com.rekoe.module.facebook.pay.Error.class, result);
		return Message.error(pay.getError().getMessage(), req);
	}

	@At
	@Ok("json")
	@RequiresPermissions("admin.pay:refund")
	@PermissionTag(name = "退单管理", tag = "退单管理", enable = false)
	public Message dispute(@Param("appid") String appid, @Param("id") String id, @Param("reason") String reason, HttpServletRequest req) throws Exception {
		BatchRequest postRequest = new BatchRequestBuilder(id + "/dispute").method("POST").body(Parameter.with("reason", reason)).build();
		List<BatchResponse> list = payFefundService.getFacebookClient().executeBatch(postRequest);
		BatchResponse res = list.get(0);
		String result = res.getBody();
		log.infof("dispute res[%s]", result);
		disputeResult disputeResult = Json.fromJson(disputeResult.class, result);
		if (!Lang.isEmpty(disputeResult) && disputeResult.isSuccess()) {
			PayRefund payRefund = payFefundService.fetchByKeyId(appid);
			payRefund.setStatus(true);
			payFefundService.update(payRefund);
			return Message.success("admin.message.success", req);
		}
		com.rekoe.module.facebook.pay.Error pay = Json.fromJson(com.rekoe.module.facebook.pay.Error.class, result);
		return Message.error(pay.getError().getMessage(), req);
	}

	public static void main(String[] args) {
		String json = "[{\"success\":true}]";
		List<disputeResult> rest = Json.fromJsonAsList(disputeResult.class, json);
		System.out.println(rest.get(0).isSuccess());
	}

	public static class disputeResult {
		private boolean success;

		public boolean isSuccess() {
			return success;
		}

		public void setSuccess(boolean success) {
			this.success = success;
		}
	}

	@Inject
	private PlatformUserService platformUserService;
	@Inject
	private PropertiesProxy conf;

	@At
	@Ok("json")
	@RequiresPermissions("admin.pay:refund")
	@PermissionTag(name = "退单管理", tag = "退单管理", enable = false)
	public Message update(@Param("id") String id, HttpServletRequest req) {
		PayRefund payRefund = payFefundService.fetchByID(id);
		if (Lang.isEmpty(payRefund)) {
			return Message.error("admin.common.error.notfund", req);
		}
		String passportid = payRefund.getPassportid();
		PlatformUser platformUser = platformUserService.getPlatformUser(conf.getInt("facebook.pid"), passportid, conf.get("facebook.pfid"));
		if (Lang.isEmpty(platformUser)) {
			return Message.error("admin.common.error.notfund", req);
		}
		platformUser.setLocked(true);
		platformUserService.update(Chain.make("is_locked", true), Cnd.where("id", "=", platformUser.getId()));
		return Message.success("admin.message.success", req);
	}
}
