package com.rekoe.module.admin;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.mvc.View;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Attr;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.view.ViewWrapper;
import org.nutz.plugins.view.freemarker.FreeMarkerConfigurer;
import org.nutz.plugins.view.freemarker.FreemarkerView;
import org.nutz.web.Webs;

import com.rekoe.common.Message;
import com.rekoe.domain.GameServer;
import com.rekoe.domain.User;
import com.rekoe.domain.VerifyServer;
import com.rekoe.service.GameServerService;
import com.rekoe.service.VerifyServerService;

/**
 * 提审服务器配置
 * @author kouxian
 *
 */
@IocBean
@At("/admin/verify")
public class AdminVerifyAct {

	@Inject
	private VerifyServerService verifyServerService;

	@Inject
	private FreeMarkerConfigurer freeMarkerConfigurer;

	@Inject
	private GameServerService gameServerService;

	@At
	@RequiresUser
	public View view(@Attr(Webs.ME) User user, @Param("pid") int pid) {
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
			return new ViewWrapper(new FreemarkerView(freeMarkerConfigurer, "template/admin/server/verify/add"), server);
		}
		verifyServer.setGameServer(server);
		return new ViewWrapper(new FreemarkerView(freeMarkerConfigurer, "template/admin/server/verify/edit"), verifyServer);
	}

	@At
	@Ok("json")
	@RequiresUser
	public Message o_save(@Param("::p.") VerifyServer vserver, HttpServletRequest req) {
		VerifyServer verifyServer = verifyServerService.fetch(Cnd.where("pid", "=", vserver.getPid()));
		if (!Lang.isEmpty(verifyServer)) {
			throw Lang.makeThrow("Not Fond PID[%s] Server", vserver.getPid());
		}
		vserver.setPid(vserver.getPid());
		boolean isRight = verifyServerService.insert(vserver);
		if (isRight) {
			return Message.success("button.submit.success", req);
		}
		return Message.error("admin.message.error", req);
	}

	@At
	@Ok("json")
	@RequiresUser
	public Message o_update(@Param("::p.") VerifyServer vserver, HttpServletRequest req) {
		VerifyServer verifyServer = verifyServerService.fetch(Cnd.where("pid", "=", vserver.getPid()));
		if (Lang.isEmpty(verifyServer)) {
			throw Lang.makeThrow("Not Fond PID[%s] Server", vserver.getPid());
		}
		verifyServerService.update(vserver);
		return Message.success("button.submit.success", req);
	}
}
