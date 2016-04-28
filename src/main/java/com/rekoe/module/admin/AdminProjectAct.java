package com.rekoe.module.admin;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.rekoe.annotation.PermissionTag;
import com.rekoe.common.Message;
import com.rekoe.common.page.Pagination;
import com.rekoe.domain.Pj;
import com.rekoe.domain.PjAuth;
import com.rekoe.domain.ProjectConfig;
import com.rekoe.module.BaseAction;
import com.rekoe.service.DefaultTreeService;
import com.rekoe.service.ProjectAuthService;
import com.rekoe.service.ProjectConfigService;
import com.rekoe.service.ProjectGroupService;
import com.rekoe.service.ProjectService;
import com.rekoe.service.RepositoryService;
import com.rekoe.service.UsrService;
import com.rekoe.utils.CommonUtils;
import com.rekoe.utils.DoCommit;

@IocBean
@At("/admin/project")
public class AdminProjectAct extends BaseAction {

	@Inject
	private ProjectService projectService;

	@At
	@Ok("fm:template.admin.project.list")
	@RequiresPermissions({ "svn.project:view" })
	@PermissionTag(name = "SVN浏览项目", tag = "SVN项目管理")
	public Pagination list(@Param(value = "pageNumber", df = "1") int page) {
		return projectService.getObjListByPager(page, 20, null);
	}

	@At
	@Ok("fm:template.admin.project.add")
	@RequiresPermissions({ "svn.project:add" })
	@PermissionTag(name = "SVN添加项目", tag = "SVN项目管理", enable = true)
	public void add() {
	}

	@At
	@Ok("fm:template.admin.project.edit")
	@RequiresPermissions({ "svn.project:edit" })
	@PermissionTag(name = "SVN编辑项目", tag = "SVN项目管理", enable = false)
	public Pj edit(@Param("pj") String pj) {
		return projectService.get(pj);
	}

	@At
	@Ok("json")
	@RequiresPermissions("svn.project:add")
	@PermissionTag(name = "SVN添加项目", tag = "SVN项目管理", enable = false)
	public Message o_save(@Param("::pj.") Pj pj, HttpServletRequest req) {
		boolean isOk = projectService.nameOk(pj.getPj());
		if (isOk) {
			projectService.save(pj);
			return Message.success("ok", req);
		}
		return Message.error("error", req);
	}

	@Inject
	private RepositoryService repositoryService;

	@At
	@Ok("fm:template.admin.project.rep")
	@RequiresPermissions({ "svn.project:view" })
	public String rep(@Param("pj") String pj, HttpServletRequest req) {
		Pj project = projectService.get(pj);
		String root = repositoryService.getRepositoryRoot(project);
		String svnUrl = RepositoryService.parseURL(projectConfigService.getProjectUrl(pj));
		String path = "/";
		if (root != null) {
			try {
				root = URLDecoder.decode(root, "UTF-8");// @see issue 34
			} catch (UnsupportedEncodingException e) {
			}
			if (svnUrl.indexOf(root) != -1) {
				path = StringUtils.substringAfter(svnUrl, root);
				if (!path.startsWith("/")) {
					path = "/" + path;
				}
			}
		} else {
			root = svnUrl;
		}
		req.setAttribute("root", root);
		req.setAttribute("path", path);
		req.setAttribute("pj", pj);
		return pj;
	}

	@Inject
	private ProjectAuthService projectAuthService;

	@Inject
	private ProjectGroupService projectGroupService;

	@Inject
	private UsrService usrService;

	@At
	@Ok("json")
	@RequiresPermissions({ "svn.project:edit" })
	public Message init(@Param("pj") String pj, HttpServletRequest req) {
		Pj project = projectService.get(pj);
		if (project.isInitTempl()) {
			return Message.success("ok", req);
		}
		boolean isRight = repositoryService.createDir(project);
		if (isRight) {
			return Message.success("ok", req);
		}
		return Message.error("error", req);
	}

	@At
	@Ok("fm:template.admin.project.pjauth")
	@RequiresPermissions({ "svn.project:view" })
	public String pjauth(@Param("pj") String pj, @Param("res") String res, @Param("entity") PjAuth entity, HttpServletRequest req) {
		if (entity == null) {
			entity = new PjAuth();
			entity.setPj(pj);
			entity.setRes("");
			entity.setRw("");
		}
		if (StringUtils.isBlank(res)) {
			String path = req.getParameter("path");
			if (StringUtils.isNotBlank(path)) {
				res = this.projectAuthService.formatRes(pj, path);
			}
		} else {
			res = entity.getRes();
		}
		String root = projectConfigService.get().getRepositoryPath() + "/";
		res = StringUtils.remove(res, root);
		entity.setRes(res);
		List<PjAuth> list = projectAuthService.list(pj, res);
		req.setAttribute("list", list);
		req.setAttribute("pj", pj);
		req.setAttribute("pjreslist", projectAuthService.getResList(pj));
		req.setAttribute("pjgrlist", projectGroupService.getList(pj));
		req.setAttribute("usrList", usrService.getList());
		req.setAttribute("entity", entity);
		return pj;
	}

	@Inject
	private DefaultTreeService treeService;

	@At
	@Ok("raw")
	@RequiresPermissions({ "svn.project:view" })
	public String ajaxTreeService(HttpServletRequest req, HttpServletResponse response) {
		NutMap params = CommonUtils.getRequestParametersMap(req);
		com.rekoe.domain.Ajax ajax = treeService.execute(params);
		if (ajax != null) {
			return ajax.getResult();
		}
		return "";
	}

	@At
	@Ok("json")
	@RequiresPermissions({ "svn.project:auth.manager" })
	@PermissionTag(name = "管理项目权限", tag = "SVN项目管理", enable = false)
	public Message delete(@Param("id") String id, HttpServletRequest req) {
		try {
			projectService.delete(id);
		} catch (Exception e) {
			return Message.error(e.getMessage(), req);
		}
		return Message.success("ok", req);
	}

	@At("/pjauth/delete")
	@Ok("fm:template.admin.project.pjauth")
	@RequiresPermissions({ "svn.project:auth.manager" })
	@PermissionTag(name = "管理项目权限", tag = "SVN项目管理", enable = true)
	public String pjauth_delete(@Param("gr") String gr, @Param("pj") String pj, @Param("res") String res, @Param("usr") String usr) {
		if (StringUtils.isNotBlank(gr)) {
			projectAuthService.deleteByGr(pj, gr, res);
		} else if (StringUtils.isNotBlank(usr)) {
			projectAuthService.deleteByUsr(pj, usr, res);
		}
		return pjauth(pj, res, null, Mvcs.getReq());
	}

	@Inject
	private ProjectConfigService projectConfigService;

	@At
	@Ok("fm:template.admin.project.config")
	@RequiresPermissions({ "svn.project:conf" })
	@PermissionTag(name = "配置管理", tag = "SVN项目管理", enable = true)
	public ProjectConfig conf() {
		return projectConfigService.get();
	}

	@At("/conf/update")
	@Ok("json")
	@RequiresPermissions({ "svn.project:conf" })
	@PermissionTag(name = "配置管理", tag = "SVN项目管理", enable = false)
	public Message conf_update(@Param("::conf.") ProjectConfig conf, HttpServletRequest req) {
		boolean isRight = projectConfigService.update(conf);
		if (isRight) {
			return Message.success("ok", req);
		}
		return Message.error("erroe", req);
	}

	@At
	@Ok("json")
	@RequiresPermissions("svn.project:update")
	@PermissionTag(name = "SVN编辑项目", tag = "SVN项目管理", enable = true)
	public Message o_update(@Param("::pj.") Pj pj, HttpServletRequest req) {
		projectService.update(pj);
		return Message.success("ok", req);
	}

	@At("/pjauth/save")
	@Ok("fm:template.admin.project.pjauth")
	@RequiresPermissions({ "svn.project:auth.manager" })
	@PermissionTag(name = "管理项目权限", tag = "SVN项目管理", enable = false)
	public String pjauth_save(@Param("rw") String rw, @Param("grs") String[] grs, @Param("pj") String pj, @Param("res") String res, @Param("usrs") String[] usrs) {
		PjAuth entity = new PjAuth();
		entity.setPj(pj);
		entity.setRes(res);
		entity.setRw(rw);
		projectAuthService.save(pj, res, rw, grs, usrs);
		return pjauth(pj, res, entity, Mvcs.getReq());
	}
	
	@At
	@Ok("fm:template.admin.project.add_dir")
	@RequiresPermissions("svn.project:update")
	@PermissionTag(name = "SVN编辑项目", tag = "SVN项目管理", enable = false)
	public String file_add(@Param("pj") String pj) {
		return pj;
	}
	
	@Inject
	private DoCommit doCommit;
	
	@At
	@Ok("json")
	@RequiresPermissions("svn.project:update")
	@PermissionTag(name = "SVN编辑项目", tag = "SVN项目管理", enable = false)
	public Message file_save(@Param("pj") String pj,@Param("file") String file, HttpServletRequest req) {
		doCommit.mkdirs(pj, new String[]{file});
		return Message.success("ok", req);
	}
}
