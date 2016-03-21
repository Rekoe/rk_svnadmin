package com.rekoe.module.admin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.entity.Record;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.rekoe.annotation.PermissionTag;
import com.rekoe.common.Message;
import com.rekoe.common.page.Pagination;
import com.rekoe.domain.CdkeyCategory;
import com.rekoe.module.BaseAction;
import com.rekoe.service.CdKeyCategoryService;
import com.rekoe.service.CdKeyService;

@IocBean
@At("/admin/cdkey/")
public class AdminCdKeyCategoryAct extends BaseAction {

	@Inject
	private CdKeyCategoryService cdKeyCategoryService;
	@Inject
	private CdKeyService cdKeyService;

	@At("/category/list")
	@Ok("fm:template.admin.cdkey.category_list")
	@RequiresPermissions("system.cdkey:category.view")
	@PermissionTag(name = "分类浏览", tag = "CDK分类", enable = true)
	public Pagination category_list(@Param(value = "pageNumber", df = "1") int pageNumber) {
		return cdKeyCategoryService.getCategoryListByPager(pageNumber);
	}

	@At("/category/add")
	@Ok("fm:template.admin.cdkey.category_add")
	@RequiresPermissions("system.cdkey:category.add")
	@PermissionTag(name = "添加分类", tag = "CDK分类", enable = true)
	public void category_add() {
	}

	@At("/category/save")
	@Ok("json")
	@RequiresPermissions("system.cdkey:category.add")
	@PermissionTag(name = "添加分类", tag = "CDK分类", enable = false)
	public Message category_save(@Param("name") String name, @Param("type") int type, HttpServletRequest req) {
		if (cdKeyCategoryService.isNotExists(type)) {
			cdKeyCategoryService.insert(new CdkeyCategory(name, type));
			return Message.success("ok", req);
		}
		return Message.error("error.type.exists", req);
	}

	@At("/category/edit")
	@Ok("fm:template.admin.cdkey.category_edit")
	@RequiresPermissions("system.cdkey:category.edit")
	@PermissionTag(name = "编辑分类", tag = "CDK分类", enable = true)
	public CdkeyCategory category_edit(@Param("id") String id) {
		return cdKeyCategoryService.fetchByID(id);
	}

	@At("/category/update")
	@Ok("json")
	@RequiresPermissions("system.cdkey:category.edit")
	@PermissionTag(name = "编辑分类", tag = "CDK分类", enable = false)
	public Message category_update(@Param("::cc.") CdkeyCategory cc, HttpServletRequest req) {
		cdKeyCategoryService.update(cc);
		return Message.success("ok", req);
	}

	@At
	@Ok("fm:template.admin.cdkey.add")
	@RequiresPermissions("system.cdkey:add")
	@PermissionTag(name = "添加CDK", tag = "CDK", enable = true)
	public String add(@Param("type") String type, HttpServletRequest req) {
		req.setAttribute("rule", type.length());
		return type;
	}

	@At
	@Ok("json")
	@RequiresPermissions("system.cdkey:add")
	@PermissionTag(name = "添加CDK", tag = "CDK", enable = false)
	public Message save(@Param("type") String taskid, @Param("num") int num, @Param("len") int len, HttpServletRequest req) {
		if ((taskid).length() > len) {
			return Message.error("长度不能小于" + taskid.length(), req);
		}
		cdKeyService.addCdkey(taskid, num, len);
		return Message.success("ok", req);
	}

	@At
	@Ok("down")
	@RequiresPermissions("system.cdkey:down")
	@PermissionTag(name = "下载CDK", tag = "CDK", enable = true)
	public ByteArrayOutputStream down(@Param("type") int type) throws IOException {
		List<Record> records = cdKeyService.searchDown(type, false);
		return loadDown(records);
	}
}
