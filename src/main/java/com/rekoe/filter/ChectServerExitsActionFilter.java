package com.rekoe.filter;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.nutz.dao.Cnd;
import org.nutz.integration.shiro.NutShiro;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionFilter;
import org.nutz.mvc.View;
import org.nutz.mvc.view.UTF8JsonView;
import org.nutz.mvc.view.ViewWrapper;
import org.nutz.plugins.view.freemarker.FreeMarkerConfigurer;
import org.nutz.plugins.view.freemarker.FreemarkerView;

import com.rekoe.common.Message;
import com.rekoe.domain.GameServer;
import com.rekoe.service.GameServerService;

@IocBean
public class ChectServerExitsActionFilter implements ActionFilter {

	@Inject
	private FreeMarkerConfigurer freeMarkerConfigurer;

	@Inject
	private GameServerService gameServerService;

	public View match(final ActionContext ctx) {
		HttpServletRequest req = ctx.getRequest();
		String pid = req.getParameter("pid");
		if (StringUtils.isNotBlank(pid)) {
			GameServer gameServer = gameServerService.fetch(Cnd.where("pid", "=", pid));
			if (Lang.isEmpty(gameServer)) {
				if (NutShiro.isAjax(ctx.getRequest())) {
					ctx.getResponse().setHeader("loginStatus", getResStatus());
					return new ViewWrapper(UTF8JsonView.COMPACT, getResStatus());
				}
				return getErrView(ctx.getRequest());
			} else {
				req.getSession().setAttribute("sid", gameServer.getId());
			}
		}
		return null;
	}

	public String getResStatus() {
		return "server_not_exits";
	}

	public View getErrView(HttpServletRequest req) {
		return new ViewWrapper(new FreemarkerView(freeMarkerConfigurer, "template/admin/common/error"), Message.error("server_not_exits", req));
	}
}
