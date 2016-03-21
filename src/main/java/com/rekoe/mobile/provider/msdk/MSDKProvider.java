package com.rekoe.mobile.provider.msdk;

import java.util.HashMap;
import java.util.Map;

import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.lang.Lang;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import com.rekoe.annotation.PlatformProvider;
import com.rekoe.domain.PlatformConfig;
import com.rekoe.mobile.Profile;
import com.rekoe.mobile.provider.AbstractProvider;
import com.rekoe.utils.Constants;

/**
 * 
 * @author kouxian
 *
 */
@IocBean
@PlatformProvider(name = Constants.MSDK)
public class MSDKProvider extends AbstractProvider<MSDKParam> {

	private static final long serialVersionUID = 2882126009485835543L;

	private final static Log log = Logs.get();

	public final static String MSDK_PROFILE = "MSDK_PROFILE";
	public final static String MSDK_PARAM = "MSDK_PARAM";

	@Override
	public String getProviderId() {
		return Constants.MSDK;
	}

	@Override
	public Profile verifyResponse(MSDKParam p) throws Exception {
		PlatformConfig platformConfig = getPlatformConfig(p.getPid());
		NutMap config = platformConfig.getConfig();
		String appkey = config.getString("appkey");
		long appid = config.getAs("appid", Long.class);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("appid", appid);
		params.put("openid", p.getOpenid());
		params.put("openkey", appkey);
		params.put("userip", p.getIp());
		String uri = makeUri(p.getOpenid(), appid, appkey);
		String url = config.getString("valid_token_url") + uri;
		String data = Json.toJson(params, JsonFormat.compact());
		if (log.isDebugEnabled()) {
			log.debugf("url >> %s", url);
		}
		String res = httpPostBody(url, data);
		if (log.isDebugEnabled()) {
			log.debugf("back >> %s", res);
		}
		try {
			MSDKUserInfo userInfo = Json.fromJson(MSDKUserInfo.class, res);
			if (userInfo.getRet() == 0) {
				Profile profile = new Profile(getProviderId(), p.getOpenid(), p.getPid());
				profile.addv(MSDK_PROFILE, p);
				profile.addv(MSDK_PARAM, userInfo);
				profile.addv(Profile.TOKEN, p.getOpenid());
				return profile;
			} else {
				log.error(res);
			}
		} catch (Exception e) {
			log.error(e);
		}
		return null;
	}

	private String makeUri(String openid, long appid, String appkey) {
		long timestamp = System.currentTimeMillis();
		String sig = Lang.md5(appkey + timestamp);
		return new StringBuilder().append("/?timestamp=").append(timestamp).append("&appid=").append(appid).append("&sig=").append(sig).append("&openid=").append(openid).append("&encode=1").toString();
	}
}