package com.rekoe.service;

import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Times;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import com.rekoe.domain.ServerHistory;

@IocBean(args = { "refer:dao" })
public class ServerHistoryService extends BaseService<ServerHistory> {

	private final static Log log = Logs.get();

	/**
	 * Daos.ext(dao(), FieldFilter.create(getEntityClass(),
	 * FieldMatcher.make(null, "(createTime|passportid|pid)$",
	 * true))).update(server);
	 * 
	 * @param dao
	 */
	public ServerHistoryService(Dao dao) {
		super(dao);
	}

	/**
	 * 更新最后登陆时间
	 * 
	 * @param server
	 */
	public void update(ServerHistory server) {
		server.setModifyTime(Times.now());
		dao().update(server, "(modifyTime)$");
	}

	public boolean add(ServerHistory server) {
		try {
			dao().insert(server);
		} catch (Exception e) {
			log.error(e);
			return false;
		}
		return true;
	}

	/**
	 * 获取用户最后登陆的服务器
	 * 
	 * @param passportid
	 * @param pid
	 * @return
	 */
	public ServerHistory getLastLoginServer(long openid, int pid) {
		return dao().fetch(getEntityClass(), Cnd.where("openid", "=", openid).and("pid", "=", pid).desc("modifyTime"));
	}

	public ServerHistory getServerHistory(long openid, int pid, int sid) {
		return dao().fetch(getEntityClass(), Cnd.where("openid", "=", openid).and("pid", "=", pid).and("sid", "=", sid));
	}
	public List<ServerHistory> getLoginServerList(long openid, int pid) {
		return dao().query(getEntityClass(), Cnd.where("openid", "=", openid).and("pid", "=", pid));
	}
	
	public ServerHistory getLoginServerList(long openid, int pid,int limit) {
		return dao().fetch(getEntityClass(), Cnd.where("openid", "=", openid).and("pid", "=", pid).desc("modifyTime"));
	}
}
