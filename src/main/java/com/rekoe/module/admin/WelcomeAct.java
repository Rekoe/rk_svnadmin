package com.rekoe.module.admin;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.nutz.castor.Castors;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;

import com.rekoe.domain.GameServer;
import com.rekoe.domain.User;
import com.rekoe.module.BaseAction;

@IocBean
public class WelcomeAct extends BaseAction {

	@At("/admin/index_main")
	@Ok("fm:template.admin.main")
	public void index() {
	}

	@At("/admin/top")
	@Ok("fm:template.admin.top")
	@RequiresUser
	public boolean top(HttpServletRequest req) {
		Object obj = SecurityUtils.getSubject().getPrincipal();
		if (obj instanceof User) {
			User user = Castors.me().castTo(obj, User.class);
			Collection<GameServer> servers = Lang.isEmpty(user.getServers()) ? (new ArrayList<GameServer>()) : ((User) obj).getServers().values();
			req.setAttribute("servers", servers);
			return Lang.equals("local", user.getProviderid());
		} else {
			req.setAttribute("servers", new ArrayList<GameServer>());
		}
		return true;
	}

	@At("/admin/main")
	@Ok("fm:template.admin.index")
	public void main() {
	}

	@At("/admin/left")
	@Ok("fm:template.admin.left")
	public void left() {
	}

	@At("/admin/right")
	@Ok("fm:template.admin.right")
	public void right() {
	}
}
