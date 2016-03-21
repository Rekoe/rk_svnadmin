package com.rekoe.service;

import java.util.List;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import com.rekoe.domain.OfficialServer;

/**
 * 服务器列表
 * 
 * @author kouxian
 *
 */
@IocBean(args = { "refer:dao" })
public class OfficialServerService extends BaseService<OfficialServer> {

	/**
	 * 推荐
	 */
	public static final short SERVER_TYPE_STATUS_RECOMMEND = 1;

	/**
	 * 正常
	 */
	public static final short SERVER_TYPE_STATUS_NORMAL = 2;

	/**
	 * 拥挤 繁忙
	 */
	public static final short SERVER_TYPE_STATUS_BUSY = 3;

	/**
	 * 关闭
	 */

	public static final short SERVER_TYPE_STATUS_CLOSE = 4;

	public OfficialServerService(Dao dao) {
		super(dao);
	}

	public void update(OfficialServer v) {
		if (v.getStatus() == SERVER_TYPE_STATUS_RECOMMEND) {
			dao().update(getEntityClass(), Chain.make("status", SERVER_TYPE_STATUS_NORMAL), Cnd.where("status", "=", SERVER_TYPE_STATUS_RECOMMEND).and("pid", "=", v.getPid()));
		}
		dao().update(v, "(sid|url|name|status|open|openWhiteList)$");
	}

	/**
	 * 如果添加的服务器为推荐服务器 那需要把原来的推荐服务器设置为默认开启状态 "0":"关闭","1":"推荐","2":"拥挤","3":"正常"
	 */
	public boolean insert(OfficialServer offic) {
		if (offic.getStatus() == SERVER_TYPE_STATUS_RECOMMEND) {
			dao().update(getEntityClass(), Chain.make("status", SERVER_TYPE_STATUS_NORMAL), Cnd.where("status", "=", SERVER_TYPE_STATUS_RECOMMEND).and("pid", "=", offic.getPid()).and("status", "<>", SERVER_TYPE_STATUS_CLOSE));
		}
		return super.insert(offic);
	}

	public List<OfficialServer> getList(int pid) {
		return dao().query(getEntityClass(), Cnd.where("pid", "=", pid));
	}

	public List<OfficialServer> getShowList(int pid) {
		return dao().query(getEntityClass(), Cnd.where("pid", "=", pid).and("status", "<>", SERVER_TYPE_STATUS_CLOSE));
	}

	/**
	 * 获得推荐服务器
	 * 
	 * @param pid
	 * @return
	 */
	public OfficialServer getRecommendServer(int pid) {
		return dao().fetch(getEntityClass(), Cnd.where("pid", "=", pid).and("status", "=", SERVER_TYPE_STATUS_RECOMMEND));
	}

	public OfficialServer getOfficialServer(int pid, int sid) {
		return dao().fetch(getEntityClass(), Cnd.where("pid", "=", pid).and("sid", "=", sid));
	}
}
