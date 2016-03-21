package com.rekoe.module.admin;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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
import com.rekoe.domain.OAuthClient;
import com.rekoe.service.OAuthClientService;

@IocBean
@At("/admin/oauth/client")
public class AdminOauthClientAct {

	@Inject
	private OAuthClientService oAuthClientService;

	@At
	@Ok("fm:template.admin.oauth_client.list")
	@RequiresPermissions({ "oauth.client:view" })
	@PermissionTag(name = "浏览", tag = "OauthClient", enable = true)
	public Pagination list(@Param(value = "pageNumber", df = "1") int page) {
		return oAuthClientService.getListPager(page);
	}

	@At
	@Ok("fm:template.admin.oauth_client.add")
	@PermissionTag(name = "添加", tag = "OauthClient", enable = true)
	@RequiresPermissions({ "oauth.client:add" })
	public void add() {
	}

	@At
	@POST
	@Ok("json")
	@RequiresPermissions({ "oauth.client:add" })
	@PermissionTag(name = "浏览", tag = "OauthClient", enable = false)
	public Message o_save(@Param("::client.") OAuthClient client, HttpServletRequest req) {
		if (StringUtils.isBlank(client.getClientName())) {
			return Message.error("error.code.name.empty", req);
		}
		if (oAuthClientService.check(client.getClientName())) {
			oAuthClientService.createClient(client);
			return Message.success("ok", req);
		}
		/** 被占用 **/
		return Message.error("error.code.name.occupied", req);
	}

	@At
	@Ok("fm:template.admin.oauth_client.edit")
	@RequiresPermissions({ "oauth.client:edit" })
	@PermissionTag(name = "编辑", tag = "OauthClient", enable = true)
	public OAuthClient edit(@Param("id") long id) {
		return oAuthClientService.findOne(id);
	}

	@At
	@Ok("json")
	@POST
	@RequiresPermissions({ "oauth.client:edit" })
	@PermissionTag(name = "编辑", tag = "OauthClient", enable = false)
	public Message o_update(@Param("::client.") OAuthClient client, HttpServletRequest req) {
		oAuthClientService.updateClient(client);
		return Message.success("OK", req);
	}

	@At
	@POST
	@Ok("json")
	@RequiresPermissions({ "oauth.client:delete" })
	@PermissionTag(name = "删除", tag = "OauthClient", enable = true)
	public Message delete(long id, HttpServletRequest req) {
		oAuthClientService.deleteClient(id);
		return Message.success("ok", req);
	}

}
