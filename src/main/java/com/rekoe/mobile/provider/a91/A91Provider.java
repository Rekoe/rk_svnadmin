package com.rekoe.mobile.provider.a91;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.repo.Base64;

import com.rekoe.annotation.PlatformProvider;
import com.rekoe.domain.PlatformConfig;
import com.rekoe.mobile.Profile;
import com.rekoe.mobile.provider.AbstractProvider;
import com.rekoe.utils.Constants;

@IocBean
@PlatformProvider(name = Constants.A91)
public class A91Provider extends AbstractProvider<A91Param> {

	private static final long serialVersionUID = 1L;
	private final static Log log = Logs.get();
	private final String VALID_TOKEN_ADDR = "http://querysdkapi.91.com/CpLoginStateQuery.ashx";// 接口地址

	@Override
	public String getProviderId() {
		return Constants.A91;
	}

	@Override
	public Profile verifyResponse(A91Param param) throws Exception {
		PlatformConfig platformConfig = getPlatformConfig(param.getPid());
		String str = platformConfig.getConfig().getString("appid") + param.getSessionid() + platformConfig.getConfig().getString("secret");// 签名
		String sign = Lang.md5(str);
		Map<String, Object> params = new HashMap<>();
		params.put("AppID", platformConfig.getConfig().getString("appid"));
		params.put("AccessToken", param.getSessionid());
		params.put("Sign", sign.toLowerCase());
		String result = httpPost(VALID_TOKEN_ADDR, params);
		A91ReturnParam ret = Json.fromJson(A91ReturnParam.class, result);
		if (ret.getResultCode() == 1) {
			String con = URLDecoder.decode(ret.getContent(), "utf-8");
			String temp = platformConfig.getConfig().getString("appid") + ret.getResultCode() + con + platformConfig.getConfig().getString("secret");
			String sign1 = Lang.md5(temp);
			if (sign1.equalsIgnoreCase(ret.getSign())) {
				A91UserInfo info = Json.fromJson(A91UserInfo.class, new String(Base64.decode(con)));
				return new Profile(getProviderId(), info.getUID(), param.getPid());
			}
		} else {
			log.error(result);
		}
		return null;
	}

}
