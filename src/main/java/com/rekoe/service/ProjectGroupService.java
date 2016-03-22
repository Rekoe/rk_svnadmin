package com.rekoe.service;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import com.rekoe.domain.PjGr;

/**
 * @author 科技㊣²º¹³<br/>
 *         2014年2月3日 下午4:48:45<br/>
 *         http://www.rekoe.com<br/>
 *         QQ:5382211
 */
@IocBean(args = { "refer:dao" })
public class ProjectGroupService extends BaseService<PjGr> {

	public ProjectGroupService(Dao dao) {
		super(dao);
	}

	public PjGr get(String pj, String gr) {
		return dao().fetch(getEntityClass(), Cnd.where("pj", "=", pj).and("gr", "=", gr));
	}

	public void save(PjGr pjGr) {
		if (this.get(pjGr.getPj(), pjGr.getGr()) == null) {
			dao().insert(pjGr);
		} else {
			dao().update(getEntityClass(), Chain.make("des", pjGr.getDes()), Cnd.where("pj", "=", pjGr.getPj()).and("gr", "=", pjGr.getGr()));
		}
	}
}
