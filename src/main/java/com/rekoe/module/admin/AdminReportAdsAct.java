package com.rekoe.module.admin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Attr;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.web.Webs;

import com.rekoe.annotation.PermissionTag;
import com.rekoe.domain.GameServer;
import com.rekoe.domain.User;
import com.rekoe.module.BaseAction;
import com.rekoe.service.GameServerService;
import com.rekoe.service.ReportAdsService;

@IocBean
@At("/admin/report/ads")
public class AdminReportAdsAct extends BaseAction {

	@Inject
	private ReportAdsService reportAdsService;

	@Inject
	private GameServerService gameServerService;

	@At
	@Ok("fm:template.admin.report.ads.search")
	@RequiresPermissions({ "report:ads" })
	@PermissionTag(name = "设备号汇报搜索", tag = "数据统计")
	public Collection<GameServer> search(@Attr(Webs.ME) User user) {
		return user.getServers().values();
	}

	@At
	@Ok("down")
	@RequiresPermissions({ "report:ads.down" })
	@PermissionTag(name = "设备号数据下载", tag = "数据统计")
	public ByteArrayOutputStream down(@Param("pid") int pid) throws IOException {
		return loadDown(reportAdsService.getList(pid));
	}
}
