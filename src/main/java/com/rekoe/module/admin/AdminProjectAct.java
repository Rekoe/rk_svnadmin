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
import com.rekoe.domain.Pj;
import com.rekoe.module.BaseAction;
import com.rekoe.service.ProjectService;

@IocBean
@At("/admin/project")
public class AdminProjectAct extends BaseAction{

	@Inject
	private ProjectService projectService;
	
	@At
	@Ok("fm:template.admin.project.list")
	@RequiresPermissions({ "svn.project:view" })
	@PermissionTag(name = "SVN浏览项目", tag = "SVN项目管理")
	public Pagination list(@Param(value = "pageNumber", df = "1") int page) {
		return projectService.getObjListByPager(page, 20, null);
	}

	
	@At
	@Ok("fm:template.admin.project.add")
	@RequiresPermissions({ "svn.project:add" })
	@PermissionTag(name = "SVN添加项目", tag = "SVN项目管理", enable = true)
	public void add() {
	}

	@At
	@Ok("json")
	@RequiresPermissions("svn.project:add")
	@PermissionTag(name = "SVN添加项目", tag = "SVN项目管理", enable = false)
	public Message o_save(@Param("::pj.") Pj pj, HttpServletRequest req) {
		boolean isOk = projectService.nameOk(pj.getPj());
		if (isOk) {
			isOk = projectService.insert(pj);
		}
		if (isOk) {
			return Message.success("ok", req);
		}
		return Message.error("error", req);
	}
}
