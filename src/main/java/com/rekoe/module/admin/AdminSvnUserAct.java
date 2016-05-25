package com.rekoe.module.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.aop.interceptor.async.Async;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.random.R;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Attr;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.rekoe.annotation.PermissionTag;
import com.rekoe.common.Message;
import com.rekoe.common.page.Pagination;
import com.rekoe.domain.Pj;
import com.rekoe.domain.ProjectConfig;
import com.rekoe.domain.Usr;
import com.rekoe.module.BaseAction;
import com.rekoe.service.EmailService;
import com.rekoe.service.ProjectConfigService;
import com.rekoe.service.ProjectService;
import com.rekoe.service.SvnService;
import com.rekoe.service.SvnUserService;
import com.rekoe.utils.EncryptUtil;

@IocBean
@At("/admin/svn/user")
public class AdminSvnUserAct extends BaseAction {

	private final static Log log = Logs.get();

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
	@Ok("fm:template.admin.svn_user.edit")
	@RequiresPermissions({ "svn.user:edit" })
	@PermissionTag(name = "编辑SVN账号", tag = "SVN账号管理", enable = true)
	public Usr edit(@Param("id") String usr) {
		return svnUserService.get(usr);
	}

	@At
	@Ok("json")
	@RequiresPermissions("svn.user:add")
	@PermissionTag(name = "SVN添加账号", tag = "SVN账号管理", enable = false)
	public Message o_save(@Param("::user.") Usr user, HttpServletRequest req) {
		boolean isOk = svnUserService.nameOk(user.getUsr());
		boolean isEmail = Strings.isEmail(user.getEmail());
		if (isOk && isEmail) {
			user.setPsw(EncryptUtil.encrypt(R.UU64().substring(0, 10)));
			isOk = svnUserService.insert(user);
		} else {
			isOk = false;
		}
		if (isOk) {
			return Message.success("ok", req);
		}
		if (!isEmail) {
			return Message.error("email error", req);
		}
		return Message.error("error", req);
	}

	@At
	@Ok("json")
	@RequiresPermissions("svn.user:edit")
	@PermissionTag(name = "编辑SVN账号", tag = "SVN账号管理", enable = false)
	public Message o_update(@Param("pwd") String pwd, @Param("usr") String usr, @Param("role") String role, HttpServletRequest req) {
		if (StringUtils.isBlank(usr)) {
			return Message.error("error", req);
		}
		Chain chain = Chain.make("role", role);
		if (StringUtils.isNotBlank(pwd)) {
			chain.add("psw", EncryptUtil.encrypt(pwd));
		}
		boolean isOk = svnUserService.update(chain, Cnd.where("usr", "=", usr)) > 0;
		if (isOk) {
			List<Pj> list = svnUserService.getPjList(usr);
			if (list != null) {
				for (Pj pj : list) {
					try {
						this.svnService.exportConfig(pj);
					} catch (Exception e) {
						log.errorf("project %s ,error %s", pj.getPj(), e.getMessage());
					}
				}
			}
			return Message.success("ok", req);
		}
		return Message.error("error", req);
	}

	@Inject
	private EmailService emailService;

	@Inject
	private ProjectConfigService projectConfigService;

	@Inject
	private SvnService svnService;

	@Inject
	private ProjectService projectService;

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
	public Message restpwd(@Param("usr") String usr, @Attr("usr") Usr manager, HttpServletRequest req) {
		Usr user = svnUserService.fetch(Cnd.where("usr", "=", usr));
		if (user == null) {
			return Message.error("error.account.empty", req);
		}
		String code = RandomStringUtils.random(7, RANDOM_ARRY_CHAR);
		svnUserService.update(Chain.make("psw", EncryptUtil.encrypt(code)), Cnd.where("usr", "=", usr));
		if (usr.equals(manager.getUsr())) {
			req.getSession().setAttribute("usr", svnUserService.fetch(Cnd.where("usr", "=", usr)));
		}
		List<Pj> list = svnUserService.getPjList(usr);
		if (list != null) {
			for (Pj pj : list) {
				try {
					this.svnService.exportConfig(pj);
				} catch (Exception e) {
					// projectService.deleteDB(pj.getPj());
					log.errorf("project %s ,error %s", pj.getPj(), e.getMessage());
				}
			}
		}
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
