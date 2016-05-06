package com.rekoe;

import java.util.HashMap;
import java.util.List;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.nutz.dao.Dao;
import org.nutz.dao.impl.FileSqlManager;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.util.Daos;
import org.nutz.ioc.Ioc;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.Setup;
import org.nutz.plugins.view.freemarker.FreeMarkerConfigurer;
import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNCommitClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import com.rekoe.domain.Pj;
import com.rekoe.domain.PjGrUsr;
import com.rekoe.domain.ProjectConfig;
import com.rekoe.domain.User;
import com.rekoe.domain.Usr;
import com.rekoe.service.ProjectConfigService;
import com.rekoe.service.UserService;

import freemarker.template.Configuration;

/**
 * @author 科技㊣²º¹³ <br />
 *         2014年2月3日 下午4:48:45<br />
 *         http://www.rekoe.com <br />
 *         QQ:5382211
 */
public class MvcSetup implements Setup {

	@SuppressWarnings("serial")
	@Override
	public void init(NutConfig config) {
		Ioc ioc = config.getIoc();
		// 加载freemarker自定义标签　自定义宏路径
		ioc.get(Configuration.class).setAutoImports(new HashMap<String, String>(2) {
			{
				put("p", "/ftl/pony/index.ftl");
				put("s", "/ftl/spring.ftl");
			}
		});
		ioc.get(FreeMarkerConfigurer.class, "mapTags");
		Dao dao = ioc.get(Dao.class);
		dao.create(PjGrUsr.class, false);
		// dao.clear(OAuthUser.class);
		Daos.createTablesInPackage(dao, User.class.getPackage().getName(), false);
		Daos.migration(dao, Usr.class, true, true, false);
		Daos.migration(dao, Pj.class, true, true, false);
		Daos.migration(dao, ProjectConfig.class, true, true, false);
		if (0 == dao.count(User.class)) {
			FileSqlManager fm = new FileSqlManager("init_system_h2.sql");
			List<Sql> sqlList = fm.createCombo(fm.keys());
			dao.execute(sqlList.toArray(new Sql[sqlList.size()]));
			List<User> userList = dao.query(User.class, null);
			for (User user : userList) {
				RandomNumberGenerator rng = new SecureRandomNumberGenerator();
				String salt = rng.nextBytes().toBase64();
				String hashedPasswordBase64 = new Sha256Hash("123", salt, 1024).toBase64();
				user.setSalt(salt);
				user.setPassword(hashedPasswordBase64);
				dao.update(user);
			}
		}
		UserService userService = ioc.get(UserService.class);
		userService.initFormPackages("com.rekoe");
		ioc.get(ProjectConfigService.class).init();
	}

	public static void main(String[] args) {
		ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager("admin", "john");
		SVNClientManager manager = SVNClientManager.newInstance();
		manager.setAuthenticationManager(authManager);
		SVNCommitClient commitClient = SVNClientManager.newInstance().getCommitClient();
		try {
			SVNCommitInfo info = commitClient.doMkDir(new SVNURL[] { SVNURL.parseURIEncoded("http://192.168.3.127/repository/koux/trunk") }, "commitMessage", null, true);
			long newRevision = info.getNewRevision();
			System.out.println(newRevision);
		} catch (SVNException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void destroy(NutConfig config) {

	}
}
