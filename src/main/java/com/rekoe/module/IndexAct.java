package com.rekoe.module;

import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;

@IocBean
@At("/user")
public class IndexAct {

	@At
	@Ok("fm:template.login.login")
	public void login() {
		/*
		 * try { SecurityUtils.getSubject().logout(); } catch (SessionException
		 * ise) { logger.debug(
		 * "Encountered session exception during logout.  This can generally safely be ignored."
		 * , ise); } catch (Exception e) { logger.debug("登出发生错误", e); }
		 */
	}

	// /user/login
}
