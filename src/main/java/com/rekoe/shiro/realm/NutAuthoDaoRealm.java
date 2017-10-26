package com.rekoe.shiro.realm;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.brickred.socialauth.Profile;
import org.nutz.castor.Castors;
import org.nutz.lang.Lang;

import com.rekoe.domain.User;

/**
 * @author 科技㊣²º¹³<br />
 *         2014年2月3日 下午<br/>
 *         4:48:45<br/>
 *         http://www.rekoe.com <br />
 *         QQ:5382211
 */
public class NutAuthoDaoRealm extends AbstractNutAuthoRealm {

	public NutAuthoDaoRealm() {
		setAuthenticationTokenClass(OAuthToken.class);
	}

	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws DisabledAccountException {
		OAuthToken oauthToken = Castors.me().castTo(token, OAuthToken.class);
		Profile credential = oauthToken.getCredentials();
		String openid = credential.getValidatedId();
		User user = getUserService().fetchByOpenID(openid);
		if (Lang.isEmpty(user)) {
			String nickName = StringUtils.defaultString(credential.getDisplayName(), openid);
			String providerid = credential.getProviderId();
			user = getUserService().initUser(nickName, openid, providerid, oauthToken.getAddr());
		} else {
			if (user.isLocked()) {
				throw Lang.makeThrow(LockedAccountException.class, "Account [ %s ] is locked.", user.getName());
			}
			getUserService().loadRolePermission(user);
		}
		oauthToken.setRname(user.isSystem());
		oauthToken.setUserId(openid);
		SimpleAuthenticationInfo account = new SimpleAuthenticationInfo(user, credential, getName());
		oauthToken.getSession().setAttribute("me", user);
		return account;
	}
}
