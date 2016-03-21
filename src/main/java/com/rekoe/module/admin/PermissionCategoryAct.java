package com.rekoe.module.admin;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.rekoe.annotation.PermissionTag;
import com.rekoe.common.Message;
import com.rekoe.common.page.Pagination;
import com.rekoe.domain.PermissionCategory;
import com.rekoe.service.PermissionCategoryService;

/**
 * @author 科技㊣²º¹³<br/>
 *         2014年2月3日 下午4:48:45<br/>
 *         http://www.rekoe.com<br/>
 *         QQ:5382211
 */
@IocBean
@At("/admin/permission/category")
public class PermissionCategoryAct {

	@Inject
	private PermissionCategoryService permissionCategoryService;

	@At
	@Ok("fm:template.admin.user.permission_category.list")
	@RequiresPermissions({ "system.permission:view" })
	@PermissionTag(name = "浏览权限", tag = "权限管理", enable = false)
	public Pagination list(@Param(value = "pageNumber", df = "1") Integer pageNumber) {
		return permissionCategoryService.getPermissionCategoryListByPager(pageNumber);
	}

	@At
	@Ok("fm:template.admin.user.permission_category.edit")
	@RequiresPermissions({ "system.permission:edit" })
	@PermissionTag(name = "编辑权限", tag = "权限管理", enable = false)
	public PermissionCategory edit(String id) {
		return permissionCategoryService.fetchByID(id);
	}

	@At
	@Ok(">>:/admin/permission/category/list.rk")
	@RequiresPermissions({ "system.permission:edit" })
	@PermissionTag(name = "编辑权限", tag = "权限管理", enable = false)
	public boolean update(@Param("name") String name, @Param("id") String id) {
		permissionCategoryService.update(Chain.make("name", name), Cnd.where("id", "=", id));
		return true;
	}

	@At
	@Ok("fm:template.admin.user.permission_category.add")
	@RequiresPermissions({ "system.permission:add" })
	public void add() {
	}

	@At
	@Ok(">>:/admin/permission/category/list.rk")
	@RequiresPermissions({ "system.permission:add" })
	@PermissionTag(name = "添加权限", tag = "权限管理", enable = false)
	public void save(@Param("name") String name) {
		PermissionCategory pc = new PermissionCategory();
		pc.setName(name);
		permissionCategoryService.insert(pc);
	}

	@At
	@Ok("json")
	@RequiresPermissions({ "system.permission:delete" })
	@PermissionTag(name = "删除权限", tag = "权限管理", enable = false)
	public Message delete(@Param("id") String id, HttpServletRequest req) {
		PermissionCategory pc = permissionCategoryService.fetchByID(id);
		if (pc.isLocked()) {
			return Message.error("admin.permissionCategory.deleteLockedNotAllowed", req);
		}
		permissionCategoryService.remove(id);
		return Message.success("admin.common.success", req);
	}
}
