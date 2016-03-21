package com.rekoe.service;

import org.apache.commons.lang3.StringUtils;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import com.rekoe.common.page.Pagination;
import com.rekoe.domain.PlatformUser;

@IocBean(args = { "refer:dao" })
public class PlatformUserService extends BaseService<PlatformUser> {

	public PlatformUserService(Dao dao) {
		super(dao);
	}

	public PlatformUser getPlatformUser(int pid, String passportid, String pfid) {
		PlatformUser user = dao().fetch(getEntityClass(), Cnd.where("pid", "=", pid).and("passportid", "=", passportid).and("pfid", "=", pfid));
		return user;
	}

	public PlatformUser add(int pid, String passportid, long openid, String pfid, String addr) {
		return dao().insert(new PlatformUser(pid, passportid, openid, pfid, addr));
	}

	public PlatformUser add(int pid, String passportid, long openid, String pfid) {
		return dao().insert(new PlatformUser(pid, passportid, openid, pfid));
	}

	public Pagination getPlatformUserListPagerByPid(Integer pageNumber, int pageSize, String name) {
		return getObjListByPager(pageNumber, pageSize, StringUtils.isBlank(name) ? Cnd.orderBy().desc("createTime") : Cnd.where("passportid", "=", name));
	}

	public Pagination getPlatformUserListPagerByOpenid(Integer pageNumber, int pageSize, long openid) {
		return getObjListByPager(pageNumber, pageSize, openid == 0 ? Cnd.orderBy().desc("createTime") : Cnd.where("openid", "=", openid).desc("createTime"));
	}

	public void lock(String id, boolean lock) {
		dao().update(PlatformUser.class, Chain.make("locked", lock), Cnd.where("id", "=", id));
	}
}
