package com.rekoe.service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.math.NumberUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.pager.Pager;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;

import com.rekoe.common.page.Pagination;
import com.rekoe.domain.CdkeyCategory;

@IocBean(fields = { "dao" })
public class CdKeyCategoryService extends BaseService<CdkeyCategory> {
	public List<CdkeyCategory> list() {
		List<CdkeyCategory> list = query(null, null);
		return list;
	}

	public boolean insert(CdkeyCategory cdkeycategory) {
		dao().insert(cdkeycategory);
		return true;
	}

	public CdkeyCategory fetchByID(String id) {
		return dao().fetch(getEntityClass(), Cnd.where("id", "=", id));
	}

	public CdkeyCategory view(String id) {
		return fetchByID(id);
	}

	public void update(CdkeyCategory cdkeycategory) {
		dao().update(cdkeycategory, "(name)$");
	}

	public boolean isNotExists(int type) {
		return Lang.isEmpty(dao().fetch(getEntityClass(), Cnd.where("cdkeyType", "=", type)));
	}

	protected int getPageNumber(Integer pageNumber) {
		return Lang.isEmpty(pageNumber) ? 1 : pageNumber;
	}

	public Pagination getCategoryListByPager(Integer pageNumber) {
		int pageSize = 20;
		pageNumber = getPageNumber(pageNumber);
		Pager pager = dao().createPager(pageNumber, pageSize);
		List<CdkeyCategory> list = dao().query(CdkeyCategory.class, null, pager);
		pager.setRecordCount(dao().count(CdkeyCategory.class, null));
		return new Pagination(pageNumber, pageSize, pager.getRecordCount(), list);
	}

	public boolean isExists(int type) {
		return !isNotExists(type);
	}

	private String getDigit(String text) {
		StringBuffer sb = new StringBuffer();
		Pattern p = Pattern.compile("(\\d+)");
		Matcher m = p.matcher(text);
		while (m.find()) {
			String find = m.group(1);
			sb.append(find);
		}
		return sb.toString();
	}

	public int getType(String cdk) {
		return NumberUtils.toInt(getDigit(cdk), -1);
	}

	public boolean isExists(String cdkey) {
		int type = NumberUtils.toInt(getDigit(cdkey), -1);
		return !isNotExists(type);
	}

}
