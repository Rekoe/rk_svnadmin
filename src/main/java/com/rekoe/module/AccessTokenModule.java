package com.rekoe.module;

import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.Param;

import com.rekoe.filter.OauthCrossOriginFilter;
import com.rekoe.service.OAuthService;
import com.rekoe.utils.Constants;

@IocBean
@At("/oauth")
public class AccessTokenModule {

	@Inject
	private OAuthService oAuthService;

	/**
	 * @api {post} /oauth/accessToken 换取accessToken
	 *
	 * @apiGroup User
	 * @apiVersion 1.0.0
	 * @apiSampleRequest http://warlogin.shanggame.com/oauth/accessToken
	 *
	 * @apiParam {String} client_id 应用id
	 * @apiParam {String} client_secret 应用secret
	 * 
	 * @apiParam {String} grant_type=authorization_code 用于传递授权码的参数名
	 * @apiParam {String} code 用户登录授权后的授权码
	 * @apiParam {String} [redirect_uri=http://www.rekoe.com] 回调地址
	 * 
	 * @apiSuccess {int} expires_in=3600 accessToken过期时间 单位(秒)
	 * @apiSuccess {String} access_token accessToken值
	 *
	 * @apiSuccessExample {json} Success-Response:
	 *    {"expires_in":3600,"access_token":"223ae05dfbb0794396fb60a0960c197e"}
	 */
	
	@At
	@Ok("raw:json")
	@Filters(@By(type = OauthCrossOriginFilter.class))
	public String accessToken(HttpServletRequest request) throws URISyntaxException, OAuthSystemException {
		try {
			// 构建OAuth请求
			OAuthTokenRequest oauthRequest = new OAuthTokenRequest(request);
			// 检查提交的客户端id是否正确
			if (!oAuthService.checkClientId(oauthRequest.getClientId())) {
				OAuthResponse response = OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST).setError(OAuthError.TokenResponse.INVALID_CLIENT).setErrorDescription(Constants.INVALID_CLIENT_ID).buildJSONMessage();
				return response.getBody();
			}
			// 检查客户端安全KEY是否正确
			if (!oAuthService.checkClientSecret(oauthRequest.getClientSecret())) {
				OAuthResponse response = OAuthASResponse.errorResponse(HttpServletResponse.SC_UNAUTHORIZED).setError(OAuthError.TokenResponse.UNAUTHORIZED_CLIENT).setErrorDescription(Constants.INVALID_CLIENT_ID).buildJSONMessage();
				return response.getBody();
			}
			String authCode = oauthRequest.getParam(OAuth.OAUTH_CODE);
			// 检查验证类型，此处只检查AUTHORIZATION_CODE类型，其他的还有PASSWORD或REFRESH_TOKEN
			if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(GrantType.AUTHORIZATION_CODE.toString())) {
				if (!oAuthService.checkAuthCode(authCode)) {
					OAuthResponse response = OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST).setError(OAuthError.TokenResponse.INVALID_GRANT).setErrorDescription(Constants.INVALID_AUTH_CODE).buildJSONMessage();
					return response.getBody();
				}
			}
			// 生成Access Token
			OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
			final String accessToken = oauthIssuerImpl.accessToken();
			oAuthService.addAccessToken(accessToken, oAuthService.getUsernameByAuthCode(authCode));
			// 生成OAuth响应
			OAuthResponse response = OAuthASResponse.tokenResponse(HttpServletResponse.SC_OK).setAccessToken(accessToken).setExpiresIn(String.valueOf(oAuthService.getExpireIn())).buildJSONMessage();
			// 根据OAuthResponse生成ResponseEntity
			return response.getBody();
		} catch (OAuthProblemException e) {
			// 构建错误响应
			OAuthResponse res = OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST).error(e).buildJSONMessage();
			return res.getBody();
		}
	}

	/**
	 * 验证accessToken
	 *
	 * @param accessToken
	 * @return
	 */
	@At
	@POST
	public void checkAccessToken(@Param("accessToken") String accessToken, HttpServletResponse res) {
		boolean b = oAuthService.checkAccessToken(accessToken);
		if (b) {
			res.setStatus(HttpServletResponse.SC_OK);
		} else {
			res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}

}
