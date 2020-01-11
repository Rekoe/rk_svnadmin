package com.rekoe.module.admin;

import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.Mvcs;
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

	/*
	 * 修改密码
	 */
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

	/***
	 * SVN 账号
	 */
	@At("/admin/frame/svn/user/main")
	@Ok("fm:template.admin.svn_user.frame.main")
	public void svn_user_main() {
	}

	@At("/admin/frame/svn/user/left")
	@Ok("fm:template.admin.svn_user.frame.left")
	public void svn_user_left() {
	}

	@At("/admin/frame/svn/user/right")
	@Ok("fm:template.admin.svn_user.frame.right")
	public void svn_user_right() {
	}

	/***
	 * SVN Project
	 */
	@At("/admin/frame/project/main")
	@Ok("fm:template.admin.project.frame.main")
	public void project_main() {
	}

	@At("/admin/frame/project/left")
	@Ok("fm:template.admin.project.frame.left")
	public void project_left() {
	}

	@At("/admin/frame/project/right")
	@Ok("fm:template.admin.project.frame.right")
	public void project_right() {
	}
}
