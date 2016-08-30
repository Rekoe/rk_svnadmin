package com.rekoe.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.dao.pager.Pager;
import org.nutz.lang.Lang;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.service.IdEntityService;

import com.rekoe.common.page.Pagination;

/**
 * @author 科技㊣²º¹³<br />
 *         2014年2月3日 下午4:48:45<br />
 *         http://www.rekoe.com<br />
 *         QQ:5382211
 */
public class BaseService<T> extends IdEntityService<T> {

	protected final static int DEFAULT_PAGE_NUMBER = 20;

	private final static Log log = Logs.get();

	public BaseService() {
		super();
	}

	public BaseService(Dao dao) {
		super(dao);
	}

	public Pagination getObjListByPager(Integer pageNumber, int pageSize, Condition cnd) {
		return getObjListByPager(dao(), pageNumber, pageSize, cnd);
	}

	public Pagination getObjListByPager(Dao dao, Integer pageNumber, int pageSize, Condition cnd) {
		pageNumber = getPageNumber(pageNumber);
		Pager pager = dao.createPager(pageNumber, pageSize);
		List<T> list = dao.query(getEntityClass(), cnd, pager);
		pager.setRecordCount(dao.count(getEntityClass(), cnd));
		return new Pagination(pageNumber, pageSize, pager.getRecordCount(), list);
	}

	public Pagination getObjListByPager(Integer pageNumber, Condition cnd) {
		return getObjListByPager(pageNumber, DEFAULT_PAGE_NUMBER, cnd);
	}

	protected int getPageNumber(Integer pageNumber) {
		return Lang.isEmpty(pageNumber) ? 1 : pageNumber;
	}

	public void delete(String[] ids) {
		dao().clear(getEntityClass(), Cnd.where("id", "in", ids));
	}

	public void delete(String id) {
		dao().clear(getEntityClass(), Cnd.where("id", "in", id));
	}

	public T insert(T t) {
		try {
			return dao().insert(t);
		} catch (Exception e) {
			log.error(e);
			return null;
		}
	}

	protected String getRandomStr(String str, String num) {
		int len = str.length();
		char[] numArr = num.toCharArray();
		Set<Integer> rr = getRandomIntArray(len, numArr.length);
		Iterator<Integer> iterator = rr.iterator();
		List<String> strList = new ArrayList<>();
		int i = 0;
		int last = 0;
		while (iterator.hasNext()) {
			int site = iterator.next();
			if (i == 0 && site == 0) {
				strList.add("");
			} else {
				strList.add(StringUtils.substring(str, last, site));
			}
			last = site;
		}
		StringBuffer sb = new StringBuffer();
		int ii = 0;
		for (String st : strList) {
			sb.append(st).append(numArr[ii]);
			ii++;
		}
		if (last < len) {
			sb.append(StringUtils.substring(str, last, str.length()));
		}
		return sb.toString();
	}

	protected Set<Integer> getRandomIntArray(int upLimit, int size) {
		Set<Integer> tempSet = new TreeSet<Integer>();
		if (upLimit <= size) {
			for (int i = 0; i < upLimit; i++) {
				tempSet.add(i);
			}
		} else {
			int pollNum = 0;
			while (tempSet.size() < size && pollNum < 25 * size) {
				Integer temp = getRandomInt(upLimit) - 1;
				if (temp < 0) {
					temp = 0;
				}
				if (!tempSet.contains(temp)) {
					tempSet.add(temp);
				}
				pollNum++;
			}
			return tempSet;
		}
		return tempSet;
	}

	private Random r = new Random();

	protected int getRandomInt(int upLimit) {
		if (upLimit <= 0) {
			return 0;
		} else {
			return r.nextInt(upLimit) + 1;
		}
	}

	/**
	 * 获得随机数字
	 * 
	 * @param size
	 * @return
	 */
	protected String getRandomIntStr(int size) {
		Set<Integer> ids = getRandomIntArray(9, size);
		StringBuffer sb = new StringBuffer();
		for (Integer id : ids) {
			sb.append(id);
		}
		return sb.toString();
	}
}
