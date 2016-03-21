package com.rekoe.shiro.realm;

import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.nutz.castor.Castors;
import org.nutz.ioc.Ioc;
import org.nutz.lang.Lang;
import org.nutz.mvc.Mvcs;

import com.rekoe.domain.Role;
import com.rekoe.domain.User;
import com.rekoe.mobile.Profile;
import com.rekoe.service.RoleService;
import com.rekoe.service.UserService;

/**
 * @author 科技㊣²º¹³<br />
 *         2014年2月3日 下午4:48:45 <br />
 *         http://www.rekoe.com QQ:5382211
 */
public abstract class AbstractNutAuthoRealm extends AuthorizingRealm {

	private UserService userService;
	private RoleService roleService;

	protected UserService getUserService() {
		if (Lang.isEmpty(userService)) {
			Ioc ioc = Mvcs.getIoc();
			userService = ioc.get(UserService.class);
		}
		return userService;
	}

	protected RoleService getRoleService() {
		if (Lang.isEmpty(roleService)) {
			Ioc ioc = Mvcs.getIoc();
			roleService = ioc.get(RoleService.class);
		}
		return roleService;
	}

	/**
	 * 更新用户授权信息缓存.
	 */
	public void clearCachedAuthorizationInfo(String principal) {
		SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
		clearCachedAuthorizationInfo(principals);
	}

	/**
	 * 清除所有用户授权信息缓存.
	 */
	public void clearAllCachedAuthorizationInfo() {
		Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
		if (cache != null) {
			for (Object key : cache.keys()) {
				cache.remove(key);
			}
		}
	}

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		Object object = principals.getPrimaryPrincipal();
		if (object.getClass().isAssignableFrom(User.class)) {
			User user = Castors.me().castTo(object, User.class);
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			info.addRoles(getUserService().getRoleNameList(user));
			for (Role role : user.getRoles()) {
				info.addStringPermissions(getRoleService().getPermissionNameList(role));
			}
			return info;
		} else if (object.getClass().isAssignableFrom(Profile.class)) {
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			info.addStringPermission("auth.login");
			return info;
		}
		return null;
	}
}
