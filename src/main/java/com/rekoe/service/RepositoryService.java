/**
 * 
 */
package com.rekoe.service;

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.tmatesoft.svn.core.SVNAuthenticationException;
import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNCommitClient;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import com.rekoe.domain.Pj;
import com.rekoe.domain.PjUsr;
import com.rekoe.domain.ProjectConfig;
import com.rekoe.domain.Usr;
import com.rekoe.utils.EncryptUtil;
import com.rekoe.utils.UsrProvider;

/**
 * 仓库服务层
 */
@IocBean(create = "init")
public class RepositoryService {

	/**
	 * 日志
	 */
	private final Log log = Logs.get();

	@Inject
	private ProjectService projectService;

	@Inject
	private ProjectUserService projectUserService;

	/**
	 * 获取svn仓库
	 * 
	 * @param pjId
	 *            项目ID
	 * @return svn仓库
	 * @throws SVNException
	 *             svn异常，例如没有权限等
	 */
	public SVNRepository getRepository(String pjId) throws SVNException {
		Pj pj = projectService.fetch(Cnd.where("pj", "=", pjId));
		if (pj == null) {
			log.warn("Not found project: " + pjId);
			return null;
		}
		return this.getRepository(pj);
	}

	/**
	 * 从项目的url中获取svn的url
	 * 
	 * @param url
	 *            项目url
	 * @return svn url
	 */
	public static String parseURL(String url) {
		if (StringUtils.isBlank(url)) {
			return null;
		}
		String result = url.trim();// 去空格
		result = StringUtils.replace(result, "\t", " ");
		result = StringUtils.replace(result, "\r", " ");
		result = StringUtils.replace(result, "\n", " ");
		result = StringUtils.replace(result, "\b", " ");
		result = StringUtils.replace(result, "<", " ");// eg. <br/>
		result = StringUtils.replace(result, "(", " ");// eg. ()

		result = result.trim();
		int blank = result.indexOf(" ");
		if (blank != -1) {
			result = result.substring(0, blank);
		}

		return result;
	}

	/**
	 * 获取svn仓库
	 * 
	 * @param pj
	 *            项目
	 * @return svn仓库
	 * @throws SVNException
	 *             svn异常，例如没有权限等
	 */
	@SuppressWarnings("deprecation")
	public SVNRepository getRepository(Pj pj) throws SVNException {
		Usr usr = UsrProvider.getCurrentUsr();
		String svnUrl = parseURL(projectConfigService.getProjectUrl(pj));
		if (StringUtils.isBlank(svnUrl)) {
			throw new RuntimeException("URL不可以为空");
		}
		String svnUserName = usr.getUsr();
		String svnPassword = usr.getPsw();
		if (!com.rekoe.utils.Constants.HTTP_MUTIL.equals(pj.getType())) {
			// pj_usr覆盖用户的密码
			PjUsr pjUsr = projectUserService.get(pj.getPj(), svnUserName);
			if (pjUsr != null) {
				svnPassword = pjUsr.getPsw();
			}
		}
		svnPassword = EncryptUtil.decrypt(svnPassword);// 解密
		SVNRepository repository = SVNRepositoryFactory.create(SVNURL.parseURIDecoded(svnUrl));
		ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(svnUserName, svnPassword);
		repository.setAuthenticationManager(authManager);
		return repository;
	}

	@Inject
	private ProjectConfigService projectConfigService;

	public String getProjectSVNUrl(Pj pj) {
		ProjectConfig conf = projectConfigService.get();
		String svnurl = conf.getDomainPath() + pj.getPj();
		return parseURL(svnurl);
	}

	/**
	 * 创建初始化的文件夹
	 * 
	 * @param pj
	 * @throws SVNException
	 */
	public synchronized boolean createDir(Pj pj) {
		Usr usr = UsrProvider.getCurrentUsr();
		String svnUrl = getProjectSVNUrl(pj);
		if (StringUtils.isBlank(svnUrl)) {
			throw new RuntimeException("URL不可以为空");
		}
		String svnUserName = usr.getUsr();
		String svnPassword = usr.getPsw();
		if (!com.rekoe.utils.Constants.HTTP_MUTIL.equals(pj.getType())) {
			// pj_usr覆盖用户的密码
			PjUsr pjUsr = projectUserService.get(pj.getPj(), svnUserName);
			if (pjUsr != null) {
				svnPassword = pjUsr.getPsw();
			}
		}
		svnPassword = EncryptUtil.decrypt(svnPassword);// 解密
		ProjectConfig conf = projectConfigService.get();
		List<String> dirs = conf.getDirs();
		SVNURL[] urlAr = new SVNURL[dirs.size()];
		int i = 0;
		for (String url : dirs) {
			try {
				String tempUrl = getProjectSVNUrl(pj) + "/" + url;
				urlAr[i] = SVNURL.parseURIEncoded(tempUrl);
			} catch (SVNException e) {
				log.error(e);
			}
			i++;
		}
		return svnMkDirs(urlAr);
	}

	public boolean svnMkDirs(SVNURL[] urls) {
		ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager("admin", "john");
		SVNClientManager manager = SVNClientManager.newInstance();
		manager.setAuthenticationManager(authManager);
		SVNCommitClient commitClient = SVNClientManager.newInstance().getCommitClient();
		try {
			SVNCommitInfo info = commitClient.doMkDir(urls, "commitMessage", null, true);
			long newRevision = info.getNewRevision();
			if (log.isDebugEnabled()) {
				if (newRevision >= 0)
					log.debug("commit successful: new revision = " + newRevision);
				else
					log.debug("no commits performed (commit operation returned new revision < 0)");
			}
		} catch (SVNException e) {
			log.error(e);
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager("admin", "john");
		SVNClientManager manager = SVNClientManager.newInstance();
		manager.setAuthenticationManager(authManager);
		SVNCommitClient commitClient = SVNClientManager.newInstance().getCommitClient();
		try {
			SVNCommitInfo info = commitClient.doMkDir(new SVNURL[] { SVNURL.parseURIEncoded("http://192.168.3.127/repository/koux/branches") }, "commitMessage", null, true);
			long newRevision = info.getNewRevision();
			System.out.println(newRevision);
		} catch (SVNException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 返回项目仓库的根
	 * 
	 * @param pj
	 *            项目
	 * @return 仓库根
	 */
	public String getRepositoryRoot(Pj pj) {
		SVNRepository repository = null;
		try {
			repository = this.getRepository(pj);
			return repository.getRepositoryRoot(true).toString();
		} catch (SVNAuthenticationException e) {
			log.error(e.getMessage());
			return null;
		} catch (SVNException e) {
			log.error(e.getMessage());
			return null;
		} finally {
			if (repository != null) {
				repository.closeSession();
			}
		}
	}

	/**
	 * 获取项目指定路径的svn仓库文件系统
	 * 
	 * @param pj
	 *            项目
	 * @param path
	 *            相对仓库根目录的路径
	 * @return 目录或文件系统
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Collection<SVNDirEntry> getDir(String pj, String path) {
		if (StringUtils.isBlank(path)) {
			path = "/";// root
		}
		if (!path.startsWith("/")) {
			path = "/" + path;
		}
		SVNRepository repository = null;
		try {
			repository = this.getRepository(pj);
			SVNProperties properties = new SVNProperties();
			return repository.getDir(path, SVNRevision.HEAD.getNumber(), properties, (Collection) null);
		} catch (SVNAuthenticationException e) {
			log.error(e);
			throw new RuntimeException("认证失败");
		} catch (SVNException e) {
			log.error(e);
			throw new RuntimeException(e.getMessage());
		} finally {
			if (repository != null) {
				repository.closeSession();
			}
		}
	}

	/**
	 * Creates a local blank FSFS-type repository. A call to this routine is
	 * equivalent to
	 * <code>createLocalRepository(path, null, enableRevisionProperties, force)</code>
	 * .
	 * 
	 * @param respository
	 *            a repository root location
	 * @return a local URL (file:///) of a newly created repository
	 */
	public static SVNURL createLocalRepository(File respository) {
		try {
			return SVNRepositoryFactory.createLocalRepository(respository, true, false);
		} catch (SVNException e) {
			throw new RuntimeException(String.format("pj.save.error.createRepository", "创建仓库失败.{0}", new Object[] { respository.getAbsolutePath() }) + " : " + e.getMessage());
		}
	}

	public void init() {
		/*
		 * For using over http:// and https://
		 */
		DAVRepositoryFactory.setup();
		/*
		 * For using over svn:// and svn+xxx://
		 */
		SVNRepositoryFactoryImpl.setup();

		/*
		 * For using over file:///
		 */
		FSRepositoryFactory.setup();

	}

}
