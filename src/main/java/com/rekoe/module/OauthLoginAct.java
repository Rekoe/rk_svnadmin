package com.rekoe.module;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.brickred.socialauth.SocialAuthConfig;
import org.brickred.socialauth.SocialAuthManager;
import org.nutz.boot.starter.freemarker.FreeMarkerConfigurer;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Encoding;
import org.nutz.lang.Files;
import org.nutz.lang.stream.NullInputStream;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;

/**
 * @author 科技㊣²º¹³ <br />
 *         2014年2月3日 下午4:48:45<br />
 *         http://www.rekoe.com QQ:5382211
 */
@IocBean(create = "init")
@At("/user")
public class OauthLoginAct {

	@Inject
	private FreeMarkerConfigurer freeMarkerConfigurer;

	// 需要登录之后才能访问,否则跳转到首页
	@RequiresAuthentication
	@At
	public Object authOnly() {
		return "You are authed!";
	}

	/* 提供社会化登录 */
	@At("/login/?")
	@Ok("void")
	public void login(String provider, HttpSession session, HttpServletRequest req, HttpServletResponse res) throws Exception {
		String returnTo = req.getRequestURL().toString() + "/callback";
		if (req.getParameterMap().size() > 0) {
			StringBuilder sb = new StringBuilder().append(returnTo).append("?");
			for (Object name : req.getParameterMap().keySet()) {
				sb.append(name).append('=').append(URLEncoder.encode(req.getParameter(name.toString()), Encoding.UTF8)).append("&");
			}
			returnTo = sb.toString();
		}
		SocialAuthManager manager = new SocialAuthManager(); // 每次都要新建哦
		manager.setSocialAuthConfig(config);
		session.setAttribute("openid.manager", manager);
		String url = manager.getAuthenticationUrl(provider, returnTo);
		res.setHeader("Location", url);
		res.setStatus(302);
	}

	// 没登录就不要登出了
	@RequiresAuthentication
	@At("/logout")
	@Ok(">>:/admin/index.rk")
	public void logout(HttpSession session) {
		// session.invalidate(); //销毁会话,啥都米有了
		SecurityUtils.getSubject().logout();
	}

	private SocialAuthConfig config;

	public void init() throws Exception {
		SocialAuthConfig config = new SocialAuthConfig();
		File devConfig = Files.findFile("oauth_consumer.properties_dev"); // 开发期所使用的配置文件
		if (devConfig == null)
			devConfig = Files.findFile("oauth_consumer.properties"); // 真实环境所使用的配置文件
		if (devConfig == null)
			config.load(new NullInputStream());
		else
			config.load(new FileInputStream(devConfig));
		this.config = config;
	}

}
