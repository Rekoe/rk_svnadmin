package com.rekoe.service;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.nutz.aop.interceptor.ioc.TransAop;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.ioc.aop.Aop;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;

import com.rekoe.domain.Pj;
import com.rekoe.domain.PjAuth;
import com.rekoe.domain.PjGr;
import com.rekoe.utils.Constants;

/**
 * @author 科技㊣²º¹³<br/>
 *         2014年2月3日 下午4:48:45<br/>
 *         http://www.rekoe.com<br/>
 *         QQ:5382211
 */
@IocBean(args = { "refer:dao" })
public class ProjectService extends BaseService<Pj> {

	public ProjectService(Dao dao) {
		super(dao);
	}

	@Inject
	private ProjectGroupService projectGroupService;

	@Inject
	private SvnService svnService;

	@Inject
	private ProjectAuthService projectAuthService;

	public Pj get(String pj) {
		return dao().fetch(getEntityClass(), Cnd.where("pj", "=", pj));
	}

	public boolean nameOk(String name) {
		if (StringUtils.isBlank(name)) {
			return false;
		}
		char[] chars = name.toCharArray();
		for (char c : chars) {
			if (Strings.isChineseCharacter(c)) {
				return false;
			}
		}
		return Lang.isEmpty(dao().fetch(getEntityClass(), Cnd.where("pj", "=", name)));
	}

	@Inject
	private RepositoryService repositoryService;
	@Inject
	private ProjectConfigService projectConfigService;

	/**
	 * 保存。<br>
	 * 数据库里已经存在相同的路径或url的项目，不可以保存。<br>
	 * 如果仓库不存在，自动创建。<br>
	 * 如果是增加项目，自动创建默认3个组。
	 * 
	 * @param pj
	 *            项目
	 */
	@Aop(TransAop.READ_COMMITTED)
	public void save(Pj pj) {
		// 是否可以增加项目
		boolean insert = nameOk(pj.getPj());
		String path = projectConfigService.getRepoPath(pj);
		// 创建仓库
		File respository = new File(path);
		if (!respository.exists() || !respository.isDirectory()) {// 不存在仓库
			RepositoryService.createLocalRepository(respository);
		}
		if (insert) {
			// 增加默认的组
			insert(pj);
			for (String gr : Constants.GROUPS) {
				PjGr pjGr = new PjGr();
				pjGr.setPj(pj.getPj());
				pjGr.setGr(gr);
				pjGr.setDes(gr);
				projectGroupService.save(pjGr);
			}
			//
			String res = this.projectAuthService.formatRes(pj, "/");
			PjAuth pjAuthDef = new PjAuth();
			pjAuthDef.setPj(pj.getPj());
			pjAuthDef.setRes(res);
			pjAuthDef.setRw("rw");
			pjAuthDef.setUsr("admin");
			projectAuthService.saveByUsr(pjAuthDef);
			// 增加默认的权限 @see Issue 29
			PjAuth pjAuth = new PjAuth();
			pjAuth.setPj(pj.getPj());
			pjAuth.setRes(res);
			pjAuth.setRw("rw");
			pjAuth.setGr(Constants.GROUP_MANAGER);
			projectAuthService.saveByGr(pjAuth);
		} else {
			dao().update(pj);
		}
		svnService.exportConfig(pj.getPj());
		// createDefatutDirs(pj);
	}

	// @Async
	public void createDefatutDirs(Pj pj) {
		repositoryService.createDir(pj);
	}

	/**
	 * 获取项目的相对根路径.例如项目的path=e:/svn/projar，则返回projar。如果path为空，则返回项目ID
	 * 
	 * @param pj
	 *            项目id
	 * @return 项目的相对根路径
	 * @since 3.0.3
	 */
	public String getRelateRootPath(String pj) {
		Pj p = this.get(pj);
		if (p == null) {
			return pj;
		}
		return pj;
	}

	/**
	 * 获取项目的相对根路径.例如项目的path=e:/svn/projar，则返回projar。如果path为空，则返回项目ID
	 * 
	 * @param pj
	 *            项目
	 * @return 项目的相对根路径
	 * @since 3.0.3
	 */
	public String getRelateRootPath(Pj pj) {
		return pj.getPj();
	}

	@Inject
	private ProjectGroupUsrService projectGroupUsrService;

	@Inject
	private ProjectUserService projectUserService;
	@Inject
	private ProjectService projectService;

	@Aop(TransAop.READ_COMMITTED)
	public void delete(String pj) {
		deleteDB(pj);
		svnService.exportConfig(pj);
	}

	public void deleteDB(String pj) {
		projectAuthService.deletePj(pj);
		projectGroupUsrService.deletePj(pj);
		projectGroupService.deletePj(pj);
		projectUserService.deletePj(pj);
		dao().clear(getEntityClass(), Cnd.where("pj", "=", pj));
	}

	public int update(Pj pj) {
		return dao().update(pj);
	}

	/**
	 * @param usr
	 *            用户
	 * @return 用户有权限的项目列表(用户是否是这个项目的管理员)
	 */
	public List<Pj> getList(String usr) {
		Sql sql = Sqls.create("select p.pj,p.des,p.type,pm.pj manager from ( " + " select distinct a.pj,a.des,a.type from pj a where  " + " exists (select b.usr from pj_gr_usr b where a.pj=b.pj and b.usr=@usr)  " + " or exists(select c.usr from pj_usr_auth c where a.pj=c.pj and c.usr=@usr) " + " ) p " + " left join ( " + " select distinct a.pj from pj a where  " + " exists (select b.usr from pj_gr_usr b where a.pj=b.pj and b.usr=@usr and b.gr like @like)" + " ) pm on p.pj=pm.pj");
		final List<Pj> list = new ArrayList<Pj>();
		sql.setCallback(new SqlCallback() {

			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				while (rs.next()) {
					Pj pj = readPj(rs);
					String manager = rs.getString("manager");// 是否是管理员组的用户
					pj.setManager(StringUtils.isNotBlank(manager));
					list.add(pj);
				}
				return list;
			}
		});
		sql.setParam("usr", usr).setParam("like", "%" + Constants.GROUP_MANAGER);
		dao().execute(sql);
		return list;
	}

	public Pj readPj(ResultSet rs) throws SQLException {
		Pj result = new Pj();
		result.setPj(rs.getString("pj"));
		result.setDes(rs.getString("des"));
		result.setType(rs.getString("type"));
		return result;
	}
}