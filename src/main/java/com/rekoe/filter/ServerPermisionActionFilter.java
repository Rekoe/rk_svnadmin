package com.rekoe.filter;

import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
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
import com.rekoe.domain.User;

@IocBean
public class ServerPermisionActionFilter implements ActionFilter {

	@Inject
	private FreeMarkerConfigurer freeMarkerConfigurer;

	public View match(final ActionContext ctx) {
		HttpServletRequest req = ctx.getRequest();
		Object sid = req.getParameter("platformid");
		if (Lang.isEmpty(sid)) {
			return getErrView(req, ctx.getResponse());
		}
		boolean isRight = false;
		Subject subject = SecurityUtils.getSubject();
		if (subject.getPrincipal() instanceof User) {
			User user = (User) subject.getPrincipal();
			Map<Integer, GameServer> servers = user.getServers();
			if (user.isSystem()) {
				isRight = true;
			} else if (!Lang.isEmpty(servers)) {
				int pid = NumberUtils.toInt(sid.toString());
				Collection<GameServer> games = servers.values();
				for (GameServer game : games) {
					if (game.getPid() == pid) {
						isRight = true;
						break;
					}
				}
			}
		}
		if (isRight) {
			return null;
		}
		return getErrView(req, ctx.getResponse());
	}

	public String getResStatus() {
		return "unauthorized";
	}

	public View getErrView(HttpServletRequest req, HttpServletResponse resp) {
		if (NutShiro.isAjax(req)) {
			resp.setHeader("loginStatus", getResStatus());
			return new ViewWrapper(UTF8JsonView.COMPACT, getResStatus());
		}
		return new ViewWrapper(new FreemarkerView(freeMarkerConfigurer, "template/admin/common/error"), Message.error(getResStatus(), req));
	}
}
