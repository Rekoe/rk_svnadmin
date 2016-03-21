package com.rekoe.mobile.provider.googlep;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.nutz.http.Request;
import org.nutz.http.Request.METHOD;
import org.nutz.http.Sender;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.lang.Lang;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Tokeninfo;
import com.rekoe.annotation.PlatformProvider;
import com.rekoe.domain.PlatformConfig;
import com.rekoe.mobile.Profile;
import com.rekoe.mobile.provider.AbstractProvider;
import com.rekoe.utils.Constants;

@IocBean
@PlatformProvider(name = Constants.GOOGLE_PLAY)
public class GooglePProvider extends AbstractProvider<GooglePParam> {

	private static final long serialVersionUID = 7944880275608182543L;
	private final static Log log = Logs.get();

	private final HttpTransport TRANSPORT = new NetHttpTransport();
	private final JacksonFactory JSON_FACTORY = new JacksonFactory();

	@Override
	public String getProviderId() {
		return Constants.GOOGLE_PLAY;
	}

	/**
	 * https://developers.google.com/identity/sign-in/android/offline-access
	 */
	@Override
	public Profile verifyResponse(GooglePParam param) throws Exception {
		TokenStatus info = validToken(param);
		if (info.isValid()) {
			Profile profile = new Profile(getProviderId(), info.getGplus_id(), param.getPid());
			profile.addv(Profile.TOKEN, param.getToken());
			return profile;
		}
		log.errorf("Err message %s", info.getMessage());
		return null;
	}

	private TokenStatus validToken(GooglePParam param) throws IOException {
		PlatformConfig platformConfig = getPlatformConfig(param.getPid());
		String client_id = platformConfig.getConfig().getString("client_id");
		String applicationName = platformConfig.getConfig().getString("application_name", "zsdpay-1203");
		String accessToken = param.getToken();
		TokenStatus accessStatus = new TokenStatus();
		if (StringUtils.isNotBlank(accessToken)) {
			try {
				GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
				Oauth2 oauth2 = new Oauth2.Builder(TRANSPORT, JSON_FACTORY, credential).setApplicationName(applicationName).build();
				Tokeninfo tokenInfo = oauth2.tokeninfo().setAccessToken(accessToken).execute();
				if (tokenInfo.containsKey("error")) {
					accessStatus.setValid(false);
					accessStatus.setId("");
					accessStatus.setMessage("Invalid Access Token.");
				} else if (!Lang.equals(client_id, tokenInfo.getIssuedTo())) {
					accessStatus.setValid(false);
					accessStatus.setId("");
					accessStatus.setMessage("Access Token not meant for this app.");
				} else {
					accessStatus.setValid(true);
					accessStatus.setId(tokenInfo.getUserId());
					accessStatus.setMessage("Access Token is valid.");
				}
			} catch (IOException e) {
				accessStatus.setValid(false);
				accessStatus.setId("");
				accessStatus.setMessage("Invalid Access Token.");
			}
		} else {
			accessStatus.setMessage("Access Token not provided");
		}
		return accessStatus;
	}

	public static void main(String[] args) {
		HttpTransport TRANSPORT = new NetHttpTransport();
		JacksonFactory JSON_FACTORY = new JacksonFactory();
		String applicationName = "zsdpay-1203";
		String token = "ya29.fgLVUDBWXuM91LAtvMdBXXWhCmKctjb44SK7Th8BO1yQ3DLfe3n_85Xi7PeLtQFsGEGOpQ";
		String client_id = "232182541248-8guj6heku6vpgdb7a3shmco33ns69lpm.apps.googleusercontent.com";
		String accessToken = token;
		TokenStatus accessStatus = new TokenStatus();
		if (StringUtils.isNotBlank(accessToken)) {
			try {
				GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
				Oauth2 oauth2 = new Oauth2.Builder(TRANSPORT, JSON_FACTORY, credential).setApplicationName(applicationName).build();
				Tokeninfo tokenInfo = oauth2.tokeninfo().setAccessToken(accessToken).execute();
				if (tokenInfo.containsKey("error")) {
					accessStatus.setValid(false);
					accessStatus.setId("");
					accessStatus.setMessage("Invalid Access Token.");
				} else if (!Lang.equals(client_id, tokenInfo.getIssuedTo())) {
					accessStatus.setValid(false);
					accessStatus.setId("");
					accessStatus.setMessage("Access Token not meant for this app.");
				} else {
					accessStatus.setValid(true);
					accessStatus.setId(tokenInfo.getUserId());
					accessStatus.setMessage("Access Token is valid.");
				}
			} catch (IOException e) {
				accessStatus.setValid(false);
				accessStatus.setId("");
				accessStatus.setMessage("Invalid Access Token.");
			}
		} else {
			accessStatus.setMessage("Access Token not provided");
		}
		System.out.println(Json.toJson(accessStatus));
	}

	public static void main1(String[] args) {
		Request req = Request.create("http://127.0.0.1:8080/api/list?pfid=1006200110011004", METHOD.POST);
		req.getHeader().set("Content-Type", "application/octet-stream;charset=UTF-8");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pid", 1001);
		params.put("token", "ya29.dgJTpUrOQ9g1RHfXKXimjWHNSTKW5OPpb9--W51L_J5_u8anU_je2gAFFa4br_0NsRJY5g");
		req.setData(Json.toJson(params, JsonFormat.compact()));
		Sender.create(req).setTimeout(6000).send();
	}
}
