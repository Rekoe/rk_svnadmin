package com.rekoe.module;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.nutz.castor.Castors;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.Param;

import com.rekoe.domain.GameServer;
import com.rekoe.domain.OAuthUser;
import com.rekoe.domain.PlatformUser;
import com.rekoe.filter.OauthCrossOriginFilter;
import com.rekoe.mobile.provider.PlatformProviderFactory;
import com.rekoe.service.GameServerService;
import com.rekoe.service.IdWorkerService;
import com.rekoe.service.OAuthService;
import com.rekoe.service.OAuthUserService;
import com.rekoe.service.PlatformUserService;
import com.rekoe.utils.CommonUtils;
import com.rekoe.utils.Constants;

@IocBean
@At("/auth")
@Filters(@By(type = OauthCrossOriginFilter.class))
public class OAuthUserAction {

	private final static String PHONE_KEY = "rk_phone_key";
	private final static String PHONE_NUMBER = "rk_phone_number";
	private final static String OAUTH_USER_KEY = "rk_oauth_user_key";

	@Inject
	private OAuthUserService oAuthUserService;

	@Inject
	private OAuthService oAuthService;

	@Inject
	private GameServerService gameServerService;

	/**
	 * @api {post} /auth/user/create 创建账号并获取授权码
	 *
	 * @apiGroup User
	 * @apiVersion 1.0.0
	 * @apiSampleRequest http://warlogin.shanggame.com/auth/user/create
	 * @apiParam {String} name 用户名
	 * @apiParam {String} password 密码
	 * @apiParam {String} captcha 手机收到的验证码
	 * @apiParamExample {String} Request-Example:
	 *                  user.name=xxx&user.password=xxx&captcha=xxx
	 * @apiSuccess {int} feedback 响应状态 0 为成功 1 账号被占用
	 * @apiSuccess {String} code 授权码
	 */

	/**
	 * 普通注册
	 * 
	 * @param user
	 * @return
	 * @throws OAuthSystemException
	 */
	@At("/user/create")
	@Ok("json")
	@POST
	public NutMap create(HttpServletRequest req) throws OAuthSystemException {
		HttpSession session = req.getSession();
		Object phoneCodeObj = session.getAttribute(PHONE_KEY);
		Object phoneObj = session.getAttribute(PHONE_NUMBER);
		if (Lang.isEmpty(phoneCodeObj) || Lang.isEmpty(phoneObj)) {
			return NutMap.NEW().addv("feedback", 404);
		}
		NutMap parames = CommonUtils.getRequestParametersMap(req);
		int captcha = parames.getInt("captcha",0);
		String name = parames.getString("name");
		String pwd = parames.getString("password");
		
		int phoneCode = Castors.me().castTo(phoneCodeObj,int.class);
		if (StringUtils.isBlank(pwd) || StringUtils.isBlank(name) || captcha != phoneCode) {
			return NutMap.NEW().addv("feedback", 1);
		}
		long phone = Castors.me().castTo(phoneObj, long.class);
		NutMap map = NutMap.NEW();
		if (checkOauthUser(name)) {
			OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
			String authorizationCode = oauthIssuerImpl.authorizationCode();
			oAuthService.addAuthCode(authorizationCode, name);
			map.addv("code", authorizationCode);
			OAuthUser user = new OAuthUser();
			user.setPhoneCode(phone);
			user.setPassword(pwd);
			user.setUserName(name);
			oAuthUserService.createUser(user);
			session.setAttribute(PHONE_KEY, null);
			session.setAttribute(PHONE_NUMBER, null);
		} else {
			return map.addv("feedback", 1);
		}
		return map.addv("feedback", 0);
	}

	/**
	 * @api {post} /auth/rest/pwd 密码重置
	 *
	 * @apiGroup User
	 * @apiVersion 1.0.0
	 * @apiSampleRequest http://warlogin.shanggame.com/auth/rest/pwd
	 *
	 * @apiParam {String} captcha 手机收到的验证码
	 * @apiParam {String} password 需要重置的新密码
	 * 
	 * @apiSuccess {int} feedback 响应状态 0 为成功
	 * @apiSuccessExample {json} Success-Response: 
	 * { "feedback": 0 }
	 */

	@At("/rest/pwd")
	@Ok("json")
	@POST
	public NutMap restpwd(HttpServletRequest req) throws OAuthSystemException {
		HttpSession session = req.getSession();
		Object phoneCodeObj = session.getAttribute(PHONE_KEY);
		Object nameObj = session.getAttribute(OAUTH_USER_KEY);
		if (Lang.isEmpty(phoneCodeObj) || Lang.isEmpty(nameObj)) {
			return NutMap.NEW().addv("feedback", 404);
		}
		NutMap parames = CommonUtils.getRequestParametersMap(req);
		int captcha = parames.getInt("captcha",0);
		String pwd = parames.getString("password");
		String name = Castors.me().castToString(nameObj);
		int phoneCode = Castors.me().castTo(phoneCodeObj,int.class);
		if (StringUtils.isBlank(pwd) || captcha!=phoneCode) {
			return NutMap.NEW().addv("feedback", 1);
		}

		OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
		String authorizationCode = oauthIssuerImpl.authorizationCode();
		oAuthService.addAuthCode(authorizationCode, name);
		oAuthUserService.updateByChain(name, pwd);
		session.setAttribute(PHONE_KEY, null);
		session.setAttribute(OAUTH_USER_KEY, null);
		return NutMap.NEW().addv("feedback", 0).addv("code", authorizationCode);
	}

	private boolean checkOauthUser(String name) {
		if (CommonUtils.isUsername(name) && StringUtils.isNotBlank(name) && Lang.isEmpty(oAuthUserService.findByUsername(name))) {
			return true;
		}
		return false;
	}

	/**
	 * @api {post} /auth/phone/captcha 获取手机验证码
	 *
	 * @apiGroup User
	 * @apiVersion 1.0.0
	 * @apiSampleRequest http://warlogin.shanggame.com/auth/phone/captcha
	 *
	 * @apiParam {Number} phone 手机号
	 * @apiParam {Number} pid 引用所属游戏平台编号
	 * 
	 * @apiSuccess {int} feedback 响应状态 0 为成功, 100手机号非法, 101未注册的游戏平台
	 * @apiSuccessExample {json} Success-Response: 
	 * { "feedback": 0 }
	 */

	@At("/phone/captcha")
	@Ok("raw:json")
	@POST
	public String regCaptcha(@Param("phone") String phone, @Param("pid") int pid, HttpSession session) {
		if (!NumberUtils.isNumber(phone) || phone.length() != 11) {
			return "{\"feedback\":100}";
		}
		GameServer gameServer = gameServerService.getByPid(pid);
		if (Lang.isEmpty(gameServer)) {
			return "{\"feedback\":101}";
		}
		int code = CommonUtils.nextInt(1000, 9999);
		boolean isRight = gameServerService.sendPhoneMail(gameServer, phone, "29337", code + "", "30");
		if (isRight) {
			session.setAttribute(PHONE_KEY, code);
			session.setAttribute(PHONE_NUMBER, phone);
			return "{\"feedback\":0,\"session\":\"" + session.getId() + "\"}";
		}
		return "{\"feedback\":102}";
	}

	/**
	 * @api {post} /auth/rest/captcha 获取手机验证码-密码重置
	 *
	 * @apiGroup User
	 * @apiVersion 1.0.0
	 * @apiSampleRequest http://warlogin.shanggame.com/auth/rest/captcha
	 *
	 * @apiParam {String} name 用户名
	 * @apiParam {Number} phone 手机号
	 * @apiParam {Number} pid 引用所属游戏平台编号
	 * 
	 * @apiSuccess {int} feedback 响应状态 0 为成功, 100手机号非法, 101未注册的游戏平台
	 * @apiSuccessExample {json} Success-Response: 
	 * { "feedback": 0 }
	 */

	@At("/rest/captcha")
	@Ok("raw:json")
	@POST
	public String restCaptcha(@Param("phone") String phone, @Param("name") String name, @Param("pid") int pid, HttpSession session) {
		if (!NumberUtils.isNumber(phone) || phone.length() != 11) {
			return "{\"feedback\":100}";
		}
		GameServer gameServer = gameServerService.getByPid(pid);
		if (Lang.isEmpty(gameServer)) {
			return "{\"feedback\":101}";
		}
		boolean isOk = oAuthUserService.checkRestPwd(name, NumberUtils.toLong(phone));
		if (isOk) {
			int code = CommonUtils.nextInt(100000, 999999);
			boolean isRight = gameServerService.sendPhoneMail(gameServer, phone, "29337", code + "", "30");
			if (isRight) {
				session.setAttribute(PHONE_KEY, code);
				session.setAttribute(OAUTH_USER_KEY, name);
				return "{\"feedback\":0,\"session\":\"" + session.getId() + "\"}";
			}
		}
		return "{\"feedback\":102}";
	}

	@Inject
	private PlatformUserService platformUserService;

	@Inject
	private PlatformProviderFactory platformProviderFactory;

	@Inject
	private IdWorkerService idFactory;

	private final Object obj = new Object();

	/**
	 * @api {post} /auth/account/bind 账号绑定
	 *
	 * @apiGroup User
	 * @apiVersion 1.0.0
	 * @apiSampleRequest http://warlogin.shanggame.com/auth/account/bind
	 *
	 * @apiParam {String} name 用户名
	 * @apiParam {String} password 密码
	 * @apiParam {Number} pid 引用所属游戏平台编号
	 * @apiParam {String} captcha 手机收到的验证码
	 * @apiParam {String} guestid 需要绑定的游客ID
	 * 
	 * @apiSuccess {int} feedback 响应状态 0 为成功, 100手机号非法, 101未注册的游戏平台
	 * @apiSuccessExample {json} Success-Response: 
	 * { "feedback": 0 }
	 */

	@At("/account/bind")
	@Ok("json")
	@POST
	public NutMap bindAccount(HttpServletRequest req) {
		HttpSession session = req.getSession();
		Object phoneCodeObj = session.getAttribute(PHONE_KEY);
		Object phoneObj = session.getAttribute(PHONE_NUMBER);
		if (Lang.isEmpty(phoneCodeObj) || Lang.isEmpty(phoneObj)) {
			return NutMap.NEW().addv("feedback", 404);
		}
		NutMap parames = CommonUtils.getRequestParametersMap(req);
		int pid = parames.getInt("pid", -1);
		String pwd = parames.getString("password");
		if (StringUtils.isBlank(pwd)) {
			return NutMap.NEW().addv("feedback", 501).addv("msg", "密码不可以为空");
		}
		int captcha = parames.getInt("captcha", 0);
		int phoneCode = Castors.me().castTo(phoneCodeObj, int.class);
		if (captcha != phoneCode) {
			return NutMap.NEW().addv("feedback", 502).addv("msg", "验证码错误");
		}
		String guestid = parames.getString("guestid");
		if (StringUtils.isBlank(guestid)) {
			return NutMap.NEW().addv("feedback", 503).addv("msg", "游客ID不可以为空");
		}
		PlatformUser platformUser = platformUserService.getPlatformUser(pid, guestid, Constants.GUEST);
		if (Lang.isEmpty(platformUser)) {
			return NutMap.NEW().addv("feedback", 405).addv("msg", "未找到此游客记录");
		}
		long phone = Castors.me().castTo(phoneObj, long.class);
		String name = parames.getString("name");
		if (checkOauthUser(name)) {
			// 创建账号
			long newOpenid = idFactory.nextId();
			synchronized (obj) {
				OAuthUser user = new OAuthUser();
				user.setPhoneCode(phone);
				user.setPassword(pwd);
				user.setUserName(name);
				user.setBind(true);
				user.setBindProviderId(platformUser.getPfid());
				user = oAuthUserService.createUser(user, platformUser.getOpenid());
				platformUser.setOpenid(newOpenid);
				platformUserService.update(Chain.make("openid", newOpenid), Cnd.where("id", "=", platformUser.getId()));
				session.setAttribute(PHONE_KEY, null);
				session.setAttribute(PHONE_NUMBER, null);
			}
		} else {
			return NutMap.NEW().addv("feedback", 504).addv("msg", "账号已被占用");
		}
		return NutMap.NEW().addv("feedback", 0);
	}
}
