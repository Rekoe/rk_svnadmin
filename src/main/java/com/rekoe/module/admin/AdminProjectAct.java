package com.rekoe.module.admin;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.rekoe.annotation.PermissionTag;
import com.rekoe.common.Message;
import com.rekoe.common.page.Pagination;
import com.rekoe.domain.Pj;
import com.rekoe.domain.PjAuth;
import com.rekoe.module.BaseAction;
import com.rekoe.service.ProjectAuthService;
import com.rekoe.service.ProjectGroupService;
import com.rekoe.service.ProjectService;
import com.rekoe.service.RepositoryService;
import com.rekoe.service.UsrService;
import com.rekoe.utils.UsrProvider;

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
		System.out.println(UsrProvider.getCurrentUsr());
		return projectService.getObjListByPager(page, 20, null);
	}

	@At
	@Ok("fm:template.admin.project.add")
	@RequiresPermissions({ "svn.project:add" })
	@PermissionTag(name = "SVN添加项目", tag = "SVN项目管理", enable = true)
	public void add() {
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
		Pj project = projectService.fetch(Cnd.where("pj", "=", pj));
		String root = repositoryService.getRepositoryRoot(project);
		String svnUrl = RepositoryService.parseURL(project.getUrl());
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
	@Ok("fm:template.admin.project.pjauth")
	@RequiresPermissions({ "svn.project:view" })
	public String pjauth(@Param("pj") String pj, @Param("res") String res, HttpServletRequest req) {
		PjAuth entity = (PjAuth) req.getAttribute("entity");
		if (entity == null) {
			entity = new PjAuth();
			entity.setPj(pj);
			entity.setRes("");
			entity.setRw("");
			req.setAttribute("entity", entity);
		}
		if (StringUtils.isBlank(res)) {
			String path = req.getParameter("path");
			if (StringUtils.isNotBlank(path)) {
				res = this.projectAuthService.formatRes(pj, path);
			}
		} else {
			res = entity.getRes();
		}
		entity.setRes(res);
		List<PjAuth> list = projectAuthService.list(pj, res);
		req.setAttribute("list", list);
		req.setAttribute("pj", pj);
		req.setAttribute("pjreslist", projectAuthService.getResList(pj));
		req.setAttribute("pjgrlist",projectGroupService.getList(pj));
		req.setAttribute("usrList", usrService.getList());
		return pj;
	}
}
