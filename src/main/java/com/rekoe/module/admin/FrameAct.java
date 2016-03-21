package com.rekoe.module.admin;

import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;

@IocBean
public class FrameAct {

	/** 用户管理 **/
	@At("/admin/frame/user/main")
	@Ok("fm:template.admin.user.frame.main")
	public void userMain() {
	}

	@At("/admin/frame/user/left")
	@Ok("fm:template.admin.user.frame.left")
	public void userLeft() {
	}

	@At("/admin/frame/user/right")
	@Ok("fm:template.admin.user.frame.right")
	public void userRight() {
	}

	// Server
	@At("/admin/frame/server/main")
	@Ok("fm:template.admin.server.frame.main")
	public void serverMain() {
	}

	@At("/admin/frame/server/left")
	@Ok("fm:template.admin.server.frame.left")
	public void serverLeft() {
	}

	@At("/admin/frame/server/right")
	@Ok("fm:template.admin.server.frame.right")
	public void serverRight() {
	}

	/*
	 * 修改密码
	 */
	// 系统配置
	@At("/admin/frame/account/main")
	@Ok("fm:template.admin.account.frame.main")
	public void accountPwdMain() {
	}

	@At("/admin/frame/account/left")
	@Ok("fm:template.admin.account.frame.left")
	public void accountPwdLeft() {
	}

	@At("/admin/frame/account/right")
	@Ok("fm:template.admin.account.frame.right")
	public void gamePwdRight() {
	}

	/**
	 * 用户操作日志
	 */

	@At("/admin/frame/system/log/main")
	@Ok("fm:template.admin.system.log.frame.main")
	public void system_log_main() {
	}

	@At("/admin/frame/system/log/left")
	@Ok("fm:template.admin.system.log.frame.left")
	public void system_log_left() {
	}

	@At("/admin/frame/system/log/right")
	@Ok("fm:template.admin.system.log.frame.right")
	public void system_log_right() {
	}

	/**
	 * 账号处理
	 */

	@At("/admin/frame/pay_refund/main")
	@Ok("fm:template.admin.pay_refund.frame.main")
	public void pay_refund_main() {
	}

	@At("/admin/frame/pay_refund/left")
	@Ok("fm:template.admin.pay_refund.frame.left")
	public void pay_refund_left() {
	}

	@At("/admin/frame/pay_refund/right")
	@Ok("fm:template.admin.pay_refund.frame.right")
	public void pay_refund_right() {
	}

	/**
	 * 退单处理
	 */

	@At("/admin/app/frame/account/main")
	@Ok("fm:template.admin.app.account.frame.main")
	public void app_account_main() {
	}

	@At("/admin/app/frame/account/left")
	@Ok("fm:template.admin.app.account.frame.left")
	public void app_account_left() {
	}

	@At("/admin/app/frame/account/right")
	@Ok("fm:template.admin.app.account.frame.right")
	public void app_account_right() {
	}

	/**
	 * CDKEY
	 */
	@At("/admin/frame/cdkey/main")
	@Ok("fm:template.admin.cdkey.frame.main")
	public void cdkey_main() {
	}

	@At("/admin/frame/cdkey/left")
	@Ok("fm:template.admin.cdkey.frame.left")
	public void cdkey_left() {
	}

	@At("/admin/frame/cdkey/right")
	@Ok("fm:template.admin.cdkey.frame.right")
	public void cdkey_right() {
	}

	/***
	 * oauth_client
	 */
	@At("/admin/frame/oauth_client/main")
	@Ok("fm:template.admin.oauth_client.frame.main")
	public void oauth_client_main() {
	}

	@At("/admin/frame/oauth_client/left")
	@Ok("fm:template.admin.oauth_client.frame.left")
	public void oauth_client_left() {
	}

	@At("/admin/frame/oauth_client/right")
	@Ok("fm:template.admin.oauth_client.frame.right")
	public void oauth_client_right() {
	}

	/***
	 * oauth_user
	 */
	@At("/admin/frame/oauth_user/main")
	@Ok("fm:template.admin.oauth_user.frame.main")
	public void oauth_user_main() {
	}

	@At("/admin/frame/oauth_user/left")
	@Ok("fm:template.admin.oauth_user.frame.left")
	public void oauth_user_left() {
	}

	@At("/admin/frame/oauth_user/right")
	@Ok("fm:template.admin.oauth_user.frame.right")
	public void oauth_user_right() {
	}

	/**
	 * 公告
	 */
	@At("/admin/frame/notice/main")
	@Ok("fm:template.admin.notice.frame.main")
	public void notice_main() {
	}

	@At("/admin/frame/notice/left")
	@Ok("fm:template.admin.notice.frame.left")
	public void notice_left() {
	}

	@At("/admin/frame/notice/right")
	@Ok("fm:template.admin.notice.frame.right")
	public void notice_right() {
	}

	/**
	 * 数据汇报
	 */
	@At("/admin/frame/report/main")
	@Ok("fm:template.admin.report.frame.main")
	public void report_main() {
	}

	@At("/admin/frame/report/left")
	@Ok("fm:template.admin.report.frame.left")
	public void report_left() {
	}

	@At("/admin/frame/report/right")
	@Ok("fm:template.admin.report.frame.right")
	public void report_right() {
	}
}
