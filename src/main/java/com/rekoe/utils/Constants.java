package com.rekoe.utils;

public class Constants {

	public final static int HTTPSTATUS_UNAUTHORIZED = 401;
	public final static int HTTPSTATUS_BAD_REQUEST = 400;

	public static final String RESOURCE_SERVER_NAME = "oauth-server";
	public static final String INVALID_CLIENT_ID = "客户端验证失败，如错误的client_id/client_secret。";
	public static final String INVALID_ACCESS_TOKEN = "accessToken无效或已过期。";
	public static final String INVALID_REDIRECT_URI = "缺少授权成功后的回调地址。";
	public static final String INVALID_AUTH_CODE = "错误的授权码。";
	// 验证accessToken
	public static final String CHECK_ACCESS_CODE_URL = "http://localhost/checkAccessToken?accessToken=";
	/**
	 * UTF-8
	 */
	public static final String ENCODING = "UTF-8";

	/**
	 * oauth_consumer_key
	 */
	public static final String OAUTH_CONSUMER_KEY = "oauth_consumer_key";

	/**
	 * authorization url
	 */
	public static final String OAUTH_AUTHORIZATION_URL = "authorizationURL";

	/**
	 * xiaomi
	 */
	public static final String XIAOMI = "xiaomi";

	/**
	 * 360
	 */
	public static final String QIHU360 = "qihu360";

	/**
	 * uc
	 */
	public static final String UC = "uc";

	/**
	 * hw
	 */
	public static final String HW = "hw";

	/**
	 * A91 baidu
	 */
	public static final String A91 = "a91";

	/**
	 * Content Encoding Header
	 */
	public static final String CONTENT_ENCODING_HEADER = "Content-Encoding";

	/**
	 * QQ
	 */
	public static final String QQ = "qq";

	/**
	 * http://wiki.mg.open.qq.com/index.php?title=%E5%90%8D%E8%AF%8D%E8%A7%A3%E9
	 * %87%8A
	 */
	public static final String MSDK = "msdk";

	public static final String GOOGLE_PLAY = "googleP";

	public static final String GOOGLE = "google";

	/**
	 * 自己平台
	 */
	public static final String SG = "sg";

	public static final String RK = "rk";
	/**
	 * 游客登陆
	 */
	public static final String GUEST = "guest";

	/**
	 * Mac
	 */
	public static final String APPLE = "apple";

	public static final String FACEBOOK = "facebook";

	/**
	 * lang 保存在session中得key
	 */
	public static final String SESSION_KEY_LANG = "_session_key_lang_";
	/**
	 * 用户在session中key
	 */
	public static final String SESSION_KEY_USER = "_session_key_user_";

	/**
	 * 
	 */
	public static final String ERROR = "error";

	/**
	 * svn协议
	 */
	public static final String SVN = "svn";
	/**
	 * http单库
	 */
	public static final String HTTP = "http";
	/**
	 * http多库
	 */
	public static final String HTTP_MUTIL = "http-mutil";

	/**
	 * 管理组
	 */
	public static final String GROUP_MANAGER = "manager";

	/**
	 * 项目默认的组
	 */
	public static final String[] GROUPS = { GROUP_MANAGER, "developer", "tester" };

	/**
	 * 管理员角色代码
	 */
	public static final String USR_ROLE_ADMIN = "admin";
}
