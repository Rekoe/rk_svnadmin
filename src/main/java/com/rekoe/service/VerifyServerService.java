package com.rekoe.service;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import com.rekoe.domain.VerifyServer;

/**
 * 提审服
 * 
 * @author kouxian
 *
 */
@IocBean(args = { "refer:dao" })
public class VerifyServerService extends BaseService<VerifyServer> {

	public VerifyServerService(Dao dao) {
		super(dao);
	}

	public void update(VerifyServer v) {
		dao().update(v, "(gid|url|name|loginUrl|payUrl|open)$");
	}

	public VerifyServer getVerifyServer(int pid) {
		return dao().fetch(getEntityClass(), Cnd.where("pid", "=", pid));
	}
}
