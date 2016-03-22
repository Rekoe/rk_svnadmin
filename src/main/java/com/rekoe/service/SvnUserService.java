package com.rekoe.service;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;

import com.rekoe.domain.Usr;

/**
 * @author 科技㊣²º¹³M<br/>
 *         2014年2月3日 下午4:48:45 <br/>
 *         http://www.rekoe.com <br/>
 *         QQ:5382211
 */
@IocBean(args = { "refer:dao" })
public class SvnUserService extends BaseService<Usr> {

	private final String REGEX_USERNAME = "^[a-zA-Z0-9]{1,16}$";

	public SvnUserService(Dao dao) {
		super(dao);
	}

	public boolean nameOk(String name) {
		if (StringUtils.isBlank(name)) {
			return false;
		}
		if (!isUsername(name)) {
			return false;
		}
		return Lang.isEmpty(dao().fetch(getEntityClass(), Cnd.where("usr", "=", name)));
	}

	/**
	 * 校验用户名
	 * 
	 * @param username
	 * @return 校验通过返回true，否则返回false
	 */
	public boolean isUsername(String username) {
		return Pattern.matches(REGEX_USERNAME, username);
	}
}
