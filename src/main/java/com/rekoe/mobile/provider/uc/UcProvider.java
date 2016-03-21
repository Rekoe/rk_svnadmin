package com.rekoe.mobile.provider.uc;

import java.util.HashMap;
import java.util.Map;

import org.nutz.http.Request;
import org.nutz.http.Request.METHOD;
import org.nutz.http.Response;
import org.nutz.http.Sender;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.lang.Lang;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import com.rekoe.annotation.PlatformProvider;
import com.rekoe.domain.PlatformConfig;
import com.rekoe.mobile.Profile;
import com.rekoe.mobile.provider.AbstractProvider;
import com.rekoe.utils.Constants;

@IocBean
@PlatformProvider(name = Constants.UC)
public class UcProvider extends AbstractProvider<UcParam> {

	private static final long serialVersionUID = 2882126009485835543L;
	private final static Log log = Logs.get();

	@Override
	public String getProviderId() {
		return Constants.UC;
	}

	@Override
	public Profile verifyResponse(UcParam param) throws Exception {
		PlatformConfig platformConfig = getPlatformConfig(param.getPid());
		String appkey = platformConfig.getConfig().getString("appkey");
		int appid = platformConfig.getConfig().getInt("appid");
		int secret = platformConfig.getConfig().getInt("secret");
		String validateUrl = platformConfig.getConfig().getString("validate_url");
		UCResponse response = response2(param.getSid(), appid, appkey, secret, validateUrl);
		if (response.getState().getCode() == 1) {
			Profile profile = new Profile(getProviderId(), response.getData().getAccountId(), param.getPid());
			profile.addv("token", param.getSid());
			return profile;
		}
		return null;
	}

	@Deprecated
	public UCResponse response(String sid, int appid, String appKey, String validateUrl) {
		Map<String, Object> params = new HashMap<>();
		params.put("id", System.currentTimeMillis());
		params.put("service", "ucid.bind.create");
		params.put("sign", Lang.md5("gameUser=" + sid + appKey));
		Map<String, Integer> gameParams = new HashMap<>();
		gameParams.put("gameId", appid);
		params.put("game", gameParams);
		Map<String, String> dataParams = new HashMap<>();
		params.put("data", dataParams);
		dataParams.put("gameUser", sid);
		String json = httpPostBody(validateUrl, Json.toJson(params, JsonFormat.compact()));
		Map<String, Object> m = Json.fromJsonAsMap(Object.class, json);
		Object obj = m.get("state");
		UCResponseState state = Lang.map2Object((Map<?, ?>) obj, UCResponseState.class);
		UCResponse res = new UCResponse(state);
		if (state.getCode() == 1) {
			res.setData(Lang.map2Object((Map<?, ?>) m.get("data"), UCResponseData.class));
		} else {
			log.error(json);
		}
		return res;
	}

	private UCResponse response2(String sid, int appid, String appKey, int secret, String validateUrl) {
		Map<String, Object> params = new HashMap<>();
		params.put("id", System.currentTimeMillis());
		params.put("service", "ucid.user.sidInfo");
		params.put("encrypt", "md5");
		params.put("sign", Lang.md5(secret + "sid=" + sid + appKey));
		Map<String, Integer> gameParams = new HashMap<>();
		gameParams.put("cpId", secret);
		gameParams.put("gameId", appid);
		gameParams.put("channelId", 2);
		gameParams.put("serverId", 0);
		params.put("game", gameParams);
		Map<String, String> dataParams = new HashMap<>();
		dataParams.put("sid", sid);
		params.put("data", dataParams);
		Request req = Request.create(validateUrl, METHOD.POST);
		req.getHeader().set("Content-Type", "application/json:charset=utf-8 ");
		req.setData(Json.toJson(params, JsonFormat.compact()));
		Response resp = Sender.create(req).send();
		String json = resp.getContent();
		Map<String, Object> m = Json.fromJsonAsMap(Object.class, json);
		Object obj = m.get("state");
		UCResponseState state = Lang.map2Object((Map<?, ?>) obj, UCResponseState.class);
		UCResponse res = new UCResponse(state);
		if (state.getCode() == 1) {
			res.setData(Lang.map2Object((Map<?, ?>) m.get("data"), UCResponseData.class));
		} else {
			log.error(json);
		}
		return res;
	}

}
