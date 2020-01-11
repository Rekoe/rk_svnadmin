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
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.Encoding;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.IocBy;
import org.nutz.mvc.annotation.Localization;

import com.rekoe.domain.Pj;
import com.rekoe.domain.PjGrUsr;
import com.rekoe.domain.ProjectConfig;
import com.rekoe.domain.User;
import com.rekoe.domain.Usr;
import com.rekoe.service.AuthorityService;
import com.rekoe.service.ProjectConfigService;

import freemarker.template.Configuration;

@Fail(">>:/admin/common/unauthorized.rk")
@Encoding(input = "UTF-8", output = "UTF-8")
@Localization(value = "msg/", defaultLocalizationKey = "zh-CN")
@IocBean(create = "init")
@IocBy(args = { "*slog" })
public class MainModule {

	@Inject
	private Dao dao;

	@Inject
	private AuthorityService authorityService;

	@Inject
	private ProjectConfigService projectConfigService;

	@Inject
	private Configuration configuration;

	public void init() {
		configuration.setAutoImports(new HashMap<String, String>(2) {
			private static final long serialVersionUID = 7208484815721559298L;
			{
				put("p", "/ftl/pony/index.ftl");
				put("s", "/ftl/spring.ftl");
			}
		});

		dao.create(PjGrUsr.class, false);
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
		authorityService.initFormPackage("com.rekoe");
		projectConfigService.init();
	}
}
