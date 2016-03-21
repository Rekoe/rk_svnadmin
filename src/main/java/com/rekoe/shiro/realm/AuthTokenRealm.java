package com.rekoe.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.nutz.castor.Castors;
import org.nutz.ioc.Ioc;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.lang.Lang;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;

import com.rekoe.mobile.AbstractParam;
import com.rekoe.mobile.Profile;
import com.rekoe.mobile.provider.AbstractProvider;
import com.rekoe.service.PlatformProviderService;

/**
 * @author 科技㊣²º¹³<br />
 *         2014年2月3日 下午4:48:45<br />
 *         http://www.rekoe.com<br />
 *         QQ:5382211
 */
public class AuthTokenRealm extends AbstractNutAuthoRealm {

	private final static Log log = Logs.get();

	private PlatformProviderService platformProviderService;

	public AuthTokenRealm() {
		setAuthenticationTokenClass(AuthToken.class);
	}

	private PlatformProviderService getPlatformProviderService() {
		if (Lang.isEmpty(platformProviderService)) {
			Ioc ioc = Mvcs.getIoc();
			this.platformProviderService = ioc.get(PlatformProviderService.class);
		}
		return platformProviderService;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws DisabledAccountException {
		AuthToken oauthToken = Castors.me().castTo(token, AuthToken.class);
		AbstractParam param = oauthToken.getPrincipal();
		AbstractProvider<AbstractParam> platformProvider = getPlatformProviderService().getAbstractProvider(param.getProviderId());
		if (Lang.isEmpty(platformProvider)) {
			throw Lang.makeThrow(UnknownAccountException.class, "UnknownPlatformProvider [ %s ] ERROR.", param.getProviderId());
		}
		Profile profile = null;
		try {
			profile = platformProvider.verifyResponse(param);
		} catch (Exception e) {
			log.errorf("PlatformProvider [%s] verifyResponse ERROR: %s", param.getProviderId(), e);
		}
		if (Lang.isEmpty(profile)) {
			throw Lang.makeThrow(AuthenticationException.class, " [ %s ] Authentication ERROR.", Json.toJson(param, JsonFormat.compact()));
		}
		SimpleAuthenticationInfo account = new SimpleAuthenticationInfo(profile, param, getName());
		return account;
	}

}
