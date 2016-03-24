package com.rekoe.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.rekoe.domain.Pj;
import com.rekoe.domain.PjAuth;
import com.rekoe.domain.PjGrAuth;
import com.rekoe.utils.Constants;

/**
 * @author 科技㊣²º¹³<br/>
 *         2014年2月3日 下午4:48:45<br/>
 *         http://www.rekoe.com<br/>
 *         QQ:5382211
 */
@IocBean(args = { "refer:dao" })
public class ProjectAuthService extends BaseService<PjAuth> {

	@Inject
	private SvnService svnService;

	@Inject
	private ProjectService projectService;

	public ProjectAuthService(Dao dao) {
		super(dao);
	}

	/**
	 * @param pj
	 *            项目
	 * @return 项目的资源列表
	 */
	public List<String> getResList(String pj) {
		Sql sql = Sqls.create("select distinct res from pj_gr_auth where pj=@pj UNION select distinct res from pj_usr_auth where pj=@pj order by res");
		sql.setCallback(Sqls.callback.strList());
		sql.setParam("pj", pj);
		dao().execute(sql);
		return sql.getList(String.class);
	}

	/**
	 * @param pj
	 *            项目
	 * @param res
	 *            资源
	 * @return 项目资源的权限列表
	 */
	public List<PjAuth> list(String pj, String res) {
		if (StringUtils.isBlank(res)) {
			return getList(pj);
		}
		return getList(pj, res);
	}

	/**
	 * @param pj
	 *            项目
	 * @param res
	 *            资源
	 * @return 项目资源的权限列表
	 */
	private List<PjAuth> getList(String pj, String res) {
		String sqlStr = "select pj,res,rw,gr,' ' usr,' ' usrname from pj_gr_auth where pj=@pj and res = @res " + " UNION " + " select a.pj,a.res,a.rw,' ' gr,a.usr,b.name as usrname from pj_usr_auth a left join usr b on (a.usr=b.usr) where a.pj=@tpj and a.res = @tres " + " order by res,gr,usr";
		Sql sql = Sqls.create(sqlStr);
		sql.setParam("pj", pj).setParam("res", res).setParam("tpj", pj).setParam("tres", res);
		final List<PjAuth> list = new ArrayList<PjAuth>();
		sql.setCallback(new SqlCallback() {

			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				while (rs.next()) {
					list.add(readPjAuth(rs));
				}
				return list;
			}
		});
		return list;
	}

	PjAuth readPjAuth(ResultSet rs) throws SQLException {
		PjAuth result = new PjAuth();
		result.setPj(rs.getString("pj"));
		result.setGr(rs.getString("gr"));
		result.setUsr(rs.getString("usr"));
		result.setUsrName(rs.getString("usrname"));
		result.setRes(rs.getString("res"));
		String rw = rs.getString("rw");
		if (StringUtils.isBlank(rw)) {
			rw = "";
		}
		result.setRw(rw);
		return result;
	}

	/**
	 * @param pj
	 *            项目
	 * @return 项目资源的权限列表
	 */
	public List<PjAuth> getList(String pj) {
		String sqlStr = "select pj,res,rw,gr,' ' usr,' ' usrname from pj_gr_auth where pj=@pj " + " UNION " + " select a.pj,a.res,a.rw,' ' gr,a.usr,b.name as usrname from pj_usr_auth a left join usr b on (a.usr = b.usr) where a.pj=@tpj " + " order by res,gr,usr";
		Sql sql = Sqls.create(sqlStr);
		sql.setParam("pj", pj).setParam("tpj", pj);
		final List<PjAuth> list = new ArrayList<PjAuth>();
		sql.setCallback(new SqlCallback() {

			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				while (rs.next()) {
					list.add(readPjAuth(rs));
				}
				return list;
			}
		});
		dao().execute(sql);
		return list;
	}

	/**
	 * 删除项目 组资源的权限
	 * 
	 * @param pj
	 *            项目
	 * @param gr
	 *            组
	 * @param res
	 *            资源
	 */
	public void deleteByGr(String pj, String gr, String res) {
		Sql sql = Sqls.create("delete from pj_gr_auth $condition");
		sql.setCondition(Cnd.where("pj", "=", pj).and("gr", "=", gr).and("res", "=", res));
		dao().execute(sql);
		svnService.exportConfig(pj);
	}

	/**
	 * 删除项目用户资源的权限
	 * 
	 * @param pj
	 *            项目
	 * @param usr
	 *            用户
	 * @param res
	 *            资源
	 */
	public void deleteByUsr(String pj, String usr, String res) {
		deleteByUsr(pj, usr, res);
		svnService.exportConfig(pj);
	}

	/**
	 * 保存
	 * 
	 * @param pj
	 *            项目
	 * @param res
	 *            资源
	 * @param rw
	 *            可读可写
	 * @param grs
	 *            组
	 * @param usrs
	 *            用户
	 */
	public void save(String pj, String res, String rw, String[] grs, String[] usrs) {
		res = this.formatRes(pj, res);// 如果资源没有[],自动加上
		// gr
		if (grs != null) {
			for (String gr : grs) {
				if (StringUtils.isBlank(gr)) {
					continue;
				}
				PjAuth pjAuth = new PjAuth();
				pjAuth.setPj(pj);
				pjAuth.setRes(res);
				pjAuth.setRw(rw);
				pjAuth.setGr(gr);
				saveByGr(pjAuth);
			}
		}
		// usr
		if (usrs != null) {
			for (String usr : usrs) {
				if (StringUtils.isBlank(usr)) {
					continue;
				}
				PjAuth pjAuth = new PjAuth();
				pjAuth.setPj(pj);
				pjAuth.setRes(res);
				pjAuth.setRw(rw);
				pjAuth.setUsr(usr);
				saveByUsr(pjAuth);
			}
		}
		// export
		svnService.exportConfig(pj);
	}

	/**
	 * @param pj
	 *            项目
	 * @param usr
	 *            用户
	 * @param res
	 *            资源
	 * @return 项目用户资源的权限
	 */
	public PjAuth getByUsr(String pj, String usr, String res) {
		Sql sql = Sqls.create("select a.pj,a.res,a.rw,b.usr,b.name as usrname,' ' gr from pj_usr_auth a left join usr b on (a.usr=b.usr) where a.pj =@pj and a.usr=@usr and a.res=@res");
		sql.setParam("pj", pj).setParam("usr", usr).setParam("res", res);
		sql.setCallback(new SqlCallback() {

			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				if (rs.next()) {
					return readPjAuth(rs);
				}
				return null;
			}
		});
		dao().execute(sql);
		return sql.getObject(PjAuth.class);
	}

	/**
	 * 保存项目用户权限
	 * 
	 * @param pjAuth
	 *            项目用户权限
	 */
	public void saveByUsr(PjAuth pjAuth) {
		if (this.getByUsr(pjAuth.getPj(), pjAuth.getUsr(), pjAuth.getRes()) == null) {
			dao().insert(pjAuth);
		} else {
			dao().update(getEntityClass(), Chain.make("rw", pjAuth.getRw()), Cnd.where("pj", "=", pjAuth.getPj()).and("usr", "=", pjAuth.getUsr()).and("res", "=", pjAuth.getRes()));
		}
	}

	/**
	 * @param pj
	 *            项目
	 * @param gr
	 *            组
	 * @param res
	 *            资源
	 * @return 项目组资源的权限
	 */
	public PjAuth getByGr(String pj, String gr, String res) {
		Sql sql = Sqls.create("select pj,res,rw,gr,' ' usr,' ' usrname from pj_gr_auth");
		sql.setCondition(Cnd.where("pj", "=", pj).and("gr", "=", gr).and("res", "=", res));
		sql.setCallback(new SqlCallback() {

			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				if (rs.next()) {
					return readPjAuth(rs);
				}
				return null;
			}
		});
		dao().execute(sql);
		return sql.getObject(PjAuth.class);
	}

	/**
	 * 保存项目组权限
	 * 
	 * @param pjAuth
	 *            项目组权限
	 */
	public void saveByGr(PjAuth pjAuth) {
		if (this.getByGr(pjAuth.getPj(), pjAuth.getGr(), pjAuth.getRes()) == null) {
			Sql sql = Sqls.create("insert into pj_gr_auth (pj,gr,res,rw) values (@pj,@gr,@res,@rw)");
			sql.setParam("pj", pjAuth.getPj()).setParam("gr", pjAuth.getGr()).setParam("res", pjAuth.getRes()).setParam("rw", pjAuth.getRw());
			dao().execute(sql);
		} else {
			dao().update(PjGrAuth.class, Chain.make("rw", pjAuth.getRw()), Cnd.where("pj", "=", pjAuth.getPj()).and("gr", "=", pjAuth.getGr()).and("res", "=", pjAuth.getRes()));
		}
	}

	/**
	 * 格式化资源.如果资源没有[],自动加上[relateRoot:/]
	 * 
	 * @param pj
	 *            项目id
	 * @param res
	 *            资源
	 * @return 格式化后的资源
	 * @since 3.0.3
	 */
	public String formatRes(String pj, String res) {
		// 如果资源没有[],自动加上
		// if(!res.startsWith("[") && !res.endsWith("]")){
		return this.formatRes(this.projectService.get(pj), res);
		// }
		// return res;
	}

	/**
	 * 格式化资源.如果资源没有[],自动加上[relateRoot:/]
	 * 
	 * @param pj
	 *            项目
	 * @param res
	 *            资源
	 * @return 格式化后的资源
	 * @since 3.0.3
	 */
	public String formatRes(Pj pj, String res) {
		// 去除[xxx:]，重新加上[relateRoot:/]，防止跨项目授权
		res = StringUtils.replaceEach(res, new String[] { "[", "]" }, new String[] { "", "" });
		if (res.indexOf(":") != -1) {
			res = StringUtils.substringAfter(res, ":");
		}

		// 如果资源没有[],自动加上
		String relateRoot = projectService.getRelateRootPath(pj);
		if (!res.startsWith("[") && !res.endsWith("]")) {
			if (res.startsWith("/")) {
				return "[" + relateRoot + ":" + res + "]";
			} else {
				return "[" + relateRoot + ":/" + res + "]";
			}
		}
		return res;
	}

	/**
	 * @param rootPath
	 *            svn root path
	 * @return 具有相同svn root的项目资源的权限列表
	 */
	public List<PjAuth> getListByRootPath(String rootPath) {
		Sql sql = Sqls.create("select pj,res,rw,gr,' ' usr,' ' usrname from pj_gr_auth where pj in (select distinct pj from pj where type=@type and path like @like) " + " UNION " + " select a.pj,a.res,a.rw,' ' gr,a.usr,b.name usrname from pj_usr_auth a left join usr b on (a.usr=b.usr) where a.pj in (select distinct pj from pj where type=@type and path like @like) " + " order by res,gr,usr");
		sql.setParam("type", Constants.HTTP_MUTIL).setParam("like", rootPath + "%");
		final List<PjAuth> list = new ArrayList<PjAuth>();
		sql.setCallback(new SqlCallback() {

			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				while (rs.next()) {
					list.add(readPjAuth(rs));
				}
				return list;
			}
		});
		dao().execute(sql);
		return list;
	}

	/**
	 * 删除项目 资源的权限
	 * 
	 * @param pj
	 *            项目
	 */
	public void deletePj(String pj) {
		Sql sql = Sqls.create("delete from pj_gr_auth $condition");
		sql.setCondition(Cnd.where("pj", "=", pj));
		dao().execute(sql);
		sql = Sqls.create("delete from pj_usr_auth $condition");
		sql.setCondition(Cnd.where("pj", "=", pj));
		dao().execute(sql);
	}
}
