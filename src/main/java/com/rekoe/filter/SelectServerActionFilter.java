package com.rekoe.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

@IocBean
public class SelectServerActionFilter implements ActionFilter {

	@Inject
	private FreeMarkerConfigurer freeMarkerConfigurer;

	public View match(final ActionContext ctx) {
		HttpSession session = ctx.getRequest().getSession();
		Object sid = session.getAttribute("sid");
		if (Lang.isEmpty(sid)) {
			if (NutShiro.isAjax(ctx.getRequest())) {
				ctx.getResponse().setHeader("loginStatus", getResStatus());
				return new ViewWrapper(UTF8JsonView.COMPACT, getResStatus());
			}
			return getErrView(ctx.getRequest());
		}
		return null;
	}

	public String getResStatus() {
		return "select_server";
	}

	public View getErrView(HttpServletRequest req) {
		return new ViewWrapper(new FreemarkerView(freeMarkerConfigurer, "template/admin/common/error"), Message.error("admin.common.error.no.server", req));
	}
}
