package com.rekoe.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.ContinueLoop;
import org.nutz.lang.Each;
import org.nutz.lang.ExitLoop;
import org.nutz.lang.Lang;
import org.nutz.lang.LoopException;
import org.nutz.lang.Times;
import org.nutz.lang.random.R;
import org.nutz.resource.Scans;

import com.rekoe.annotation.PermissionTag;
import com.rekoe.common.page.Pagination;
import com.rekoe.domain.Permission;
import com.rekoe.domain.PermissionCategory;
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

	public void update(User user) {
		dao().update(user);
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

	public boolean insert(User user) {
		user = dao().insert(user);
		dao().insertRelation(user, "roles");
		return true;
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

	class PermissionTagClzz {

		private String premission;
		private String tag;
		private String name;

		public PermissionTagClzz(String premission, String tag, String name) {
			super();
			this.premission = premission;
			this.tag = tag;
			this.name = name;
		}

		public String getPremission() {
			return premission;
		}

		public void setPremission(String premission) {
			this.premission = premission;
		}

		public String getTag() {
			return tag;
		}

		public void setTag(String tag) {
			this.tag = tag;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}

	public void initFormPackages(String... pkgs) {
		List<Class<?>> allClazz = new ArrayList<>();
		for (String pkg : pkgs) {
			List<Class<?>> scanPackage = Scans.me().scanPackage(pkg);
			allClazz.addAll(scanPackage);
		}
		Set<String> rpTagNames = new HashSet<>();
		final Set<String> permissions = new HashSet<String>();
		final Map<String, PermissionTagClzz> perTagMap = new HashMap<>();
		for (Class<?> klass : allClazz) {
			for (Method method : klass.getMethods()) {
				RequiresPermissions rp = method.getAnnotation(RequiresPermissions.class);
				if (rp != null && rp.value() != null) {
					PermissionTag rpTag = method.getAnnotation(PermissionTag.class);
					if (rpTag == null || rpTag.enable() == false)
						continue;
					rpTagNames.add(rpTag.tag());
					for (String permission : rp.value()) {
						if (permission != null && !permission.endsWith("*")) {
							permissions.add(permission);
							perTagMap.put(permission, new PermissionTagClzz(permission, rpTag.tag(), rpTag.name()));
						}
					}
				}
			}
		}

		// 整理出 需要添加权限的所有的权限分类
		final Iterator<Entry<String, PermissionTagClzz>> iter = perTagMap.entrySet().iterator();
		final Set<String> perTags = new HashSet<String>();
		while (iter.hasNext()) {
			Entry<String, PermissionTagClzz> entry = iter.next();
			PermissionTagClzz ptc = entry.getValue();
			String name = ptc.getTag();
			perTags.add(name);
		}

		dao().each(PermissionCategory.class, null, new Each<PermissionCategory>() {
			public void invoke(int index, PermissionCategory ele, int length) throws ExitLoop, ContinueLoop, LoopException {
				perTags.remove(ele.getName());
			}
		});

		// 把分类数据插入到数据库
		final Map<String, String> tagIds = new HashMap<String, String>();
		for (String name : perTags) {
			PermissionCategory pc = new PermissionCategory();
			pc.setLocked(true);
			pc.setName(name);
			dao().insert(pc);
		}

		// 把全部权限查出来一一检查
		dao().each(Permission.class, null, new Each<Permission>() {
			public void invoke(int index, Permission ele, int length) throws ExitLoop, ContinueLoop, LoopException {
				permissions.remove(ele.getName());
				perTagMap.remove(ele.getName());
			}
		});

		Iterator<Entry<String, PermissionTagClzz>> iterator = perTagMap.entrySet().iterator();
		Set<String> tagNames = new HashSet<String>();
		while (iterator.hasNext()) {
			Entry<String, PermissionTagClzz> entry = iterator.next();
			tagNames.add(entry.getValue().getTag());
		}
		dao().each(PermissionCategory.class, Cnd.where("name", "in", tagNames), new Each<PermissionCategory>() {
			public void invoke(int index, PermissionCategory ele, int length) throws ExitLoop, ContinueLoop, LoopException {
				tagIds.put(ele.getName(), ele.getId());
			}
		});

		List<Permission> newSet = new ArrayList<>();
		final Iterator<Entry<String, PermissionTagClzz>> $iter = perTagMap.entrySet().iterator();
		while ($iter.hasNext()) {
			Entry<String, PermissionTagClzz> entry = $iter.next();
			PermissionTagClzz ptc = entry.getValue();
			String name = ptc.getTag();
			Permission p = new Permission();
			p.setDescription(ptc.getName());
			p.setLocked(true);
			p.setPermissionCategoryId(tagIds.get(name));
			p.setName(entry.getKey());
			newSet.add(p);
		}

		if (!Lang.isEmpty(newSet)) {
			dao().fastInsert(newSet);
		}
	}

	public void loadRolePermission(User user) {
		List<Role> roleList = user.getRoles();
		for (Role role : roleList) {
			dao().fetchLinks(role, "permissions");
		}
	}
}
