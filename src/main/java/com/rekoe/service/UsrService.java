package com.rekoe.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.ioc.loader.annotation.IocBean;

import com.rekoe.domain.Usr;

/**
 * @author 科技㊣²º¹³<br/>
 *         2014年2月3日 下午4:48:45<br/>
 *         http://www.rekoe.com<br/>
 *         QQ:5382211
 */
@IocBean(args = { "refer:dao" })
public class UsrService extends BaseService<Usr> {

	public UsrService(Dao dao) {
		super(dao);
	}

	/**
	 * @return 所有用户列表
	 */
	public List<Usr> getList() {
		return dao().query(getEntityClass(), null);
	}

	/**
	 * @param pj
	 *            项目
	 * @return 所有项目用户列表(不包括*)
	 */
	public List<Usr> getList(String pj) {
		Sql sql = Sqls.create("select p.usr,p.name,p.role,CASE WHEN pu.psw IS NOT NULL THEN pu.psw ELSE p.psw END psw from (" + " select a.usr,a.role,a.psw,a.name from usr a " + " where " + " exists (select d.usr from pj_gr_usr d where d.usr=a.usr and d.pj=@pj) " + " or exists(select c.usr from pj_usr_auth c where a.usr=c.usr and c.pj=@pj) " + " ) p " + " left join pj_usr pu on (p.usr=pu.usr and pu.pj=@pj) where p.usr <> '*'" + " order by p.usr ");
		sql.setParam("pj", pj);
		final List<Usr> list = new ArrayList<Usr>();
		sql.setCallback(new SqlCallback() {

			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				while (rs.next()) {
					list.add(readUsr(rs));
				}
				return list;
			}
		});
		dao().execute(sql);
		return list;
	}

	Usr readUsr(ResultSet rs) throws SQLException {
		Usr result = new Usr();
		result.setUsr(rs.getString("usr"));
		result.setName(rs.getString("name"));
		result.setPsw(rs.getString("psw"));
		result.setRole(rs.getString("role"));
		return result;
	}

	public List<Usr> getListByRootPath(String rootPath) {
		Sql sql = Sqls.create("select p.usr,p.name,p.role,CASE WHEN pu.psw IS NOT NULL THEN pu.psw ELSE p.psw END psw from (" + " select a.usr,a.role,a.psw,a.name from usr a " + " where " + " exists (select d.usr from pj_gr_usr d where d.usr=a.usr and d.pj in (select distinct pj from pj where type=@type)) " + " or exists(select c.usr from pj_usr_auth c where a.usr=c.usr and c.pj in (select distinct pj from pj where type=@type)) " + " ) p " + " left join pj_usr pu on (p.usr=pu.usr) where p.usr <> '*'" + " order by p.usr ");
		sql.setParam("type", com.rekoe.utils.Constants.HTTP_MUTIL);
		final List<Usr> list = new ArrayList<Usr>();
		sql.setCallback(new SqlCallback() {

			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				while (rs.next()) {
					list.add(readUsr(rs));
				}
				return list;
			}
		});
		dao().execute(sql);
		return list;
	}
}
