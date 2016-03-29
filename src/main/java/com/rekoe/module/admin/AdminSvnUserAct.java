package com.rekoe.module.admin;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.aop.interceptor.async.Async;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.rekoe.annotation.PermissionTag;
import com.rekoe.common.Message;
import com.rekoe.common.page.Pagination;
import com.rekoe.domain.ProjectConfig;
import com.rekoe.domain.Usr;
import com.rekoe.module.BaseAction;
import com.rekoe.service.EmailService;
import com.rekoe.service.ProjectConfigService;
import com.rekoe.service.SvnUserService;
import com.rekoe.utils.EncryptUtil;

@IocBean
@At("/admin/svn/user")
public class AdminSvnUserAct extends BaseAction {

	private static final char[] RANDOM_ARRY_CHAR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

	@Inject
	private SvnUserService svnUserService;

	@At
	@Ok("fm:template.admin.svn_user.list")
	@RequiresPermissions({ "svn.user:view" })
	@PermissionTag(name = "SVN浏览账号", tag = "SVN账号管理")
	public Pagination list(@Param(value = "pageNumber", df = "1") int page) {
		return svnUserService.getObjListByPager(page, 20, Cnd.where("usr", "<>", "*"));
	}

	@At
	@Ok("fm:template.admin.svn_user.add")
	@RequiresPermissions({ "svn.user:add" })
	@PermissionTag(name = "SVN添加账号", tag = "SVN账号管理", enable = true)
	public void add() {
	}

	@At
	@Ok("json")
	@RequiresPermissions("svn.user:add")
	@PermissionTag(name = "SVN添加账号", tag = "SVN账号管理", enable = false)
	public Message o_save(@Param("::user.") Usr user, HttpServletRequest req) {
		boolean isOk = svnUserService.nameOk(user.getUsr());
		if (isOk) {
			isOk = svnUserService.insert(user);
		}
		if (isOk) {
			return Message.success("ok", req);
		}
		return Message.error("error", req);
	}

	@Inject
	private EmailService emailService;

	@Inject
	private ProjectConfigService projectConfigService;

	/**
	 * 重置账号密码
	 * 
	 * @param usr
	 * @param req
	 * @return
	 */
	@At
	@Ok("json")
	@RequiresPermissions("svn.user:add")
	@PermissionTag(name = "SVN添加账号", tag = "SVN账号管理", enable = false)
	public Message restpwd(@Param("usr") String usr, HttpServletRequest req) {
		Usr user = svnUserService.fetch(Cnd.where("usr", "=", usr));
		if (user == null) {
			return Message.error("error.account.empty", req);
		}
		String code = RandomStringUtils.random(7, RANDOM_ARRY_CHAR);
		svnUserService.update(Chain.make("psw", EncryptUtil.encrypt(code)), Cnd.where("usr", "=", usr));
		ProjectConfig conf = projectConfigService.get();
		emailNotify(user, emailService, conf, user.getEmail(), code);
		return Message.success("ok", req);
	}

	@Async
	private void emailNotify(Usr user, EmailService emailService, ProjectConfig conf, String to, String pwd) {
		if (conf.isEmailNotify() && Strings.isEmail(to)) {
			Map<String, Object> root = new HashMap<String, Object>();
			root.put("name", user.getName());
			root.put("pwd", pwd);
			root.put("usr", user.getUsr());
			emailService.restpwd(to, root);
		}
	}
}
