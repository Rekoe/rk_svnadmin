package com.rekoe.module.admin;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.rekoe.annotation.PermissionTag;
import com.rekoe.common.Message;
import com.rekoe.common.page.Pagination;
import com.rekoe.domain.Usr;
import com.rekoe.module.BaseAction;
import com.rekoe.service.SvnUserService;

@IocBean
@At("/admin/svn/user")
public class AdminSvnUserAct extends BaseAction {

	@Inject
	private SvnUserService svnUserService;

	@At
	@Ok("fm:template.admin.svn_user.list")
	@RequiresPermissions({ "svn.user:view" })
	@PermissionTag(name = "SVN浏览账号", tag = "SVN账号管理")
	public Pagination list(@Param(value = "pageNumber", df = "1") int page) {
		return svnUserService.getObjListByPager(page, 20, null);
	}

	@At
	@Ok("fm:template.admin.svn_user.add")
	@RequiresPermissions({ "svn.user:add" })
	@PermissionTag(name = "SVN添加账号", tag = "SVN账号管理", enable = true)
	public void add() {
	}

	@At
	@Ok("json")
	@RequiresPermissions("svn.user:add")
	@PermissionTag(name = "SVN添加账号", tag = "SVN账号管理", enable = false)
	public Message o_save(@Param("::user.") Usr user, HttpServletRequest req) {
		boolean isOk = svnUserService.nameOk(user.getUsr());
		if (isOk) {
			isOk = svnUserService.insert(user);
		}
		if (isOk) {
			return Message.success("ok", req);
		}
		return Message.error("error", req);
	}
}
