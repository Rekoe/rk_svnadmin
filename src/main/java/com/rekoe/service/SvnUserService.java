package com.rekoe.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;

import com.rekoe.domain.Pj;
import com.rekoe.domain.SVNRoleType;
import com.rekoe.domain.Usr;

/**
 * @author 科技㊣²º¹³M<br/>
 *         2014年2月3日 下午4:48:45 <br/>
 *         http://www.rekoe.com <br/>
 *         QQ:5382211
 */
@IocBean(args = { "refer:dao" })
public class SvnUserService extends BaseService<Usr> {

	private final String REGEX_USERNAME = "^[a-zA-Z0-9]{1,16}$";

	public SvnUserService() {
	}

	public SvnUserService(Dao dao) {
		super(dao);
	}

	public boolean nameOk(String name) {
		if (StringUtils.isBlank(name)) {
			return false;
		}
		if (!isUsername(name)) {
			return false;
		}
		return Lang.isEmpty(dao().fetch(getEntityClass(), Cnd.where("usr", "=", name)));
	}

	public Usr get(String usr) {
		return dao().fetch(getEntityClass(), Cnd.where("usr", "=", usr));
	}

	/**
	 * 校验用户名
	 * 
	 * @param username
	 * @return 校验通过返回true，否则返回false
	 */
	public boolean isUsername(String username) {
		return Pattern.matches(REGEX_USERNAME, username);
	}

	/**
	 * 获取这个项目组未选的用户(不包括*)
	 * 
	 * @param pj
	 *            项目
	 * @param gr
	 *            组
	 * @return 项目组未选的用户(不包括*)
	 */
	public List<Usr> listUnSelected(String pj, String gr) {
		Sql sql = Sqls.create("select usr,name,psw,email,role from usr a where a.usr <> '*' " + " and not exists (select usr from pj_gr_usr b where a.usr = b.usr and b.pj=@pj and b.gr=@gr) order by a.usr");
		sql.setParam("pj", pj).setParam("gr", gr);
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

	public List<Usr> listSelected(String pj) {
		Sql sql = Sqls.create("select * from usr a where a.usr <> '*' " + " and exists (select usr from pj_gr_usr b where a.usr = b.usr and b.pj=@pj) order by a.usr");
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
		result.setRole(SVNRoleType.valueOf(rs.getString("role")));
		result.setEmail(rs.getString("email"));
		return result;
	}

	@Inject
	private ProjectService projectService;

	public List<Pj> getPjList(String usr) {
		List<Pj> list = projectService.getList(usr);// 用户可以看到的所有项目
		return list;
	}
}
