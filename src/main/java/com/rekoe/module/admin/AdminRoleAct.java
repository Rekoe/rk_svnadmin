package com.rekoe.module.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.mvc.NutConfigException;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.rekoe.annotation.PermissionTag;
import com.rekoe.common.Message;
import com.rekoe.domain.Permission;
import com.rekoe.domain.PermissionCategory;
import com.rekoe.domain.Role;
import com.rekoe.service.PermissionCategoryService;
import com.rekoe.service.PermissionService;
import com.rekoe.service.RoleService;

/**
 * @author 科技㊣²º¹³<br />
 *         2014年2月3日 下午4:48:45<br/>
 *         http://www.rekoe.com<br/>
 *         QQ:5382211
 */
@IocBean
@At("/admin/role")
public class AdminRoleAct {

	@Inject
	private RoleService roleService;

	@Inject
	private PermissionService permissionService;

	@Inject
	private PermissionCategoryService permissionCategoryService;

	@At
	@Ok("fm:template.admin.user.role.list")
	@RequiresPermissions("system.role:view")
	@PermissionTag(name = "浏览角色", tag = "角色管理")
	public Object list(@Param(value = "pageNumber", df = "1") int pageNumber) {
		return roleService.getRoleListByPager(pageNumber, 20);
	}

	@At
	@Ok("fm:template.admin.user.role.edit")
	@RequiresPermissions("system.role:edit")
	@PermissionTag(name = "编辑角色", tag = "角色管理")
	public Object edit(@Param("id") long id, HttpServletRequest req) {
		Role role = roleService.view(id);
		List<PermissionCategory> pcList = permissionCategoryService.list();
		req.setAttribute("pcList", pcList);
		return role;
	}

	@At
	@Ok(">>:/admin/role/list.rk")
	@RequiresPermissions("system.role:edit")
	public void update(@Param("::role.") Role tempRole, @Param("name") String name, @Param("authorities") int[] permIds) {
		Role $role = roleService.view(tempRole.getId());
		if (!Lang.isEmpty($role)) {
			List<Permission> perms = permissionService.query(Cnd.where("id", "in", permIds), null);
			$role.setDescription(tempRole.getDescription());
			$role.setName(name);
			roleService.updateRoleRelation($role, perms);
		} else {
			throw new NutConfigException("用户不存在");
		}
	}

	/**
	 * 添加新的角色
	 */
	@At
	@Ok("fm:template.admin.user.role.add")
	@RequiresPermissions("system.role:add")
	@PermissionTag(name = "添加角色", tag = "角色管理")
	public List<PermissionCategory> add(HttpServletRequest req) {
		return permissionCategoryService.list();
	}

	@At
	@Ok(">>:${obj==true?'/role/list.rk':'/admin/common/unauthorized.rk'}")
	@RequiresPermissions("system.role:add")
	@PermissionTag(name = "添加角色", tag = "角色管理", enable = false)
	public boolean save(@Param("name") String name, @Param("description") String desc, @Param("authorities") int[] ids) {
		Role role = roleService.fetchByName(name);
		if (Lang.isEmpty(role)) {
			role = new Role();
			role.setDescription(desc);
			role.setName(name);
			role.setPermissions(permissionService.query(Cnd.where("id", "in", ids), null));
			roleService.insert(role);
			return true;
		}
		return false;
	}

	@At
	@Ok("json")
	@RequiresPermissions("system.role:delete")
	@PermissionTag(name = "删除角色", tag = "角色管理")
	public Message delete(@Param("ids") Long[] uids, HttpServletRequest req) {
		for (Long id : uids) {
			roleService.delete(id);
		}
		return Message.success("admin.message.success", req);
	}
}
