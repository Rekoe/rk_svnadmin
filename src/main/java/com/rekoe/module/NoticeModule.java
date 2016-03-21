package com.rekoe.module;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;

import com.rekoe.domain.Notice;
import com.rekoe.filter.OauthCrossOriginFilter;
import com.rekoe.service.NoticeService;

@IocBean
@At("/notice")
@Filters(@By(type = OauthCrossOriginFilter.class))
public class NoticeModule {

	@Inject
	private NoticeService noticeService;

	/**
	 * @api {post} /notice/:pid/list 获得公告列表
	 * @apiParam {Number} pid 申请的游戏编码ID
	 * @apiGroup notice
	 * @apiVersion 1.0.0
	 * @apiSampleRequest http://warlogin.shanggame.com/notice/:pid/list
	 * @apiSuccess {Object[]}  notices      List of notice.
	 * @apiSuccess {String} notices.color 公告标题颜色.
	 * @apiSuccess {String} notices.title 公告标题.
	 * @apiSuccess {number} notices.id 公告ID.
	 * @apiSuccessExample {json} Success-Response:
	 *  [
	 *  	{
	 *    		"color": "#FFFFF", "title": "xxxx" ,"id":1
	 *  	}
	 *  ]
	 */

	@At("/?/list")
	@Ok("json")
	public Object list(int pid) {
		List<Notice> list = noticeService.query(Cnd.where("pid", "=", pid).and("publication", "=", true).desc("top"), null);
		if (Lang.isEmpty(list)) {
			return new ArrayList<Notice>();
		}
		return list;
	}

	/**
	 * @api {post} /notice/view/:id 公告内容详细
	 *
	 * @apiParam {Number} id ID.
	 * 
	 * @apiGroup notice
	 * @apiVersion 1.0.0
	 * @apiSampleRequest http://warlogin.shanggame.com/notice/view/:id
	 */

	@At("/view/?")
	@Ok("raw")
	public String view(long id,HttpServletRequest req) throws IOException {
		Notice article =noticeService.fetch(id);
		if (Lang.isEmpty(article)) {
			return "empty";
		}
		return article.getContent();
	}

}
