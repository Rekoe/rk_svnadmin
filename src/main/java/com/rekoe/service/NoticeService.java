package com.rekoe.service;

import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.dao.FieldFilter;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.util.Daos;
import org.nutz.ioc.loader.annotation.IocBean;

import com.rekoe.common.page.Pagination;
import com.rekoe.domain.Notice;

/**
 * @author 科技㊣²º¹³<br />
 *         2014年2月3日 下午4:48:45<br />
 *         http://www.rekoe.com<br />
 *         QQ:5382211
 */
@IocBean(fields = { "dao" })
public class NoticeService extends BaseService<Notice> {
	public NoticeService() {
		super();
	}

	public NoticeService(Dao dao) {
		super(dao);
	}

	public List<Notice> list() {
		return query(null, null);
	}

	public List<Notice> getIndexNewList(int limit, String desc) {
		return getListByCnd(Cnd.NEW().limit(1, limit).desc(desc));
	}

	public List<Notice> getListByCnd(Condition cnd) {
		return dao().query(getEntityClass(), cnd);
	}

	public void update(final Notice art) {
		Daos.ext(dao(), FieldFilter.create(Notice.class, null, "^(createDate)$", true)).update(art);
	}

	public Pagination getObjListByPager(int pageNumber, String keyWorld) {
		Pager pager = dao().createPager(pageNumber, 10);
		Cnd cnd = Cnd.where("title", "like", "%" + keyWorld + "%");
		List<Notice> list = dao().query(getEntityClass(), cnd, pager);
		pager.setRecordCount(dao().count(getEntityClass(), cnd));
		Pagination pagination = new Pagination(pageNumber, 10, pager.getRecordCount(), list);
		return pagination;
	}
}
