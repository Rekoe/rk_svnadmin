package com.rekoe.module.admin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Attr;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.web.Webs;

import com.rekoe.annotation.PermissionTag;
import com.rekoe.common.Message;
import com.rekoe.common.PlatformVO;
import com.rekoe.common.page.Pagination;
import com.rekoe.domain.AuthType;
import com.rekoe.domain.GameServer;
import com.rekoe.domain.User;
import com.rekoe.service.GameServerService;
import com.rekoe.service.UserService;

@IocBean(create = "init")
@At("/admin/server")
public class AdminGameServerAct {

	@Inject
	private GameServerService gameServerService;

	@Inject
	private UserService userService;

	private List<PlatformVO> authTypeList = new ArrayList<>();

	public void init() {
		authTypeList.add(new PlatformVO(AuthType.TOKEN.getDisplay(), AuthType.TOKEN.getDisplay()));
		authTypeList.add(new PlatformVO(AuthType.DEFAULT.getDisplay(), AuthType.DEFAULT.getDisplay()));
		authTypeList.add(new PlatformVO(AuthType.QQ_HTML5.getDisplay(), AuthType.QQ_HTML5.getDisplay()));
	}

	@At
	@Ok("fm:template.admin.server.list")
	@RequiresPermissions("server:view")
	@PermissionTag(name = "应用浏览", tag = "游戏应用", enable = true)
	public Pagination list(@Param(value = "pageNumber", df = "1") int pageNumber) {
		return gameServerService.getObjectListByPager(pageNumber, 20);
	}

	@At("/change/?")
	@Ok("json")
	@RequiresUser
	public Message select(@Param("id") int id, @Attr(Webs.ME) User user, HttpServletRequest req, HttpSession session) {
		GameServer server = user.getServers().get(id);
		if (!Lang.isEmpty(server)) {
			session.setAttribute("sid", id);
			return Message.success("button.submit.success", req);
		}
		return Message.error("admin.message.error", req);
	}

	@At
	@Ok("fm:template.admin.server.add")
	@RequiresPermissions("server:add")
	@PermissionTag(name = "添加应用", tag = "游戏应用", enable = true)
	public List<PlatformVO> add() {
		return authTypeList;
	}

	@At
	@Ok("json")
	@RequiresPermissions("server:add")
	@PermissionTag(name = "添加应用", tag = "游戏应用", enable = false)
	public Message o_save(@Param("::p.") GameServer server, HttpServletRequest req, @Param("..") NutMap map) {
		if (gameServerService.canAdd(server.getPid())) {
			gameServerService.insert(server);
			return Message.success("button.submit.success", req);
		}
		Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, Object> entry = iterator.next();
			String key = entry.getKey();
			if (key.startsWith("p.")) {
				iterator.remove();
			}
		}
		server.setMobileAuth(map);
		return Message.error("admin.message.error", req);
	}

	@At
	@Ok("fm:template.admin.server.edit")
	@RequiresPermissions("server:edit")
	@PermissionTag(name = "编辑应用", tag = "游戏应用", enable = true)
	public GameServer edit(@Param("id") int id, HttpServletRequest request) throws Throwable {
		GameServer server = gameServerService.getServer(id);
		if (Lang.isEmpty(server)) {
			throw new Throwable();
		}
		request.setAttribute("authTypes", authTypeList);
		return server;
	}

	@At
	@Ok(">>:/admin/game_server/list")
	@RequiresPermissions("server:edit")
	@PermissionTag(name = "编辑应用", tag = "游戏应用", enable = false)
	public void update(@Param("::server.") GameServer server, @Param("..") NutMap map) {
		gameServerService.update(server);
	}

	@At
	@Ok("fm:template.admin.server.servers_edit")
	@RequiresUser
	public void v_servers_edit(@Param("id") long id, HttpServletRequest request) {
		User user = userService.view(id);
		List<Integer> list = gameServerService.getAllIds();
		List<GameServer> servers = new ArrayList<GameServer>();
		for (Integer iid : list) {
			GameServer server = gameServerService.getServer(iid.intValue());
			if (!Lang.isEmpty(server)) {
				servers.add(server);
			}
		}
		List<Integer> userServeridList = new ArrayList<Integer>();
		Map<Integer, GameServer> serverMap = user.getServers();
		if (!Lang.isEmpty(serverMap)) {
			Iterator<Integer> userlist = serverMap.keySet().iterator();
			while (userlist.hasNext()) {
				int serverid = userlist.next();
				userServeridList.add(serverid);
			}
		}
		request.setAttribute("serverList", servers);
		request.setAttribute("serverIds", userServeridList);
	}

	@At
	@Ok("json")
	@RequiresPermissions("server:add")
	@PermissionTag(name = "添加应用", tag = "游戏应用", enable = false)
	public Message o_update(@Param("::p.") GameServer server, HttpServletRequest req, @Param("..") NutMap map) {
		Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, Object> entry = iterator.next();
			String key = entry.getKey();
			if (key.startsWith("p.")) {
				iterator.remove();
			}
		}
		server.setMobileAuth(map);
		gameServerService.update(server);
		return Message.success("button.submit.success", req);
	}
}
