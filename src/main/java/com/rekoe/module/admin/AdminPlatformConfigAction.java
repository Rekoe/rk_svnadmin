package com.rekoe.module.admin;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.rekoe.annotation.PermissionTag;
import com.rekoe.common.AppleVO;
import com.rekoe.common.GooglePVO;
import com.rekoe.common.GoogleVO;
import com.rekoe.common.Message;
import com.rekoe.common.page.Pagination;
import com.rekoe.domain.PlatformConfig;
import com.rekoe.filter.ServerPermisionActionFilter;
import com.rekoe.service.PlatformConfigService;

@IocBean
@At("/admin/platform_config")
@Filters(@By(type = ServerPermisionActionFilter.class, args = { "ioc:serverPermisionActionFilter" }))
public class AdminPlatformConfigAction {

	@Inject
	private PlatformConfigService platformConfigService;

	@At
	@Ok("fm:template.admin.server.platform.list")
	@RequiresPermissions("admin.platform")
	@PermissionTag(name = "浏览列表", tag = "平台接入", enable = false)
	public Pagination list(@Param("platformid") int sid, @Param(value = "pageNumber", df = "1") int pageNumber) {
		Mvcs.getReq().setAttribute("platformid", sid);
		return platformConfigService.getListByPager(pageNumber, sid);
	}

	@At
	@Ok("fm:template.admin.server.platform.add")
	@RequiresPermissions("admin.platform:add")
	@PermissionTag(name = "添加平台", tag = "平台接入", enable = true)
	public int add(@Param("platformid") int sid) {
		return sid;
	}

	@At
	@Ok("fm:template.admin.server.platform.edit")
	@RequiresPermissions("admin.platform:edit")
	@PermissionTag(name = "编辑平台", tag = "平台接入")
	public PlatformConfig edit(@Param("platformid") int sid, @Param("id") String id) {
		return platformConfigService.fetch(Cnd.where("id", "=", id));
	}

	@At
	@Ok("fm:template.admin.server.platform.${obj}.add_${obj}")
	@RequiresPermissions("admin.platform")
	@PermissionTag(name = "浏览列表", tag = "平台接入", enable = false)
	public String add_view(@Param("platformid") int sid, @Param(value = "provider", df = "uc") String provider) {
		Mvcs.getReq().setAttribute("platformid", sid);
		return provider;
	}

	@At
	@Ok("json")
	@RequiresPermissions("admin.platform:add")
	@PermissionTag(name = "添加平台", tag = "平台接入", enable = false)
	public Message o_save(@Param("platformid") int sid, @Param("..") NutMap map, HttpServletRequest req) {
		PlatformConfig platformConfig = platformConfigService.fetch(Cnd.where("sid", "=", sid).and("provider", "=", map.getString("provider")));
		if (Lang.isEmpty(platformConfig)) {
			platformConfigService.insert(new PlatformConfig(sid, map));
		} else {
			return Message.error("duplate.data", req);
		}
		return Message.success("ok", req);
	}

	@At
	@Ok("json")
	@RequiresPermissions("admin.platform:edit")
	@PermissionTag(name = "编辑平台", tag = "平台接入", enable = false)
	public Message o_update(@Param("platformid") int sid, @Param("id") String id, @Param("..") NutMap map, HttpServletRequest req) {
		map.remove("platformid");
		map.remove("id");
		PlatformConfig platformConfig = platformConfigService.fetch(Cnd.where("id", "=", id));
		platformConfig.setConfig(map);
		platformConfigService.update(platformConfig);
		return Message.success("ok", req);
	}

	@At("/googleP/o_update")
	@Ok("json")
	@RequiresPermissions("admin.platform:edit")
	@PermissionTag(name = "编辑平台", tag = "平台接入", enable = false)
	public Message google_play_update(@Param("platformid") int sid, @Param("id") String id, @Param("::p.") GooglePVO googlePVO, HttpServletRequest req) {
		PlatformConfig platformConfig = platformConfigService.fetch(Cnd.where("id", "=", id));
		platformConfig.setConfig(Lang.obj2nutmap(googlePVO));
		platformConfigService.update(platformConfig);
		return Message.success("ok", req);
	}

	@At("/googleP/o_save")
	@Ok("json")
	@RequiresPermissions("admin.platform:add")
	@PermissionTag(name = "添加平台", tag = "平台接入", enable = false)
	public Message google_play_save(@Param("platformid") int sid, @Param("provider") String provider, @Param("::p.") GooglePVO googlePVO, HttpServletRequest req) {
		PlatformConfig platformConfig = platformConfigService.fetch(Cnd.where("sid", "=", sid).and("provider", "=", provider));
		if (Lang.isEmpty(platformConfig)) {
			NutMap map = Lang.obj2nutmap(googlePVO);
			map.put("provider", provider);
			platformConfigService.insert(new PlatformConfig(sid, map));
		} else {
			return Message.error("duplate.data", req);
		}
		return Message.success("ok", req);
	}

	/**
	 * google
	 */

	@At("/google/o_update")
	@Ok("json")
	@RequiresPermissions("admin.platform:edit")
	@PermissionTag(name = "编辑平台", tag = "平台接入", enable = false)
	public Message google_update(@Param("platformid") int sid, @Param("id") String id, @Param("::p.") GoogleVO googleVO, HttpServletRequest req) {
		PlatformConfig platformConfig = platformConfigService.fetch(Cnd.where("id", "=", id));
		platformConfig.setConfig(Lang.obj2nutmap(googleVO));
		platformConfigService.update(platformConfig);
		return Message.success("ok", req);
	}

	@At("/google/o_save")
	@Ok("json")
	@RequiresPermissions("admin.platform:add")
	@PermissionTag(name = "添加平台", tag = "平台接入", enable = false)
	public Message google_save(@Param("platformid") int sid, @Param("provider") String provider, @Param("::p.") GoogleVO googleVO, HttpServletRequest req) {
		PlatformConfig platformConfig = platformConfigService.fetch(Cnd.where("sid", "=", sid).and("provider", "=", provider));
		if (Lang.isEmpty(platformConfig)) {
			NutMap map = Lang.obj2nutmap(googleVO);
			map.put("provider", provider);
			platformConfigService.insert(new PlatformConfig(sid, map));
		} else {
			return Message.error("duplate.data", req);
		}
		return Message.success("ok", req);
	}

	/**
	 * apple
	 */

	@At("/apple/o_update")
	@Ok("json")
	@RequiresPermissions("admin.platform:edit")
	@PermissionTag(name = "编辑平台", tag = "平台接入", enable = false)
	public Message apple_update(@Param("platformid") int sid, @Param("id") String id, @Param("::p.") AppleVO appleVO, HttpServletRequest req) {
		PlatformConfig platformConfig = platformConfigService.fetch(Cnd.where("id", "=", id));
		platformConfig.setConfig(Lang.obj2nutmap(appleVO));
		platformConfigService.update(platformConfig);
		return Message.success("ok", req);
	}

	@At("/apple/o_save")
	@Ok("json")
	@RequiresPermissions("admin.platform:add")
	@PermissionTag(name = "添加平台", tag = "平台接入", enable = false)
	public Message apple_save(@Param("platformid") int sid, @Param("provider") String provider, @Param("::p.") AppleVO appleVO, HttpServletRequest req) {
		PlatformConfig platformConfig = platformConfigService.fetch(Cnd.where("sid", "=", sid).and("provider", "=", provider));
		if (Lang.isEmpty(platformConfig)) {
			NutMap map = Lang.obj2nutmap(appleVO);
			map.put("provider", provider);
			platformConfigService.insert(new PlatformConfig(sid, map));
		} else {
			return Message.error("duplate.data", req);
		}
		return Message.success("ok", req);
	}
}
