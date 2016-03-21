package com.rekoe.module.admin;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Files;
import org.nutz.lang.Times;
import org.nutz.lang.random.R;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.upload.TempFile;
import org.nutz.mvc.upload.UploadAdaptor;
import org.nutz.mvc.view.HttpStatusView;

import com.rekoe.annotation.PermissionTag;
import com.rekoe.common.Message;
import com.rekoe.common.page.Pagination;
import com.rekoe.domain.GameServer;
import com.rekoe.domain.Notice;
import com.rekoe.service.GameServerService;
import com.rekoe.service.NoticeService;

/**
 * @author 科技㊣²º¹³<br />
 *         2014年2月3日 下午4:48:45<br />
 *         http://www.rekoe.com<br />
 *         QQ:5382211 <br />
 *         公告系统
 */
@IocBean
@At("/admin/notice")
public class AdminNoticeAct {

	@Inject
	private NoticeService noticeService;

	@Inject
	private GameServerService gameServerService;

	@At
	@Ok("fm:template.admin.notice.list")
	@RequiresPermissions("system.notice")
	@PermissionTag(name = "浏览公告", tag = "游戏公告", enable = false)
	public Pagination list(@Param(value = "pageNumber", df = "1") int pageNumber) {
		return noticeService.getObjListByPager(pageNumber, 20, null);
	}

	@At
	@Ok("fm:template.admin.notice.add")
	@RequiresPermissions("system.notice:add")
	@PermissionTag(name = "添加公告", tag = "游戏公告", enable = true)
	public List<GameServer> add() {
		return gameServerService.list();
	}

	@At
	@Ok("json")
	@RequiresPermissions("system.notice:add")
	@PermissionTag(name = "添加公告", tag = "游戏公告", enable = false)
	public Message save(@Param("::notice.") Notice notice, HttpServletRequest req) {
		Date now = Times.now();
		notice.setCreateDate(now);
		notice.setModifyDate(now);
		noticeService.insert(notice);
		return Message.success("button.submit.success", req);
	}

	@At
	@Ok("fm:template.admin.notice.edit")
	@RequiresPermissions("system.notice:edit")
	@PermissionTag(name = "编辑公告", tag = "游戏公告", enable = true)
	public List<GameServer> edit(long id, HttpServletRequest req) {
		Notice art = noticeService.fetch(id);
		req.setAttribute("notice", art);
		return gameServerService.list();
	}

	@At
	@Ok("json")
	@RequiresPermissions("system.notice:edit")
	@PermissionTag(name = "编辑公告", tag = "游戏公告", enable = false)
	public Message update(@Param("::notice.") Notice notice, HttpServletRequest req) {
		notice.setModifyDate(Times.now());
		noticeService.update(notice);
		return Message.success("button.submit.success", req);
	}

	@At
	@Ok("json")
	@RequiresPermissions("system.notice:delete")
	@PermissionTag(name = "删除公告", tag = "游戏公告", enable = true)
	public Message delete(@Param("id") long id, HttpServletRequest req) {
		noticeService.delete(id);
		return Message.success("button.submit.success", req);
	}

	@Inject("java:$conf.get('topic.image.dir')")
	protected String imageDir;
	private final static String NEWS_FILE_PATH = "/upload/notice/";

	@SuppressWarnings("deprecation")
	@AdaptBy(type = UploadAdaptor.class, args = { "${app.root}/WEB-INF/tmp2" })
	@POST
	@At
	@Ok("json")
	@RequiresUser
	public Object upload(@Param("file") TempFile tmp, HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("application/json");
		NutMap jsonrpc = new NutMap();
		if (tmp == null || tmp.getSize() == 0) {
			return jsonrpc.setv("msg", "空文件");
		}
		if (tmp.getSize() > 2 * 1024 * 1024) {
			return jsonrpc.setv("msg", "文件太大了");
		}
		String id = R.UU32();
		String path = "/" + id.substring(0, 2) + "/" + id.substring(2);
		File f = new File(imageDir + NEWS_FILE_PATH + path);
		Files.createNewFile(f);
		Files.copyFile(tmp.getFile(), f);
		jsonrpc.setv("url", req.getRequestURI() + path);
		jsonrpc.setv("success", true);
		return jsonrpc;
	}

	@Ok("raw:jpg")
	@At("/upload/?/?")
	@Fail("http:404")
	public Object image(String p, String p2) throws IOException {
		if ((p + p2).contains("."))
			return HttpStatusView.HTTP_404;
		File f = new File(imageDir + NEWS_FILE_PATH, p + "/" + p2);
		return f;
	}
}
