package com.rekoe;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.permission.PermissionResolver;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.nutz.boot.NbApp;
import org.nutz.boot.starter.freemarker.FreeMarkerConfigurer;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.impl.FileSqlManager;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.util.Daos;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.ContinueLoop;
import org.nutz.lang.Each;
import org.nutz.lang.ExitLoop;
import org.nutz.lang.Lang;
import org.nutz.lang.LoopException;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.Encoding;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.IocBy;
import org.nutz.mvc.annotation.Localization;
import org.nutz.plugins.cache.dao.DaoCacheInterceptor;
import org.nutz.resource.Scans;

import com.rekoe.domain.Pj;
import com.rekoe.domain.PjGrUsr;
import com.rekoe.domain.ProjectConfig;
import com.rekoe.domain.User;
import com.rekoe.domain.Usr;
import com.rekoe.service.AuthorityService;
import com.rekoe.service.ProjectConfigService;
import com.rekoe.shiro.freemarker.AuthenticatedTag;
import com.rekoe.shiro.freemarker.GuestTag;
import com.rekoe.shiro.freemarker.HasAnyPermissionTag;
import com.rekoe.shiro.freemarker.HasAnyRolesTag;
import com.rekoe.shiro.freemarker.HasPermissionTag;
import com.rekoe.shiro.freemarker.HasRoleTag;
import com.rekoe.shiro.freemarker.LacksPermissionTag;
import com.rekoe.shiro.freemarker.LacksRoleTag;
import com.rekoe.shiro.freemarker.NotAuthenticatedTag;
import com.rekoe.shiro.freemarker.PrincipalTag;
import com.rekoe.shiro.freemarker.UserTag;
import com.rekoe.web.freemarker.CurrentTimeDirective;
import com.rekoe.web.freemarker.HtmlCutDirective;
import com.rekoe.web.freemarker.PaginationDirective;
import com.rekoe.web.freemarker.PermissionDirective;
import com.rekoe.web.freemarker.PermissionShiroFreemarker;
import com.rekoe.web.freemarker.ProcessTimeDirective;
import com.rekoe.web.freemarker.TimeFormatDirective;

import freemarker.template.SimpleHash;

@Fail(">>:/admin/common/unauthorized.rk")
@Encoding(input = "UTF-8", output = "UTF-8")
@Localization(value = "msg/", defaultLocalizationKey = "zh-CN")
@IocBean(create = "init")
@IocBy(args = { "*slog" })
public class CloudLauncher {

	@Inject
	private Dao dao;

	@Inject
	private AuthorityService authorityService;

	@Inject
	private FreeMarkerConfigurer freeMarkerConfigurer;

	@Inject
	private ProjectConfigService projectConfigService;

	@Inject
	private SimpleHash shiroTags;

	@Inject
	private PermissionShiroFreemarker permissionShiro;

	@Inject
	private ProcessTimeDirective process;

	@Inject
	private HtmlCutDirective htmlCut;

	@Inject
	private TimeFormatDirective timeFormat;

	@Inject
	private CurrentTimeDirective currentTime;

	@Inject
	private PaginationDirective pagination;

	@Inject
	private PermissionDirective permission;

	@IocBean(name = "shiroTags")
	@SuppressWarnings("deprecation")
	public SimpleHash createShiroTags() {
		return new SimpleHash(new HashMap<String, Object>()) {
			private static final long serialVersionUID = -2531751737433483659L;
			{
				put("authenticated", new AuthenticatedTag());
				put("guest", new GuestTag());
				put("hasAnyRoles", new HasAnyRolesTag());
				put("hasPermission", new HasPermissionTag());
				put("hasAnyPermission", new HasAnyPermissionTag());
				put("hasRole", new HasRoleTag());
				put("lacksPermission", new LacksPermissionTag());
				put("lacksRole", new LacksRoleTag());
				put("notAuthenticated", new NotAuthenticatedTag());
				put("principal", new PrincipalTag());
				put("user", new UserTag());
			}
		};
	}

	@IocBean(name = "permissionShiro")
	public PermissionShiroFreemarker createPermissionResolver(@Inject PermissionResolver permissionResolver) {
		return new com.rekoe.web.freemarker.PermissionShiroFreemarker(permissionResolver);
	}

	@IocBean(name = "permissionResolver")
	public PermissionResolver createPermissionResolver() {
		return new org.apache.shiro.authz.permission.WildcardPermissionResolver();
	}

	@Inject
	private DaoCacheInterceptor daoCacheInterceptor;

	public void init() {
		freeMarkerConfigurer.getConfiguration().setAutoImports(new HashMap<String, String>(2) {
			private static final long serialVersionUID = 7208484815721559298L;
			{
				put("p", "/ftl/pony/index.ftl");
				put("s", "/ftl/spring.ftl");
			}
		});

		freeMarkerConfigurer.addTags(new HashMap<String, Object>() {
			private static final long serialVersionUID = 2819227381581642466L;
			{
				put("shiro", shiroTags);
				put("perm_chow", permissionShiro);
				put("cms_perm", permission);
				put("process_time", process);
				put("pagination", pagination);
				put("htmlCut", htmlCut);
				put("timeFormat", timeFormat);
				put("currentTime", currentTime);
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

		List<Class<?>> clazzList = Scans.me().scanPackage("com.rekoe.domain");
		Lang.each(clazzList, new Each<Class<?>>() {
			@Override
			public void invoke(int index, Class<?> clazz, int length) throws ExitLoop, ContinueLoop, LoopException {
				Table table = clazz.getAnnotation(Table.class);
				if (Lang.isNotEmpty(table)) {
					String name = table.value();
					daoCacheInterceptor.addCachedTableName(StringUtils.trim(name));
				}
			}
		});
	}

	public static void main(String[] args) throws Exception {
		new NbApp().setPrintProcDoc(true).run();
	}
}
