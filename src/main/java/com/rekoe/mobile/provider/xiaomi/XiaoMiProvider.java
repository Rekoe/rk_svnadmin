package com.rekoe.mobile.provider.xiaomi;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import com.rekoe.annotation.PlatformProvider;
import com.rekoe.domain.PlatformConfig;
import com.rekoe.mobile.Profile;
import com.rekoe.mobile.provider.AbstractProvider;
import com.rekoe.utils.Constants;
import com.rekoe.utils.HmacSHA1Encryption;

@IocBean
@PlatformProvider(name = Constants.XIAOMI)
public class XiaoMiProvider extends AbstractProvider<XiaoMiParam> {

	private static final long serialVersionUID = 2882126009485835543L;

	private final static Log log = Logs.get();

	private final static String PARAM_URL_STR = "appId={0}&session={1}&uid={2}&signature={3}";
	private final static String ENCRYPT_TEXT = "appId={0}&session={1}&uid={2}";
	private static final Map<String, String> ENDPOINTS;

	private final String ERROR_CODE = "{\"errcode\":-1}";
	static {
		ENDPOINTS = new HashMap<String, String>();
		ENDPOINTS.put(Constants.OAUTH_AUTHORIZATION_URL, "http://mis.migc.xiaomi.com/api/biz/service/verifySession.do?");
	}

	public Map<String, Integer> checkUserAndroidMiLogin(String sdkUin, String sdkToken, String appid, String secret) throws Exception {
		String encryptText = MessageFormat.format(ENCRYPT_TEXT, appid, sdkToken, sdkUin);
		String signature = HmacSHA1Encryption.HmacSHA1Encrypt(encryptText, secret);
		String getUrl = ENDPOINTS.get(Constants.OAUTH_AUTHORIZATION_URL) + MessageFormat.format(PARAM_URL_STR, appid, sdkToken, sdkUin, signature);
		return result2Json(HttpGet(getUrl), Integer.class, ERROR_CODE);
	}

	@Override
	public String getProviderId() {
		return Constants.XIAOMI;
	}

	@Override
	public Profile verifyResponse(XiaoMiParam param) throws Exception {
		PlatformConfig platformConfig = getPlatformConfig(param.getPid());
		Map<String, Integer> params = checkUserAndroidMiLogin(param.getUid(), param.getToken(), platformConfig.getConfig().getString("appid"), platformConfig.getConfig().getString("secret"));
		if (params.get("errcode") == 200) {
			return new Profile(getProviderId(), param.getUid(), param.getPid());
		} else {
			log.errorf("ProviderId:%s,errCode:%s", getProviderId(), params);
		}
		return null;
	}
}
