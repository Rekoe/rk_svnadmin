package com.rekoe.module.admin;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;

import com.rekoe.module.BaseAction;

@IocBean
public class WelcomeAct extends BaseAction {

	@At("/admin/index_main")
	@Ok("fm:template.admin.main")
	public void index() {
	}

	@At("/admin/top")
	@Ok("fm:template.admin.top")
	@RequiresUser
	public boolean top(HttpServletRequest req) {
		return true;
	}

	@At("/admin/main")
	@Ok("fm:template.admin.index")
	public void main() {
	}

	@At("/admin/left")
	@Ok("fm:template.admin.left")
	public void left() {
	}

	@At("/admin/right")
	@Ok("fm:template.admin.right")
	public void right() {
	}

}
