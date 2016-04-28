package com.rekoe.module.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.aop.interceptor.async.Async;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.rekoe.annotation.PermissionTag;
import com.rekoe.common.Message;
import com.rekoe.common.page.Pagination;
import com.rekoe.domain.Pj;
import com.rekoe.domain.PjGrUsr;
import com.rekoe.domain.ProjectConfig;
import com.rekoe.domain.Usr;
import com.rekoe.module.BaseAction;
import com.rekoe.service.EmailService;
import com.rekoe.service.ProjectConfigService;
import com.rekoe.service.ProjectGroupUsrService;
import com.rekoe.service.ProjectService;
import com.rekoe.service.SvnService;
import com.rekoe.service.SvnUserService;
import com.rekoe.utils.EncryptUtil;

@IocBean
@At("/admin/project/group/usr")
public class AdminProjectGroupUsrAct extends BaseAction {

	@Inject
	private ProjectGroupUsrService projectGroupUsrService;

	@Inject
	private SvnService svnService;

	@At
	@Ok("fm:template.admin.project_group_usr.list")
	@RequiresPermissions({ "project.group:view" })
	@PermissionTag(name = "SVN浏览账号", tag = "SVN账号管理")
	public Pagination list(@Param(value = "pageNumber", df = "1") int page, @Param("pj") String pj, @Param("gr") String gr, HttpServletRequest req) {
		req.setAttribute("pj", pj);
		req.setAttribute("gr", gr);
		return projectGroupUsrService.getList(pj, gr, page);
	}

	@Inject
	private SvnUserService svnUserService;

	@At
	@Ok("fm:template.admin.project_group_usr.add")
	@RequiresPermissions({ "project.group:add" })
	@PermissionTag(name = "添加项目组用户", tag = "SVN账号管理", enable = true)
	public String add(@Param("pj") String pj, @Param("gr") String gr, HttpServletRequest req) {
		req.setAttribute("pj", pj);
		req.setAttribute("gr", gr);
		req.setAttribute("usrList", svnUserService.listUnSelected(pj, gr));
		return pj;
	}

	@At
	@Ok("json")
	@RequiresPermissions("project.group:add")
	@PermissionTag(name = "添加项目组用户", tag = "SVN账号管理", enable = false)
	public Message o_save(@Param("pj") String pj, @Param("gr") String gr, @Param("usrs") String[] usrs, HttpServletRequest req) {
		if (usrs == null || usrs.length == 0) {
			return Message.error("error", req);
		}
		for (String usr : usrs) {
			if (StringUtils.isBlank(usr)) {
				continue;
			}
			PjGrUsr pjGrUsr = new PjGrUsr();
			pjGrUsr.setPj(pj);
			pjGrUsr.setGr(gr);
			pjGrUsr.setUsr(usr);
			projectGroupUsrService.save(pjGrUsr);
		}
		// export
		svnService.exportConfig(pj);
		return Message.success("ok", req);
	}

	@At
	@Ok("json")
	@RequiresPermissions("project.group:delete")
	@PermissionTag(name = "删除项目组用户", tag = "SVN账号管理", enable = true)
	public Message delete(@Param("pj") String pj, @Param("gr") String gr, @Param("usr") String usr, HttpServletRequest req) {
		projectGroupUsrService.delete(pj, gr, usr);
		svnService.exportConfig(pj);
		return Message.success("ok", req);
	}

	@Inject
	private EmailService emailService;

	@Inject
	private ProjectService projectService;

	@Inject
	private ProjectConfigService projectConfigService;

	@At
	@Ok("json")
	@RequiresPermissions("project.group:add")
	@PermissionTag(name = "添加项目组用户", tag = "SVN账号管理", enable = false)
	public Message all_email(@Param("pj") String pj, HttpServletRequest req) {
		Pj project = projectService.get(pj);
		ProjectConfig conf = projectConfigService.get();
		List<Usr> getList = svnUserService.listSelected(pj);
		sendProjectOpenEmail(project, conf, getList, emailService);
		return Message.success("ok", req);
	}

	@At
	@Ok("json")
	@RequiresPermissions("project.group:add")
	@PermissionTag(name = "添加项目组用户", tag = "SVN账号管理", enable = false)
	public Message email(@Param("pj") String pj, @Param("usr") String usr, HttpServletRequest req) {
		Pj project = projectService.get(pj);
		ProjectConfig conf = projectConfigService.get();
		sendProjectOpenEmail(project, conf, svnUserService.get(usr), emailService);
		return Message.success("ok", req);
	}

	@Async
	public void sendProjectOpenEmail(Pj project, ProjectConfig conf, List<Usr> getList, EmailService emailService) {
		for (Usr usr : getList) {
			Map<String, Object> root = new HashMap<>();
			root.put("usr", usr.getUsr());
			root.put("name", usr.getName());
			root.put("pwd", EncryptUtil.decrypt(usr.getPsw()));
			root.put("project", project.getDes());
			root.put("url", conf.getDomainPath() + project.getPj());
			emailService.projectOpen(usr.getEmail(), root);
		}
	}

	@Async
	public void sendProjectOpenEmail(Pj project, ProjectConfig conf, Usr usr, EmailService emailService) {
		Map<String, Object> root = new HashMap<>();
		root.put("usr", usr.getUsr());
		root.put("name", usr.getName());
		root.put("pwd", EncryptUtil.decrypt(usr.getPsw()));
		root.put("project", project.getDes());
		root.put("url", conf.getDomainPath() + project.getPj());
		emailService.projectOpen(usr.getEmail(), root);
	}
}
