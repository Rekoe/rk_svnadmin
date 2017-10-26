package com.rekoe.module.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.integration.shiro.annotation.NutzRequiresPermissions;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.lang.Mirror;
import org.nutz.lang.Strings;
import org.nutz.mvc.NutConfigException;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Attr;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.web.ajax.Ajax;

import com.alibaba.druid.util.DruidWebUtils;
import com.rekoe.common.Message;
import com.rekoe.common.page.Pagination;
import com.rekoe.domain.Role;
import com.rekoe.domain.User;
import com.rekoe.service.RoleService;
import com.rekoe.service.UserService;

/**
 * @author 科技㊣²º¹³<br/>
 *         2014年2月3日 下午4:48:45 <br/>
 *         http://www.rekoe.com<br/>
 *         QQ:5382211
 */
@IocBean
@At("/admin/user")
public class AdminUserAct {

	@Inject
	private UserService userService;
	@Inject
	private RoleService roleService;

	@At
	@Ok("fm:template.admin.common.main")
	@RequiresAuthentication
	public void main() {
	}

	@At
	@Ok("fm:template.admin.user.list")
	@NutzRequiresPermissions(value = "system.user:view", name = "浏览账号", tag = "账号管理", enable = true)
	public Pagination list(@Param(value = "pageNumber", df = "1") int pageNumber) {
		return userService.getUserListByPager(pageNumber, 20);
	}

	@At
	@Ok("fm:template.admin.user.add")
	@RequiresPermissions("system.user:add")
	public List<Role> add() {
		return roleService.list();
	}

	@At
	@Ok("json")
	@NutzRequiresPermissions(value = "system.user:delete", name = "删除账号", tag = "账号管理", enable = true)
	public Message delete(@Param("ids") int[] uids, HttpServletRequest req) {
		userService.update(Chain.make("is_del", true), Cnd.where("id", "id", uids));
		return Message.success("admin.message.success", req);
	}

	@At
	@Ok(">>:${obj==true?'/admin/user/list.rk':'/admin/common/unauthorized.rk'}")
	@NutzRequiresPermissions(value = "system.user:add", name = "添加账号", tag = "账号管理", enable = true)
	public boolean save(HttpServletRequest req, @Param("username") String username, @Param("password") String password, @Param("isEnabled") boolean isEnabled, @Param("roleIds") int[] roleIds) {
		return userService.save(username, password, isEnabled, req.getRemoteAddr(), roleIds);
	}

	@At("/check/username")
	@Ok("raw")
	public boolean checkName(@Param("username") String username) {
		return Lang.isEmpty(userService.fetchByName(username)) ? true : false;
	}

	@At("/check_email")
	@Ok("raw")
	public boolean checkEmail(@Param("email") String email) {
		return Strings.isEmail(email);
	}

	@At
	@Ok("fm:template.admin.user.edit")
	@Fail("json")
	@NutzRequiresPermissions(value = "system.user:edit", name = "编辑账号", tag = "账号管理", enable = true)
	public User edit(@Attr("me") User user, @Param("id") long id, HttpServletRequest req) {
		User editUser = userService.view(id);
		if (Lang.isEmpty(editUser) || editUser.isLocked()) {
			throw new NutConfigException(String.format("先解除帐号 %s 的锁定状态", editUser.getName()));
		}
		boolean isSupper = user.isSystem();
		if (user.getId() != editUser.getId()) {
			if (!isSupper && editUser.isSystem()) {
				throw new NutConfigException("不可以编辑比自己权限高的账号");
			}
		}
		List<Role> roleList = user.getRoles();
		if (isSupper) {
			roleList = roleService.list();
		}
		req.setAttribute("roleList", roleList);
		return editUser;
	}

	@At
	@Ok(">>:/admin/user/list")
	@RequiresPermissions("system.user:edit")
	public Object update(@Param("id") long id, @Param("allServer") Boolean allServer, @Param("serverIds") Integer[] serverIds, @Param("roleIds") Integer[] roleIds) {
		User user = userService.fetch(id);
		userService.removeUserUpdata(user);
		user.setRoles(roleService.loadRoles(roleIds));
		userService.insertRelations(user);
		return user;
	}

	/**
	 * 锁定用户
	 */
	@At
	@Ok("json")
	@NutzRequiresPermissions(value = "system.user:lock", name = "锁定账号", tag = "账号管理", enable = true)
	public Message lock(@Param("id") long id, HttpServletRequest req) {
		User user = userService.view(id);
		if (Lang.isEmpty(user)) {
			return Message.error("common.error.lock.account.empty", req);
		}
		if (user.isSystem()) {
			return Message.error("common.error.lock.account.system", req);
		}
		boolean lock = user.isLocked();
		user.setLocked(!lock);
		userService.updateLock(user);
		return Message.success("button.submit.success", req);
	}

	@At("/profile/check_current_password")
	@Ok("raw")
	@RequiresUser
	public boolean checkCurrentPassword(@Param("currentPassword") String password) {
		if (StringUtils.isBlank(password)) {
			return false;
		}
		Object principal = SecurityUtils.getSubject().getPrincipal();
		User user = (User) principal;
		if (StringUtils.equals(new Sha256Hash(password, user.getSalt(), 1024).toBase64(), user.getPassword())) {
			return true;
		}
		return false;
	}

	@At("/profile/edit")
	@Ok("fm:template.admin.profile.edit")
	@RequiresUser
	public Subject profileEdit() {
		return SecurityUtils.getSubject();
	}

	/**
	 * 添加账号
	 */
	@At
	@Ok("fm:template.admin.user.add_user")
	@RequiresPermissions("system.user:add")
	public void add_user() {
	}

	@At
	@Ok(">>:/admin/user/list")
	@RequiresPermissions("system.user:add")
	public Object save_user(@Param("::user.") User user, HttpServletRequest req) {
		User selectUser = userService.fetchByName(user.getName());
		if (Lang.isEmpty(selectUser)) {
			userService.regist(user, DruidWebUtils.getRemoteAddr(req));
			return Ajax.ok();
		}
		return Ajax.fail();
	}

	@At("/profile/update")
	@Ok("fm:template.admin.profile.edit")
	@RequiresUser
	public boolean profileUpdate(@Param("currentPassword") String currentPassword, @Param("password") String password) {
		if (checkCurrentPassword(currentPassword)) {
			Object principal = SecurityUtils.getSubject().getPrincipal();
			Object uid = Mirror.me(User.class).getValue(principal, "id");
			userService.updatePwd(uid, password);
			return true;
		}
		return false;
	}

	@At("/profile/re_update")
	@Ok(">>:${obj?'/admin/main':'/admin/common/unauthorized.rk'}")
	public boolean regUpate(@Param("username") String username, @Param("password") String password, @Attr("me") User suser) {
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			return false;
		}
		User selectUser = userService.fetchByName(username);
		if (!Lang.isEmpty(selectUser)) {
			return false;
		}
		suser.setSystem(false);
		suser.setName(username);
		String salt = new SecureRandomNumberGenerator().nextBytes().toBase64();
		suser.setSalt(salt);
		suser.setPassword(new Sha256Hash(password, salt, 1024).toBase64());
		userService.update(suser);
		return true;
	}

	@At
	@Ok("fm:template.admin.account.change_pwd")
	@RequiresUser
	public void change_pwd() {

	}

	@At
	@Ok("json")
	@RequiresUser
	public Object pwd_updata(@Param("oldpwd") String oldpwd, @Param("newpwd") String newpwd, @Param("rewpwd") String rewpwd, @Attr("me") User user) {
		if (StringUtils.isNotBlank(newpwd)) {
			if (Lang.equals(newpwd, rewpwd)) {
				String oldSalt = user.getSalt();
				String $oldPwd = new Sha256Hash(oldpwd, oldSalt, 1024).toBase64();
				if (Lang.equals($oldPwd, user.getPassword())) {
					RandomNumberGenerator rng = new SecureRandomNumberGenerator();
					String salt = rng.nextBytes().toBase64();
					String hashedPasswordBase64 = new Sha256Hash(newpwd, salt, 1024).toBase64();
					user.setSalt(salt);
					user.setPassword(hashedPasswordBase64);
					userService.update(user);
					return Ajax.ok();
				} else {
					return Ajax.fail().setMsg("旧的密码错误");
				}
			} else {
				return Ajax.fail().setMsg("两次输入的密码不一致");
			}
		} else {
			return Ajax.fail().setMsg("密码不能为空");
		}
	}
}
