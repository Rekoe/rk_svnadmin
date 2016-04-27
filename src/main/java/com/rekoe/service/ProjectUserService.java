package com.rekoe.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import com.rekoe.domain.PjUsr;

/**
 * @author 科技㊣²º¹³<br/>
 *         2014年2月3日 下午4:48:45<br/>
 *         http://www.rekoe.com<br/>
 *         QQ:5382211
 */
@IocBean(args = { "refer:dao" })
public class ProjectUserService extends BaseService<PjUsr> {

	private final static Log log = Logs.get();

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

	/**
	 * 删除这个项目的用户
	 * 
	 * @param pj
	 *            项目
	 */
	public void deletePj(String pj) {
		Sql sql = Sqls.create("delete from pj_usr $condition");// where pj = ?";
		sql.setCondition(Cnd.where("pj", "=", pj));
		dao().execute(sql);
	}

	PjUsr readPjUsr(ResultSet rs) throws SQLException {
		PjUsr result = new PjUsr();
		result.setPj(rs.getString("pj"));
		result.setUsr(rs.getString("usr"));
		result.setName(rs.getString("usrname"));
		result.setPsw(rs.getString("psw"));
		return result;
	}

	/**
	 * @param pj
	 *            项目
	 * @return 项目的用户列表
	 */
	public List<PjUsr> getList(String pj) {
		Sql sql = Sqls.create("select a.pj,a.usr,a.psw,b.name usrname from pj_usr a left join usr b on (a.usr = b.usr) where a.pj = @pj");
		sql.setParam("pj", pj);
		final List<PjUsr> list = new ArrayList<PjUsr>();
		sql.setCallback(new SqlCallback() {

			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				while (rs.next()) {
					list.add(readPjUsr(rs));
				}
				return list;
			}
		});
		dao().execute(sql);
		return list;
	}

	/**
	 * 删除
	 * 
	 * @param pj
	 *            项目
	 * @param usr
	 *            用户
	 */
	public void delete(String pj, String usr) {
		Sql sql = Sqls.create("delete from pj_usr $condition");
		sql.setCondition(Cnd.where("pj", "=", pj).and("usr", "=", usr));
		dao().execute(sql);
	}

	/**
	 * 删除用户
	 * 
	 * @param usr
	 *            用户
	 */
	public void deleteUsr(String usr) {
		Sql sql = Sqls.create("delete from pj_usr $condition");
		sql.setCondition(Cnd.where("usr", "=", usr));
		dao().execute(sql);
	}

	/**
	 * 增加一条记录
	 * 
	 * @param pjUsr
	 *            项目用户
	 * @return 成功数量
	 */
	public int add(PjUsr pjUsr) {
		try {
			dao().insert(pjUsr);
		} catch (Exception e) {
			log.error(e);
			return 0;
		}
		return 1;
	}

	/**
	 * 更新用户
	 * 
	 * @param pjUsr
	 *            项目用户
	 * @return 更新的数量
	 */
	public int update(PjUsr pjUsr) {
		return dao().update(pjUsr);
	}
}
