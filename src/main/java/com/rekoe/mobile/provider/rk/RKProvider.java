package com.rekoe.mobile.provider.rk;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.nutz.http.Http;
import org.nutz.http.Response;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import com.rekoe.annotation.PlatformProvider;
import com.rekoe.domain.PlatformConfig;
import com.rekoe.mobile.Profile;
import com.rekoe.mobile.provider.AbstractProvider;
import com.rekoe.utils.Constants;

@IocBean
@PlatformProvider(name = Constants.RK)
public class RKProvider extends AbstractProvider<RKParam> {

	private static final long serialVersionUID = 2882126009485835543L;

	private final static Log log = Logs.get();

	private final static String TOKEN_VALIDATE = "/oauth/accessToken";
	private final static String TOKEN_FOR_USER = "/v1/openapi/userinfo?access_token=";

	@Override
	public Profile verifyResponse(RKParam param) throws Exception {
		String code = param.getCode();
		PlatformConfig platformConfig = getPlatformConfig(param.getPid());
		String clientId = platformConfig.getConfig().getString("client_id");
		String grantType = platformConfig.getConfig().getString("grant_type");
		String clientSecret = platformConfig.getConfig().getString("client_secret");
		String redirectUri = platformConfig.getConfig().getString("redirect_uri");
		String validateUrl = platformConfig.getConfig().getString("validate_url");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("client_id", clientId);
		params.put("grant_type", grantType);
		params.put("client_secret", clientSecret);
		params.put("code", code);
		params.put("redirect_uri", redirectUri);
		try {
			Response res = Http.post2(validateUrl + TOKEN_VALIDATE, params, 60);
			if (res.isOK()) {
				String resultToken = res.getContent();
				NutMap map = Json.fromJson(NutMap.class, resultToken);
				String accessToken = map.getString("access_token");
				params.clear();
				if (StringUtils.isNotBlank(accessToken)) {
					params.put("access_token", accessToken);
					String resultvValidateToken = Http.get(validateUrl + TOKEN_FOR_USER + accessToken).getContent();
					NutMap userInfo = Json.fromJson(NutMap.class, resultvValidateToken);
					return new Profile(getProviderId(), userInfo.getString("uid"), param.getPid());
				}
				log.error(resultToken);
			}
		} catch (Exception e) {
			log.errorf("url:[%s]", validateUrl);
		}
		return null;
	}

	@Override
	public String getProviderId() {
		return Constants.RK;
	}

}
