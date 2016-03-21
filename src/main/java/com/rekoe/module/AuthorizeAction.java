package com.rekoe.module;

import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.POST;

import com.rekoe.domain.OAuthUser;
import com.rekoe.filter.OauthCrossOriginFilter;
import com.rekoe.utils.CommonUtils;

/**
 * @apiDefine TOKEN_ERROR
 * @apiError 403 The <code>accesstoken</code> is invaild
 */
/**
 * @apiDefine TOKEN
 * @apiParam {String} accesstoken 访问凭证
 *
 */
@At("/api/v1/")
@IocBean
@Filters(@By(type = OauthCrossOriginFilter.class))
public class AuthorizeAction {

	private final static Log log = Logs.get();

	@Inject
	private com.rekoe.service.OAuthService oAuthService;
	@Inject
	private com.rekoe.service.OAuthClientService oAuthClientService;
	@Inject
	private com.rekoe.service.OAuthUserService oAuthUserService;

	/**
	 * @apiDefine CODE_200
	 * @apiSuccess (Reponse 200) {number} code 200
	 * @apiSuccess (Reponse 200) {json} [data='""'] 如果有数据返回
	 * @apiSuccessExample {json} Response 200
	 * { 
	 * 		"code":200, 
	 * 		"feedback":0, 
	 * 		"msg","Ok" 
	 * }
	 */

	/**
	 * @api {post} /api/v1/authorize 请求授权码
	 * @apiSampleRequest http://warlogin.shanggame.com/api/v1/authorize
	 * @apiDescription 用户输入正确的用户名和密码以POST方式提交后会重定向到用户所填的回调地址并在地址后携带授权码.
	 * @apiGroup User
	 * @apiVersion 1.0.0
	 *
	 * @apiParam {String} username 用户名
	 * @apiParam {String} password 密码
	 * @apiParam {String} client_id 应用id
	 * @apiParam {String} response_type=code 返回授权码的标识
	 * 
	 * @apiSuccess (成功返回值) {int} feedback 响应状态 0 为成功
	 * @apiSuccess (成功返回值) {String} msg 错误描述
	 * @apiSuccess (成功返回值) {String} code 授权码
	 * @apiSuccessExample {json} Success-Response: 
	 * { 
	 * 		"code":"c1b0a32d71101d00f4c96a134ec6bd42", 
	 * 		"feedback": 0,
	 *  	"msg": "Ok"
	 *   }
	 */

	@At
	@Ok("json")
	@POST
	public NutMap authorize(HttpServletRequest request, HttpServletResponse res) throws URISyntaxException, OAuthSystemException {
		try {
			// 构建OAuth 授权请求
			OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);
			// 检查传入的客户端id是否正确
			if (!oAuthService.checkClientId(oauthRequest.getClientId())) {
				return NutMap.NEW().addv("feedback", 2);
			}

			// 如果用户没有登录，跳转到登陆页面
			if (!login(request)) {// 登录失败时跳转到登陆页面
				return NutMap.NEW().addv("feedback", 3).addv("msg", "登陆失败");
			}

			String username = request.getParameter("username"); // 获取用户名
			// responseType目前仅支持CODE，另外还有TOKEN
			NutMap map = NutMap.NEW();
			String responseType = oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE);
			if (responseType.equals(ResponseType.CODE.toString())) {
				OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
				String authorizationCode = oauthIssuerImpl.authorizationCode();
				oAuthService.addAuthCode(authorizationCode, username);
				map.addv("code", authorizationCode);
			} else {
				return map.addv("feedback", 4).addv("msg", "参数错误");
			}
			map.addv("feedback", 0).addv("msg", "Ok");
			return map;
		} catch (OAuthProblemException e) {
			log.error(e);
		}
		return null;
	}

	private boolean login(HttpServletRequest request) {
		NutMap parames = CommonUtils.getRequestParametersMap(request);
		String username = parames.getString("username");
		String password = parames.getString("password");
		if (Strings.isEmpty(username) || Strings.isEmpty(password)) {
			return false;
		}
		try {
			OAuthUser user = oAuthUserService.findByUsername(username);
			if (user != null) {
				if (!oAuthUserService.checkUser(username, password, user.getSalt(), user.getPassword())) {
					return false;
				} else {
					return true;
				}
			} else {
				return false;
			}
		} catch (Exception e) {
			log.error(e);
			return false;
		}
	}
}