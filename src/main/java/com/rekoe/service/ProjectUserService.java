package com.rekoe.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.ioc.loader.annotation.IocBean;

import com.rekoe.domain.PjUsr;

/**
 * @author 科技㊣²º¹³<br/>
 *         2014年2月3日 下午4:48:45<br/>
 *         http://www.rekoe.com<br/>
 *         QQ:5382211
 */
@IocBean(args = { "refer:dao" })
public class ProjectUserService extends BaseService<PjUsr> {

	public ProjectUserService(Dao dao) {
		super(dao);
	}

	public PjUsr get(String pj, String usr) {
		Sql sql = Sqls.create("select a.pj,a.usr,a.psw,b.name as usrname from pj_usr a left join usr b on (a.usr = b.usr) where a.pj = $pj and a.usr=$usr");
		sql.setVar("pf", pj).setVar("usr", usr);
		sql.setCallback(new SqlCallback() {

			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				if (rs.next()) {
					PjUsr result = new PjUsr();
					result.setPj(rs.getString("pj"));
					result.setUsr(rs.getString("usr"));
					result.setName(rs.getString("usrname"));
					result.setPsw(rs.getString("psw"));
					return result;
				}
				return null;
			}
		});
		dao().execute(sql);
		return sql.getObject(PjUsr.class);
	}
}
