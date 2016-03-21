package com.rekoe.service;

import java.util.List;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;

import com.rekoe.common.page.Pagination;
import com.rekoe.domain.OAuthUser;

@IocBean(name = "oAuthUserService", fields = { "dao" })
public class OAuthUserServiceImpl extends BaseService<OAuthUser> implements OAuthUserService {

	@Inject
	private PasswordHelper passwordHelper;

	@Inject
	private IdWorkerService idFactory;

	/**
	 * 创建用户
	 * 
	 * @param user
	 */
	public OAuthUser createUser(OAuthUser user) {
		return createUser(user, idFactory.nextId());
	}

	public OAuthUser createUser(OAuthUser user, long userid) {
		passwordHelper.encryptPassword(user);
		user.setUserId(userid);
		return dao().insert(user);
	}

	@Override
	public OAuthUser updateUser(OAuthUser user) {
		dao().update(user);
		return user;
	}

	@Override
	public void deleteUser(String userId) {
		dao().delete(getEntityClass(), userId);
	}

	/**
	 * 修改密码
	 * 
	 * @param userId
	 * @param newPassword
	 */
	public void changePassword(String userId, String newPassword) {
		OAuthUser user = dao().fetch(getEntityClass(), userId);
		user.setPassword(newPassword);
		passwordHelper.encryptPassword(user);
		updateUser(user);
	}

	@Override
	public OAuthUser findOne(long userId) {
		return dao().fetch(getEntityClass(), Cnd.where("userId", "=", userId));
	}

	@Override
	public List<OAuthUser> findAll() {
		return dao().query(getEntityClass(), null);
	}

	/**
	 * 根据用户名查找用户
	 * 
	 * @param username
	 * @return
	 */
	public OAuthUser findByUsername(String username) {
		return dao().fetch(getEntityClass(), Cnd.where("userName", "=", username));
	}

	/**
	 * 验证登录
	 * 
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @param salt
	 *            盐
	 * @param encryptpwd
	 *            加密后的密码
	 * @return
	 */
	public boolean checkUser(String username, String password, String salt, String encryptpwd) {
		String pwd = passwordHelper.encryptPassword(username, password, salt);
		return pwd.equals(encryptpwd);
	}

	@Override
	public boolean checkRestPwd(String name, long phoneCode) {
		return !Lang.isEmpty(dao().fetch(getEntityClass(), Cnd.where("userName", "=", name).and("phoneCode", "=", phoneCode)));
	}

	@Override
	public boolean updateByChain(String name, String pwd) {
		String salt = passwordHelper.buildSalt();
		String passpord = passwordHelper.encryptPassword(name, pwd, salt);
		dao().update(getEntityClass(), Chain.make("password", passpord).add("salt", salt), Cnd.where("userName", "=", name));
		return true;
	}

	public Pagination getListPager(int pageNumber) {
		return getObjListByPager(pageNumber, DEFAULT_PAGE_NUMBER, null);
	}

	public String getRandomCode() {
		return getRandomIntStr(6);
	}
}
