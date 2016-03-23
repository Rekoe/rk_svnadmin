package com.rekoe.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
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

	public List<PjGr> getList(String pj) {
		Sql sql = Sqls.create("select pj,gr,des from pj_gr $condition");
		sql.setCondition(Cnd.where("pj", "=", pj).desc("pj,gr"));
		final List<PjGr> list = new ArrayList<PjGr>();
		sql.setCallback(new SqlCallback() {

			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				while (rs.next()) {
					list.add(readPjGr(rs));
				}
				return list;
			}
		});
		dao().execute(sql);
		return list;
	}

	PjGr readPjGr(ResultSet rs) throws SQLException {
		PjGr result = new PjGr();
		result.setPj(rs.getString("pj"));
		result.setGr(rs.getString("gr"));
		result.setDes(rs.getString("des"));
		return result;
	}
}
