package com.rekoe.shiro.realm;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.nutz.castor.Castors;
import org.nutz.integration.shiro.AbstractSimpleAuthorizingRealm;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;

import com.rekoe.domain.Role;
import com.rekoe.domain.User;
import com.rekoe.exception.CreateUserSaltException;
import com.rekoe.service.RoleService;
import com.rekoe.service.UserService;

@IocBean(name = "shiroRealm", create = "_init")
public class UsernamePasswordRealm extends AbstractSimpleAuthorizingRealm {

	@Inject
	private UserService userService;
	@Inject
	private RoleService roleService;

	@Inject
	private org.apache.shiro.cache.CacheManager shiroCacheManager;

	public void _init() {
		setCacheManager(shiroCacheManager);
	}

	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof UsernamePasswordToken;
	}

	public UsernamePasswordRealm() {
		setAuthenticationTokenClass(UsernamePasswordToken.class);
	}

	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws DisabledAccountException {
		UsernamePasswordToken authcToken = Castors.me().castTo(token, UsernamePasswordToken.class);
		String accountName = authcToken.getUsername();
		if (StringUtils.isBlank(accountName)) {
			throw Lang.makeThrow(AuthenticationException.class, "Account is empty");
		}
		User user = userService.fetchByName(authcToken.getUsername());
		if (Lang.isEmpty(user)) {
			throw Lang.makeThrow(UnknownAccountException.class, "Account [ %s ] not found", authcToken.getUsername());
		}
		if (user.isLocked()) {
			throw Lang.makeThrow(LockedAccountException.class, "Account [ %s ] is locked.", authcToken.getUsername());
		}
		String userSalt = user.getSalt();
		if (Strings.isBlank(userSalt)) {
			throw Lang.makeThrow(CreateUserSaltException.class, "Account [ %s ] is not set PassWord", authcToken.getUsername());
		}
		userService.loadRolePermission(user);
		ByteSource salt = ByteSource.Util.bytes(user.getSalt());
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPassword(), getName());
		info.setCredentialsSalt(salt);
		return info;
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		Object object = principals.getPrimaryPrincipal();
		User user = Castors.me().castTo(object, User.class);
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.addRoles(userService.getRoleNameList(user));
		for (Role role : user.getRoles()) {
			info.addStringPermissions(roleService.getPermissionNameList(role));
		}
		return info;
	}
}
