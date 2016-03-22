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

import com.rekoe.domain.PjGrUsr;
import com.rekoe.utils.Constants;

/**
 * @author 科技㊣²º¹³<br/>
 *         2014年2月3日 下午4:48:45<br/>
 *         http://www.rekoe.com<br/>
 *         QQ:5382211
 */
@IocBean(args = { "refer:dao" })
public class ProjectGroupUsrService extends BaseService<PjGrUsr> {

	public ProjectGroupUsrService(Dao dao) {
		super(dao);
	}

	/**
	 * @param pj
	 *            项目
	 * @param gr
	 *            组
	 * @param usr
	 *            用户
	 * @return 组用户
	 */
	public PjGrUsr get(String pj, String gr, String usr) {
		Sql sql = Sqls.create("select a.pj,a.usr,a.gr,b.name as usrname from pj_gr_usr a left join usr b on (a.usr=b.usr) where a.pj = $pj and a.gr=$gr and a.usr=$usr");
		sql.setVar("pj", pj).setVar("gr", gr).setVar("usr", usr);
		sql.setCallback(new SqlCallback() {

			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				if (rs.next()) {
					return readPjGrUsr(rs);
				}
				return null;
			}
		});
		dao().execute(sql);
		return sql.getObject(PjGrUsr.class);
	}

	/**
	 * @param pj
	 *            项目
	 * @param gr
	 *            组
	 * @return 组用户列表
	 */
	public List<PjGrUsr> getList(String pj, String gr) {
		Sql sql = Sqls.create("select a.pj,a.usr,a.gr,b.name as usrname from pj_gr_usr a left join usr b on (a.usr = b.usr) where a.pj=$pj and a.gr=$gr order by a.usr");
		sql.setVar("pj", pj).setVar("gr", gr);
		final List<PjGrUsr> list = new ArrayList<PjGrUsr>();
		sql.setCallback(new SqlCallback() {

			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				while (rs.next()) {
					list.add(readPjGrUsr(rs));
				}
				return list;
			}
		});
		dao().execute(sql);
		return list;
	}

	/**
	 * 项目的组用户列表(用户可能为空),导出authz文件时使用
	 * 
	 * @param pj
	 *            项目
	 * @return 项目的组用户列表
	 */
	public List<PjGrUsr> getList(String pj) {
		Sql sql = Sqls.create("select a.pj,a.gr,b.usr,c.name as usrname from pj_gr a left join pj_gr_usr b on (a.pj=b.pj and a.gr=b.gr) left join usr c on (b.usr = c.usr) where a.pj=$pj order by a.gr,b.usr");
		sql.setVar("pj", pj);
		final List<PjGrUsr> list = new ArrayList<PjGrUsr>();
		sql.setCallback(new SqlCallback() {

			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				while (rs.next()) {
					list.add(readPjGrUsr(rs));
				}
				return list;
			}
		});
		dao().execute(sql);
		return list;
	}

	/**
	 * 有相同的svn root的项目的组用户列表(用户可能为空),导出authz文件时使用
	 * 
	 * @param rootPath
	 *            svn root
	 * @return 有相同的svn root的项目组用户
	 */
	public List<PjGrUsr> getListByRootPath(String rootPath) {
		Sql sql = Sqls.create("select a.pj,a.gr,b.usr,c.name as usrname from pj_gr a left join pj_gr_usr b on (a.pj=b.pj and a.gr=b.gr) left join usr c on (b.usr=c.usr) " + " where a.pj in (select distinct pj from pj where type=$type and path like $like) order by a.pj,a.gr,b.usr");
		sql.setVar("type", Constants.HTTP_MUTIL).setVar("like", rootPath + "%");
		final List<PjGrUsr> list = new ArrayList<PjGrUsr>();
		sql.setCallback(new SqlCallback() {

			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				while (rs.next()) {
					list.add(readPjGrUsr(rs));
				}
				return list;
			}
		});
		dao().execute(sql);
		return list;
	}

	/**
	 * @param rs
	 *            ResultSet
	 * @return 组用户
	 * @throws SQLException
	 *             jdbc异常
	 */
	PjGrUsr readPjGrUsr(ResultSet rs) throws SQLException {
		PjGrUsr result = new PjGrUsr();
		result.setPj(rs.getString("pj"));
		result.setUsr(rs.getString("usr"));
		result.setGr(rs.getString("gr"));
		result.setUsrName(rs.getString("usrname"));
		return result;
	}

	/**
	 * 删除
	 * 
	 * @param pj
	 *            项目
	 * @param gr
	 *            组
	 * @param usr
	 *            用户
	 */
	public void delete(String pj, String gr, String usr) {
		dao().clear(getEntityClass(), Cnd.where("pj", "=", pj).and("gr", "=", gr).and("usr", "=", usr));
	}

	/**
	 * 删除
	 * 
	 * @param usr
	 *            用户
	 */
	public void deleteUsr(String usr) {
		dao().clear(getEntityClass(), Cnd.where("usr", "=", usr));
	}

	/**
	 * 删除
	 * 
	 * @param pj
	 *            项目
	 * @param gr
	 *            组
	 */
	public void deletePjGr(String pj, String gr) {
		dao().clear(getEntityClass(), Cnd.where("pj", "=", pj).and("gr", "=", gr));
	}

	/**
	 * 删除
	 * 
	 * @param pj
	 *            项目
	 */
	public void deletePj(String pj) {
		dao().clear(getEntityClass(), Cnd.where("pj", "=", pj));
	}

	/**
	 * 保存
	 * 
	 * @param pjGrUsr
	 *            项目用户
	 */
	public void save(PjGrUsr pjGrUsr) {
		if (this.get(pjGrUsr.getPj(), pjGrUsr.getGr(), pjGrUsr.getUsr()) == null) {
			dao().insert(pjGrUsr);
		}
	}
}
