package com.rekoe.mobile.provider.hw;

import java.io.IOException;

import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import com.rekoe.annotation.PlatformProvider;
import com.rekoe.mobile.Profile;
import com.rekoe.mobile.provider.AbstractProvider;
import com.rekoe.utils.Constants;

@IocBean
@PlatformProvider(name = Constants.HW)
public class HwProvider extends AbstractProvider<HwParam> {

	private static final long serialVersionUID = 7944880275608182543L;
	private final static Log log = Logs.get();
	// 验证accessToken地址
	private static final String VALID_TOKEN_ADDR = "https://api.vmall.com/rest.php";

	@Override
	public String getProviderId() {
		return Constants.HW;
	}

	@Override
	public Profile verifyResponse(HwParam param) throws Exception {
		UserInfo info = validToken(param);
		if (!Lang.isEmpty(info) && !Strings.isBlank(info.getUserID())) {
			return new Profile(getProviderId(), info.getUserID(), param.getPid());
		} else {
			log.errorf("Can`t find user info on platform %s", getProviderId());
		}

		return null;
	}

	private UserInfo validToken(HwParam param) throws IOException {
		if (Lang.isEmpty(param)) {
			log.errorf("Can`t get %s Param", getProviderId());
			return null;
		}
		String accessToken = param.getAccessToken();
		if (!Strings.isBlank(accessToken)) {
			String postBody = "nsp_svc=OpenUP.User.getInfo&nsp_ts=";
			postBody += String.valueOf(System.currentTimeMillis() / 1000);
			postBody += "&access_token=" + java.net.URLEncoder.encode(accessToken, "utf-8");

			String result = httpsPostBody(VALID_TOKEN_ADDR, postBody);
			return Json.fromJson(UserInfo.class, result);
		} else {
			log.errorf("%s access token is missing %s", getProviderId(), accessToken);
		}
		return null;
	}
}
