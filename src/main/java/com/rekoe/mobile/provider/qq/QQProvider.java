package com.rekoe.mobile.provider.qq;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.repo.Base64;

import com.rekoe.annotation.PlatformProvider;
import com.rekoe.domain.PlatformConfig;
import com.rekoe.mobile.Profile;
import com.rekoe.mobile.provider.AbstractProvider;
import com.rekoe.utils.Constants;

@IocBean
@PlatformProvider(name = Constants.QQ)
public class QQProvider extends AbstractProvider<QQParam> {

	private static final long serialVersionUID = 2882126009485835543L;

	private final static Log log = Logs.get();

	public final static String QQPARAM_KEY = "param";
	public final static String USERINFO_KEY = "userinfo";

	@Override
	public String getProviderId() {
		return Constants.QQ;
	}

	@Override
	public Profile verifyResponse(QQParam p) throws Exception {
		PlatformConfig platformConfig = getPlatformConfig(p.getPid());
		String appkey = platformConfig.getConfig().getString("appkey");
		String appid = platformConfig.getConfig().getString("appid");
		String validateUrl = platformConfig.getConfig().getString("validate_url");
		String uri = paramEncode("/v3/user/get_info");
		String param = "appid=" + appid + "&format=json&openid=" + p.getOpenid() + "&openkey=" + p.getOpenkey() + "&pf=" + p.getPf();
		String get = "GET&" + uri + "&" + paramEncode(param);
		String sig = getSignature(get, appkey);
		String url = validateUrl + "/v3/user/get_info?openid=" + paramEncode(p.getOpenid()) + "&openkey=" + paramEncode(p.getOpenkey()) + "&appid=" + appid + "&sig=" + paramEncode(sig) + "&pf=" + paramEncode(p.getPf()) + "&format=" + paramEncode("json");
		String qqRes = HttpGet(url);
		QQUserInfo userInf = Json.fromJson(QQUserInfo.class, qqRes);
		if (userInf.getRet() == 0) {// ,
			Profile profile = new Profile(getProviderId(), p.getOpenid(), p.getPid());
			profile.addv(USERINFO_KEY, userInf);
			profile.addv(QQPARAM_KEY, p);
			return profile;
		} else {
			log.errorf("ProviderId >> %s,Param >> %s", getProviderId(), Json.toJson(p, JsonFormat.compact()));
			log.error(qqRes);
		}
		return null;
	}

	private String paramEncode(String param) throws UnsupportedEncodingException {
		return URLEncoder.encode(Strings.isBlank(param) ? "" : param, "UTF-8");
	}

	private final String HMAC_SHA1 = "HmacSHA1";

	public String getSignature(String data, String key) throws Exception {
		key += "&";
		data = encodeCompnentURL(data);
		byte[] keyBytes = key.getBytes();
		SecretKeySpec signingKey = new SecretKeySpec(keyBytes, HMAC_SHA1);
		Mac mac = Mac.getInstance(HMAC_SHA1);
		mac.init(signingKey);
		byte[] rawHmac = mac.doFinal(data.getBytes());
		return Base64.encodeToString(rawHmac, false);
	}

	private String encodeCompnentURL(String url) throws UnsupportedEncodingException {
		String temp = url.replace("*", "%2A");
		return temp;
	}
}