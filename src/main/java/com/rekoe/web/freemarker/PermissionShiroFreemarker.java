package com.rekoe.web.freemarker;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.PermissionResolver;
import org.apache.shiro.authz.permission.WildcardPermissionResolver;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.PermissionUtils;
import org.apache.shiro.util.StringUtils;
import org.nutz.lang.Lang;

import com.rekoe.domain.Role;
import com.rekoe.domain.User;
import com.rekoe.utils.DirectiveUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class PermissionShiroFreemarker implements TemplateDirectiveModel {

	private PermissionResolver permissionResolver;

	private final static String PERM = "perm";

	public PermissionShiroFreemarker(PermissionResolver permissionResolver) {
		this.permissionResolver = permissionResolver;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		Subject subject = SecurityUtils.getSubject();
		if (subject.getPrincipal() instanceof User) {
			User user = (User) subject.getPrincipal();
			List<Role> roleList = user.getRoles();
			if (Lang.isEmpty(roleList)) {
				return;
			}
			Set<String> set = new HashSet();
			for (Role role : roleList) {
				List<com.rekoe.domain.Permission> perList = role.getPermissions();
				if (Lang.isEmpty(perList)) {
					continue;
				}
				for (com.rekoe.domain.Permission permission : perList) {
					if (Lang.isEmpty(permission)) {
						continue;
					}
					set.add(permission.getName());
				}
			}
			if (Lang.isEmpty(set)) {
				return;
			}
			boolean isRight = false;
			String wildcardString = DirectiveUtils.getString(PERM, params);
			String[] ps = StringUtils.split(wildcardString);
			Collection<Permission> systemPerms = resolvePermissions(ps);
			for (Permission permission : systemPerms) {
				for (String uPerm : set) {
					if (permission.implies(permissionResolver.resolvePermission(uPerm)) || permissionResolver.resolvePermission(uPerm).implies(permission)) {
						isRight = true;
						body.render(env.getOut());
						break;
					}
				}
				if (isRight) {
					break;
				}
			}

		}
	}

	private Collection<Permission> resolvePermissions(String... stringPerms) {
		Collection<String> pe = Lang.array2list(stringPerms, String.class);
		Collection<Permission> perms = PermissionUtils.resolvePermissions(pe, permissionResolver);
		return perms;
	}

	public static void main(String[] args) {
		PermissionResolver permissionResolver = new WildcardPermissionResolver();

		System.out.println(permissionResolver.resolvePermission("*:*:*").implies(permissionResolver.resolvePermission("game.server")));
	}
}
