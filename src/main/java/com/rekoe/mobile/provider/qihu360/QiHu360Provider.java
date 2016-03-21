package com.rekoe.mobile.provider.qihu360;

import java.util.HashMap;
import java.util.Map;

import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;

import com.rekoe.annotation.PlatformProvider;
import com.rekoe.mobile.Profile;
import com.rekoe.mobile.provider.AbstractProvider;
import com.rekoe.utils.Constants;

@IocBean
@PlatformProvider(name = Constants.QIHU360)
public class QiHu360Provider extends AbstractProvider<QiHu360Param> {

	private static final long serialVersionUID = 2882126009485835543L;

	private static final Map<String, String> ENDPOINTS;

	static {
		ENDPOINTS = new HashMap<String, String>();
		ENDPOINTS.put(Constants.OAUTH_AUTHORIZATION_URL, "https://openapi.360.cn/user/me.json?access_token=");
	}

	@Override
	public String getProviderId() {
		return Constants.QIHU360;
	}

	@Override
	public Profile verifyResponse(QiHu360Param param) throws Exception {
		String params = HttpsGet((ENDPOINTS.get(Constants.OAUTH_AUTHORIZATION_URL) + param.getSessionid()));
		if (!Strings.isBlank(params)) {
			Map<String, String> result = result2Json(params, String.class);
			String userId = result.get("id");
			if (!Strings.isBlank(params)) {
				return new Profile(getProviderId(), userId, param.getPid());
			}
		}
		return null;
	}
}
