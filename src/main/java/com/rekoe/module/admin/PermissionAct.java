package com.rekoe.module.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Attr;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.web.Webs;

import com.rekoe.annotation.PermissionTag;
import com.rekoe.common.Message;
import com.rekoe.common.page.Pagination;
import com.rekoe.domain.Permission;
import com.rekoe.domain.PermissionCategory;
import com.rekoe.domain.User;
import com.rekoe.service.PermissionCategoryService;
import com.rekoe.service.PermissionService;

/**
 * @author 科技㊣²º¹³<br />
 *         2014年4月19日 上午9:20:28<br />
 *         http://www.rekoe.com<br/>
 *         QQ:5382211
 */
@IocBean
@At("/admin/permission")
public class PermissionAct {

	@Inject
	private PermissionCategoryService permissionCategoryService;
	@Inject
	private PermissionService permissionService;

	@At
	@Ok("fm:template.admin.user.permission.list")
	@RequiresPermissions({ "system.permission:view" })
	@PermissionTag(name = "浏览权限", tag = "权限管理")
	public Pagination list(@Param(value = "pageNumber", df = "1") int pageNumber) {
		return permissionService.getPermissionListByPager(pageNumber);
	}

	@At("/list_category/?")
	@Ok("fm:template.admin.user.permission.list")
	@RequiresPermissions({ "system.permission:view" })
	@PermissionTag(name = "浏览权限分类", tag = "权限管理", enable = false)
	public Pagination listCategory(String id, @Param(value = "pageNumber", df = "1") Integer pageNumber) {
		return permissionService.getPermissionListByPager(pageNumber, id);
	}

	@At
	@Ok("fm:template.admin.user.permission.edit")
	@RequiresPermissions({ "system.permission:edit" })
	@PermissionTag(name = "编辑限分类", tag = "权限管理")
	public List<PermissionCategory> edit(long id, HttpServletRequest req) {
		Permission permission = permissionService.fetch(id);
		req.setAttribute("permission", permission);
		return add();
	}

	@At
	@Ok("fm:template.admin.user.permission.add")
	@RequiresPermissions({ "system.permission:add" })
	@PermissionTag(name = "添加权限", tag = "权限管理")
	public List<PermissionCategory> add() {
		List<PermissionCategory> list = permissionCategoryService.list();
		return list;
	}

	@At
	@Ok("json")
	@RequiresPermissions({ "system.permission:delete" })
	@PermissionTag(name = "删除权限", tag = "权限管理")
	public Message delete(@Attr(Webs.ME) User user, @Param("id") long id, HttpServletRequest req) {
		Permission permission = permissionService.fetch(id);
		if (permission.isLocked() && !user.isSystem()) {
			return Message.error("admin.permissionCategory.deleteLockedNotAllowed", req);
		}
		permissionService.delete(id);
		return Message.success("admin.common.success", req);
	}

	@At
	@Ok(">>:/admin/permission/list.rk")
	@RequiresPermissions({ "system.permission:edit" })
	@PermissionTag(name = "编辑权限", tag = "权限管理", enable = false)
	public boolean update(@Param("::permission.") Permission permission, @Param("description") String description, @Param("name") String name, @Param("id") String id) {
		permission.setName(name);
		permission.setDescription(description);
		permissionService.update(permission);
		return true;
	}

	@At
	@Ok(">>:/admin/permission/list.rk")
	@RequiresPermissions({ "system.permission:add" })
	@PermissionTag(name = "添加权限", tag = "权限管理")
	public void save(@Param("name") String name, @Param("permissionCategoryId") String permissionCategoryId, @Param("description") String description) {
		Permission permission = new Permission();
		permission.setName(name);
		permission.setDescription(description);
		permission.setPermissionCategoryId(permissionCategoryId);
		permissionService.insert(permission);
	}
}
