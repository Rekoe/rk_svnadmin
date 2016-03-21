package com.rekoe.module.admin;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.Param;

import com.rekoe.annotation.PermissionTag;
import com.rekoe.common.Message;
import com.rekoe.common.page.Pagination;
import com.rekoe.domain.OAuthUser;
import com.rekoe.service.OAuthUserService;

@IocBean
@At("/admin/oauth/user")
public class AdminOauthUserAct {

	@Inject
	private OAuthUserService oAuthUserService;

	@At
	@Ok("fm:template.admin.oauth_user.list")
	@RequiresPermissions({ "oauth.user:view" })
	@PermissionTag(name = "浏览", tag = "OauthUser", enable = true)
	public Pagination list(@Param(value = "pageNumber", df = "1") int page) {
		return oAuthUserService.getListPager(page);
	}

	@At
	@Ok("fm:template.admin.oauth_user.edit")
	@RequiresPermissions({ "oauth.user:edit" })
	@PermissionTag(name = "浏览", tag = "OauthUser", enable = true)
	public OAuthUser edit(@Param("id") long id) {
		return oAuthUserService.findOne(id);
	}

	@At
	@Ok("json")
	@POST
	@RequiresPermissions({ "oauth.user:edit" })
	@PermissionTag(name = "浏览", tag = "OauthUser", enable = false)
	public Message o_update(@Param("::user.") OAuthUser user, HttpServletRequest req) {
		//oAuthUserService.updateUser(user);
		return Message.success("OK", req);
	}

}
