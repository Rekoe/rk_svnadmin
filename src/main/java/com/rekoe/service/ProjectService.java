package com.rekoe.service;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.nutz.aop.interceptor.ioc.TransAop;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
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

	public int getCount(String path, String url) {
		int num = dao().count(getEntityClass(), Cnd.where("path", "=", path).or("url", "=", url));
		return num;
	}

	@Inject 
	private RepositoryService repositoryService;
	/**
	 * 保存。<br>
	 * 数据库里已经存在相同的路径或url的项目，不可以保存。<br>
	 * 如果仓库不存在，自动创建。<br>
	 * 如果是增加项目，自动创建默认3个组。
	 * 
	 * @param pj
	 *            项目
	 */
	public void save(Pj pj) {
		// 路径 把\替换为/
		if (StringUtils.isNotBlank(pj.getPath())) {
			pj.setPath(StringUtils.replace(pj.getPath(), "\\", "/"));
		}
		// url 把\替换为/
		if (StringUtils.isNotBlank(pj.getUrl())) {
			pj.setUrl(StringUtils.replace(pj.getUrl(), "\\", "/"));
		}

		// 是否可以增加项目
		boolean insert = nameOk(pj.getPj());
		if (insert) {
			// 数据库里已经存在相同的路径或url的项目
			if (this.getCount(pj.getPath(), pj.getUrl()) > 0) {
				throw new RuntimeException("数据库里已经存在相同的路径或url的仓库项目，请检查路径或url");
			}
		} else {
			// 数据库里已经存在相同的路径或url的项目
			if (this.getCount(pj.getPath(), pj.getUrl()) > 1) {
				throw new RuntimeException("数据库里已经存在多个相同的路径或url的仓库项目，请检查路径或url");
			}
		}
		// 创建仓库
		File respository = new File(pj.getPath());
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
			// 增加默认的权限 @see Issue 29
			PjAuth pjAuth = new PjAuth();
			pjAuth.setPj(pj.getPj());
			pjAuth.setRes(this.projectAuthService.formatRes(pj, "/"));
			pjAuth.setRw("rw");
			pjAuth.setGr(Constants.GROUP_MANAGER);
			projectAuthService.saveByGr(pjAuth);
			repositoryService.createDir(pj);
		} else {
			dao().update(pj);
		}
		svnService.exportConfig(pj.getPj());
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
		if (p == null || StringUtils.isBlank(p.getPath())) {
			return pj;
		}
		return getRelateRootPath(pj);
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
		String path = pj.getPath();
		if (StringUtils.isBlank(path)) {
			return pj.getPj();
		}
		path = StringUtils.replace(path, "\\", "/");
		while (path.endsWith("/")) {
			path = path.substring(0, path.length() - 1);
		}
		return StringUtils.substringAfterLast(path, "/");
	}

	@Inject
	private ProjectGroupUsrService projectGroupUsrService;

	@Inject
	private ProjectUserService projectUserService;
	@Inject
	private ProjectService projectService;

	@Aop(TransAop.READ_COMMITTED)
	public void delete(String pj) {
		projectAuthService.deletePj(pj);
		projectGroupUsrService.deletePj(pj);
		projectGroupService.deletePj(pj);
		projectUserService.deletePj(pj);
		svnService.exportConfig(pj);
		projectService.delete(pj);
	}
}