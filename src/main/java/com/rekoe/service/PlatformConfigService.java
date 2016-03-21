package com.rekoe.service;

import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.pager.Pager;
import org.nutz.ioc.loader.annotation.IocBean;

import com.rekoe.common.page.Pagination;
import com.rekoe.domain.PlatformConfig;

/**
 * @author 科技㊣²º¹³<br />
 *         Mar 27, 2013 6:09:25 PM <br />
 *         QQ:5382211
 */
@IocBean(args = { "refer:dao" })
public class PlatformConfigService extends BaseService<PlatformConfig> {

	public PlatformConfigService(Dao dao) {
		super(dao);
	}

	public Pagination getListByPager(int pageNumber, int sid) {
		Cnd cnd = Cnd.where("sid", "=", sid);
		Pager pager = dao().createPager(pageNumber, 20);
		List<PlatformConfig> list = dao().query(getEntityClass(), cnd, pager);
		pager.setRecordCount(dao().count(getEntityClass(), cnd));
		return new Pagination(pageNumber, 20, pager.getRecordCount(), list);
	}

	public PlatformConfig getPlatformConfig(int sid, String provider) {
		return dao().fetch(getEntityClass(), Cnd.where("sid", "=", sid).and("provider", "=", provider));
	}

	public void update(PlatformConfig conf) {
		dao().update(conf,"^(config)$");
	}
}
