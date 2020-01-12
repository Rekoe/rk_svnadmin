package com.rekoe.utils;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import com.rekoe.domain.Pj;
import com.rekoe.domain.PjUsr;
import com.rekoe.domain.ProjectConfig;
import com.rekoe.domain.Usr;
import com.rekoe.service.ProjectConfigService;
import com.rekoe.service.ProjectService;
import com.rekoe.service.ProjectUserService;
import com.rekoe.service.RepositoryService;

@IocBean(create = "init")
public class DoCommit {

	private final static Log log = Logs.get();

	@Inject
	private ProjectService projectService;
	@Inject
	private RepositoryService repositoryService;

	@Inject
	private ProjectUserService projectUserService;

	@Inject
	private ProjectConfigService projectConfigService;

	public boolean mkdirs(String pj) {
		ProjectConfig conf = projectConfigService.get();
		List<String> dirs = conf.getDirs();
		return mkdirs(pj, Lang.collection2array(dirs));
	}

	public boolean mkdirs(String pj, String[] dirs) {
		Pj project = projectService.get(pj);
		Usr usr = UsrProvider.getCurrentUsr();
		String svnUrl = repositoryService.getProjectSVNUrl(project);
		if (StringUtils.isBlank(svnUrl)) {
			throw new RuntimeException("URL不可以为空");
		}
		String svnUserName = usr.getUsr();
		String svnPassword = usr.getPsw();
		if (!com.rekoe.utils.Constants.HTTP_MUTIL.equals(project.getType())) {
			// pj_usr覆盖用户的密码
			PjUsr pjUsr = projectUserService.get(project.getPj(), svnUserName);
			if (pjUsr != null) {
				svnPassword = pjUsr.getPsw();
			}
		}
		svnPassword = EncryptUtil.decrypt(svnPassword);// 解密
		svnPassword = "123456";
		SVNURL[] urlAr = new SVNURL[dirs.length];
		int i = 0;
		for (String url : dirs) {
			try {
				String tempUrl = repositoryService.getProjectSVNUrl(project) + "/" + url;
				urlAr[i] = SVNURL.parseURIEncoded(tempUrl);
			} catch (SVNException e) {
				log.error(e);
			}
			i++;
		}
		String name = svnUserName;
		String password = svnPassword;
		ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
		// 实例化客户端管理类
		SVNClientManager ourClientManager = SVNClientManager.newInstance((DefaultSVNOptions) options, name, password);
		// 要提交的文件
		try {
			ourClientManager.getCommitClient().doMkDir(urlAr, "commitMessage");
		} catch (SVNException e) {
			log.error(e);
			return false;
		}
		return true;
	}

	public void init() {
		SVNRepositoryFactoryImpl.setup();
	}
}
