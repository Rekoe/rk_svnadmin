package com.rekoe.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.util.concurrent.ConcurrentHashMap;

import org.nutz.aop.interceptor.async.Async;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import com.cloopen.rest.sdk.CCPRestSDK;
import com.rekoe.common.page.Pagination;
import com.rekoe.domain.GameServer;
import com.rekoe.domain.OAuthUser;

@IocBean(args = { "refer:dao" })
public class GameServerService extends BaseService<GameServer> {

	private final static Log log = Logs.get();

	private ConcurrentHashMap<Integer, CCPRestSDK> ccpSDKMaps = new ConcurrentHashMap<Integer, CCPRestSDK>(5);

	public GameServerService() {
		super();
	}
	
	@Async
	public boolean sendPhoneMail(GameServer server, OAuthUser user, String templateId, String... str) {
		boolean isRight = sendPhoneMail(server, user.getPhoneCode() + "", templateId, str);
		return isRight;
	}
	
	public boolean sendPhoneMail(GameServer server, String phone, String templateId, String ...str) {
		int pid = server.getPid();
		CCPRestSDK temp = ccpSDKMaps.get(pid);
		if (Lang.isEmpty(temp)) {
			NutMap config = server.getMobileAuth();
			String accountSid = config.getString("accountSid","");
			String accountToken = config.getString("accountToken","");
			String AppId = config.getString("AppId");
			String serverIP = config.getString("serverIP");
			String serverPort = config.getString("serverPort");
			CCPRestSDK restAPI = new CCPRestSDK();
			restAPI.init(serverIP, serverPort);
			restAPI.setAccount(accountSid, accountToken);
			restAPI.setAppId(AppId);
			ccpSDKMaps.putIfAbsent(pid, restAPI);
		}
		CCPRestSDK restAPI = ccpSDKMaps.get(pid);
		if (!Lang.isEmpty(restAPI) && server.isUseMobileAuth()) {
			//Map<String, Object> result = restAPI.sendTemplateSMS("13691366833", "29337",  new String[]{"91366","30"});
			Map<String, Object> result = restAPI.sendTemplateSMS(phone, templateId, str);
			if (log.isDebugEnabled()) {
				log.debug(result);
			}
			String res = result.get("statusCode").toString();
			boolean isRight = "000000".equalsIgnoreCase(res);
			return isRight;
		}
		return false;
	}

	public static void main(String[] args) {
		String accountSid = "aaf98f894ecd7d6a014ed7b7ccd30fbb";
		String accountToken = "9951c6eacb424918a36e2c6d4185c491";
		String AppId = "aaf98f894ecd7d6a014ed7bb0e8d0fce";
		String serverIP = "app.cloopen.com";
		String serverPort = "8883";
		CCPRestSDK restAPI = new CCPRestSDK();
		restAPI.init(serverIP, serverPort);
		restAPI.setAccount(accountSid, accountToken);
		restAPI.setAppId(AppId);
		//Map<String, Object> result = restAPI.sendTemplateSMS("13810275348", "29337", new String[]{"91366","30"});
		Map<String, Object> result = restAPI.sendTemplateSMS("13691366833", "29337", new String[]{"91366","30"});
		System.out.println(result);
	}
	public GameServerService(Dao dao) {
		super(dao);
	}

	public List<GameServer> list(Condition cdn) {
		List<GameServer> list = query(cdn, null);
		return list;
	}

	public void update(GameServer server) {
		if (server.getItems() == null) {
			server.setItems(new ArrayList<String>());
		}
		if (server.isUseMobileAuth()) {
			NutMap config = server.getMobileAuth();
			String accountSid = config.getString("accountSid");
			String accountToken = config.getString("accountToken");
			String AppId = config.getString("AppId");
			String serverIP = config.getString("serverIP");
			String serverPort = config.getString("serverPort");
			CCPRestSDK restAPI = new CCPRestSDK();
			restAPI.init(serverIP, serverPort);
			// restAPI.init("app.cloopen.com", "8883");
			restAPI.setAccount(accountSid, accountToken);
			restAPI.setAppId(AppId);
			ccpSDKMaps.putIfAbsent(server.getPid(), restAPI);
		}
		dao().update(server, "(platformName|open|payUrl|loginUrl|version|verify|blackOpen|blackList|clientSecret|authType|mobileAuth)$");
	}

	public boolean insert(GameServer server) {
		return super.insert(server);
	}

	/**
	 * 获得所有的服务器ID
	 */
	public List<Integer> getAllIds() {
		Dao dao = dao();
		Sql sql = Sqls.create("select id from system_server $condition");
		sql.setCondition(Cnd.where("is_open", "=", true));
		sql.setCallback(new SqlCallback() {
			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				List<Integer> results = new ArrayList<Integer>();
				while (rs.next()) {
					results.add(rs.getInt(1));
				}
				return results;
			}
		});
		dao.execute(sql);
		return sql.getList(Integer.class);
	}

	public GameServer getServer(int serverid) {
		GameServer server = dao().fetch(GameServer.class, Cnd.where("id", "=", serverid));
		return server;
	}

	public Pagination getObjectListByPager(Integer pageNumber, int pageSize) {
		return getObjListByPager(dao(), pageNumber, pageSize, null);
	}

	public List<GameServer> loadAll() {
		List<GameServer> gss = dao().query(getEntityClass(), Cnd.where("is_open", "=", true));
		return gss;
	}

	public List<GameServer> loadAllByIds(Integer[] serverIds) {
		List<GameServer> gss = dao().query(getEntityClass(), Cnd.where("id", "iN", serverIds));
		return gss;
	}

	public boolean canAdd(int pid) {
		return Lang.isEmpty(dao().fetch(getEntityClass(), Cnd.where("pid", "=", pid)));
	}

	public GameServer getByPid(int pid) {
		GameServer server = dao().fetch(getEntityClass(), Cnd.where("pid", "=", pid));
		return server;
	}

	public List<GameServer> list() {
		List<GameServer> gss = dao().query(getEntityClass(), null);
		return gss;
	}

}
