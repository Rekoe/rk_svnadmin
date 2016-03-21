package com.rekoe.module.admin;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.mvc.Scope;
import org.nutz.mvc.View;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Attr;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.view.ViewWrapper;
import org.nutz.plugins.view.freemarker.FreeMarkerConfigurer;
import org.nutz.plugins.view.freemarker.FreemarkerView;
import org.nutz.web.Webs;

import com.rekoe.common.Message;
import com.rekoe.common.page.Pagination;
import com.rekoe.domain.GameServer;
import com.rekoe.domain.OfficialServer;
import com.rekoe.domain.User;
import com.rekoe.domain.VerifyServer;
import com.rekoe.filter.ChectServerExitsActionFilter;
import com.rekoe.filter.SelectServerActionFilter;
import com.rekoe.service.GameServerService;
import com.rekoe.service.OfficialServerService;
import com.rekoe.service.VerifyServerService;

@IocBean
@At("/admin/official_server")
public class AdminOfficialAct {

	@Inject
	private VerifyServerService verifyServerService;

	@Inject
	private OfficialServerService officialServerService;

	@Inject
	private FreeMarkerConfigurer freeMarkerConfigurer;

	@Inject
	private GameServerService gameServerService;

	@At
	@Filters(@By(type = SelectServerActionFilter.class, args = { "ioc:selectServerActionFilter" }))
	public View view(@Attr(scope = Scope.SESSION, value = Webs.ME) User user, @Param("pid") int pid) {
		VerifyServer verifyServer = verifyServerService.fetch(Cnd.where("pid", "=", pid));
		GameServer server = gameServerService.fetch(Cnd.where("pid", "=", pid));
		if (Lang.isEmpty(server)) {
			throw Lang.makeThrow("Not Fond PID[%s] Server", pid);
		}
		GameServer userServer = user.getServers().get(server.getId());
		if (Lang.isEmpty(userServer)) {
			throw Lang.makeThrow("You Not Have This Server[%s] Permission ", server.getPlatformName());
		}
		if (Lang.isEmpty(verifyServer)) {
			return new ViewWrapper(new FreemarkerView(freeMarkerConfigurer, "template/admin/game_server/server/verify/add"), server);
		}
		verifyServer.setGameServer(server);
		return new ViewWrapper(new FreemarkerView(freeMarkerConfigurer, "template/admin/game_server/server/verify/edit"), verifyServer);
	}

	@At
	@Ok("json")
	//@Filters(@By(type = SelectServerActionFilter.class, args = { "ioc:selectServerActionFilter" }))
	@RequiresUser
	public Message o_save(@Attr(scope = Scope.SESSION, value = "sid") int sid, @Param("::p.") OfficialServer vserver, HttpServletRequest req) {
		GameServer gameServer = gameServerService.fetch(sid);
		OfficialServer verifyServer = officialServerService.fetch(Cnd.where("pid", "=", gameServer.getPid()).and("sid", "=", vserver.getSid()));
		if (!Lang.isEmpty(verifyServer)) {
			throw Lang.makeThrow("Not Fond PID[%s] Server", vserver.getPid());
		}
		vserver.setPid(gameServer.getPid());
		boolean isRight = officialServerService.insert(vserver);
		if (isRight) {
			return Message.success("button.submit.success", req);
		}
		return Message.error("admin.message.error", req);
	}

	@At
	@Ok("json")
	@Filters(@By(type = SelectServerActionFilter.class, args = { "ioc:selectServerActionFilter" }))
	public Message o_update(@Param("::p.") OfficialServer vserver, HttpServletRequest req) {
		OfficialServer verifyServer = officialServerService.fetch(Cnd.where("pid", "=", vserver.getPid()));
		if (Lang.isEmpty(verifyServer)) {
			throw Lang.makeThrow("Not Fond PID[%s] Server", vserver.getPid());
		}
		officialServerService.update(vserver);
		return Message.success("button.submit.success", req);
	}

	@At
	@Ok("fm:template.admin.server.official.list")
	@Filters(@By(type = ChectServerExitsActionFilter.class, args = { "ioc:chectServerExitsActionFilter" }))
	public Pagination list(@Attr(scope = Scope.SESSION, value = "sid") int sid, @Param("pageNumber") Integer pageNumber) {
		GameServer gameServer = gameServerService.fetch(sid);
		return list_view(gameServer.getPid(), pageNumber);
	}

	@At
	@Ok("fm:template.admin.server.official.list_view")
	@Filters(@By(type = ChectServerExitsActionFilter.class, args = { "ioc:chectServerExitsActionFilter" }))
	public Pagination list_view(@Param("pid") int pid, @Param(value="pageNumber",df="1") Integer pageNumber) {
		return officialServerService.getObjListByPager(pageNumber, Cnd.where("pid", "=", pid).desc("sid"));
	}

	@At
	@Ok("fm:template.admin.server.official.add")
	@RequiresUser
	public void add() {

	}

	@At
	@Ok("fm:template.admin.server.official.edit")
	@RequiresUser
	public OfficialServer edit(@Param("id") String id) {
		return officialServerService.fetch(Cnd.where("id", "=", id));
	}
}
