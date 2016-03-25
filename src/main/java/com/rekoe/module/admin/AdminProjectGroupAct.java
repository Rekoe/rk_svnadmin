package com.rekoe.module.admin;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
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
import com.rekoe.service.ProjectGroupService;

@IocBean
@At("/admin/project/group")
public class AdminProjectGroupAct extends BaseAction {

	@Inject
	private ProjectGroupService projectGroupService;

	@At
	@Ok("fm:template.admin.project_group.list")
	@RequiresPermissions({ "project.group:view" })
	@PermissionTag(name = "SVN浏览账号", tag = "SVN账号管理")
	public Pagination list(@Param(value = "pageNumber", df = "1") int page, @Param("pj") String pj) {
		return projectGroupService.getObjListByPager(page, 20, Cnd.where("pj", "=", pj));
	}

	@At
	@Ok("fm:template.admin.project_group.add")
	@RequiresPermissions({ "project.group:add" })
	@PermissionTag(name = "添加项目用户组", tag = "SVN账号管理", enable = true)
	public void add() {
	}

	@At
	@Ok("json")
	@RequiresPermissions("project.group:add")
	@PermissionTag(name = "添加项目用户组", tag = "SVN账号管理", enable = false)
	public Message o_save(@Param("::group.") Usr group, HttpServletRequest req) {
		return Message.error("error", req);
	}

	@At
	@Ok("json")
	@RequiresPermissions("project.group:delete")
	@PermissionTag(name = "删除项目用户组", tag = "SVN账号管理", enable = true)
	public Message delete(@Param("pj") String pj, @Param("gr") String gr, HttpServletRequest req) {
		projectGroupService.delete(pj, gr);
		return Message.success("ok", req);
	}
}
