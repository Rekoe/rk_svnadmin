package com.rekoe.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.lang.Times;
import org.nutz.lang.random.R;

import com.rekoe.common.page.Pagination;
import com.rekoe.domain.Role;
import com.rekoe.domain.User;

/**
 * @author 科技㊣²º¹³ <br />
 *         2014年2月3日 下午4:48:45 <br />
 *         http://www.rekoe.com <br />
 *         QQ:5382211
 */
@IocBean(args = { "refer:dao" })
public class UserService extends BaseService<User> {

	public UserService(Dao dao) {
		super(dao);
	}

	public List<User> list() {
		return query(null, null);
	}

	public int update(User user) {
		return dao().update(user);
	}

	public void update(long uid, String password, boolean isLocked, Integer[] ids) {
		User user = fetch(uid);
		dao().clearLinks(user, "roles");
		if (!Lang.isEmptyArray(ids)) {
			user.setRoles(dao().query(Role.class, Cnd.where("id", "in", ids)));
		}
		if (StringUtils.isNotBlank(password)) {
			String salt = new SecureRandomNumberGenerator().nextBytes().toBase64();
			user.setSalt(salt);
			user.setPassword(new Sha256Hash(password, salt, 1024).toBase64());
		}
		user.setLocked(isLocked);
		dao().update(user);
		if (!Lang.isEmpty(user.getRoles())) {
			dao().insertRelation(user, "roles");
		}
	}

	public void updatePwd(Object uid, String password) {
		String salt = new SecureRandomNumberGenerator().nextBytes().toBase64();
		dao().update(User.class, Chain.make("password", new Sha256Hash(password, salt, 1024).toBase64()).add("salt", salt), Cnd.where("id", "=", uid));
	}

	public User insert(User user) {
		user = dao().insert(user);
		return dao().insertRelation(user, "roles");
	}

	public boolean save(String username, String password, boolean isEnabled, String addr, int[] roleIds) {
		User user = new User();
		user.setCreateDate(Times.now());
		user.setDescription("--");
		user.setLocked(!isEnabled);
		user.setName(username);
		user.setRegisterIp(addr);
		user.setRoles(dao().query(Role.class, Cnd.where("id", "in", roleIds)));
		String salt = new SecureRandomNumberGenerator().nextBytes().toBase64();
		user.setSalt(salt);
		user.setPassword(new Sha256Hash(password, salt, 1024).toBase64());
		insert(user);
		return true;
	}

	public User view(Long id) {
		User user = fetch(id);
		dao().fetchLinks(user, null);
		return user;
	}

	public User fetchByName(String name) {
		User user = fetch(Cnd.where("name", "=", name));
		dao().fetchLinks(user, null);
		return user;
	}

	public List<String> getRoleNameList(User user) {
		List<String> roleNameList = new ArrayList<String>();
		for (Role role : user.getRoles()) {
			roleNameList.add(role.getName());
		}
		return roleNameList;
	}

	public void addRole(Long userId, Long roleId) {
		User user = fetch(userId);
		Role role = new Role();
		role.setId(roleId);
		user.setRoles(Lang.list(role));
		dao().insertRelation(user, "roles");
	}

	public void removeRole(Long userId, Long roleId) {
		dao().clear("system_user_role", Cnd.where("userid", "=", userId).and("roleid", "=", roleId));
	}

	public Pagination getUserListByPager(Integer pageNumber, int pageSize) {
		return getObjListByPager(pageNumber, pageSize, null);
	}

	public User initUser(String name, String openid, String providerid, String addr) {
		User temp = dao().fetch(getEntityClass(), Cnd.where("name", "=", name));
		if (!Lang.isEmpty(temp)) {
			name += R.random(2, 5);
		}
		User user = new User();
		user.setCreateDate(Times.now());
		user.setName(name);
		user.setOpenid(openid);
		user.setProviderid(providerid);
		user.setRegisterIp(addr);
		user.setLocked(true);
		user.setSystem(false);
		return dao().insert(user);
	}

	public User fetchByOpenID(String openid) {
		User user = fetch(Cnd.where("openid", "=", openid));
		if (!Lang.isEmpty(user) && !user.isLocked()) {
			dao().fetchLinks(user, "servers");
			dao().fetchLinks(user, "roles");
		}
		return user;
	}

	public User regist(User user, String addr) {
		user.setCreateDate(Times.now());
		user.setRegisterIp(addr);
		user.setSystem(false);
		String salt = new SecureRandomNumberGenerator().nextBytes().toBase64();
		user.setSalt(salt);
		user.setPassword(new Sha256Hash(user.getPassword(), salt, 1024).toBase64());
		return dao().insert(user);
	}

	public void removeUserUpdata(User user) {
		dao().clear("system_user_role", Cnd.where("USERID", "=", user.getId()));
		dao().clear("system_user_server", Cnd.where("USERID", "=", user.getId()));
	}

	public void insertRelations(User user) {
		dao().insertRelation(user, "servers");
		dao().insertRelation(user, "roles");
	}

	public void updateLock(User user) {
		dao().update(user, "^(locked)$");
	}

	public void loadRolePermission(User user) {
		List<Role> roleList = user.getRoles();
		for (Role role : roleList) {
			dao().fetchLinks(role, "permissions");
		}
	}
}
