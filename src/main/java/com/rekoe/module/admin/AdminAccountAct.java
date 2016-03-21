package com.rekoe.module.admin;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.rekoe.annotation.PermissionTag;
import com.rekoe.common.Message;
import com.rekoe.common.page.Pagination;
import com.rekoe.service.PlatformUserService;

@IocBean
@At("/admin/app/account")
public class AdminAccountAct {

	@Inject
	private PlatformUserService platformUserService;

	@At
	@Ok("fm:template.admin.app.account.list")
	@RequiresPermissions({ "app.account:view" })
	@PermissionTag(name = "账号浏览", tag = "平台用户", enable = true)
	public Pagination list(@Param(value = "pageNumber", df = "1") Integer pageNumber, @Param("keyword") String keyword, @Param("type") int type, HttpServletRequest req) {
		req.setAttribute("keyword", keyword);
		req.setAttribute("type", type);
		switch (type) {
		case 1:
			return platformUserService.getPlatformUserListPagerByPid(pageNumber, 20, keyword);
		default:
			return platformUserService.getPlatformUserListPagerByOpenid(pageNumber, 20, NumberUtils.toLong(keyword));
		}
	}

	@At
	@Ok("json")
	@RequiresPermissions({ "app.account:lock" })
	@PermissionTag(name = "账号锁定", tag = "平台用户", enable = true)
	public Message lock(@Param("id") String id, @Param("lock") boolean lock, HttpServletRequest req) {
		platformUserService.lock(id, lock);
		return Message.success("admin.message.success", req);
	}
}
