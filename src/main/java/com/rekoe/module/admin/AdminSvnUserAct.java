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
@At("/admin/project/group")
public class AdminSvnUserAct extends BaseAction {

	@Inject
	private SvnUserService projectUserService;

	@At
	@Ok("fm:template.admin.project_group.list")
	@RequiresPermissions({ "project.group:view" })
	@PermissionTag(name = "SVN浏览账号", tag = "SVN账号管理")
	public Pagination list(@Param(value = "pageNumber", df = "1") int page) {
		return projectUserService.getObjListByPager(page, 20, null);
	}

	@At
	@Ok("fm:template.admin.project_user.add")
	@RequiresPermissions({ "project.group:add" })
	@PermissionTag(name = "SVN添加账号", tag = "SVN账号管理", enable = true)
	public void add() {
	}

	@At
	@Ok("json")
	@RequiresPermissions("project.group:add")
	@PermissionTag(name = "SVN添加账号", tag = "SVN账号管理", enable = false)
	public Message o_save(@Param("::user.") Usr user, HttpServletRequest req) {
		boolean isOk = projectUserService.nameOk(user.getUsr());
		if (isOk) {
			isOk = projectUserService.insert(user);
		}
		if (isOk) {
			return Message.success("ok", req);
		}
		return Message.error("error", req);
	}
}
