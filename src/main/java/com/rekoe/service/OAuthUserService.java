package com.rekoe.service;

import java.util.List;

import com.rekoe.common.page.Pagination;
import com.rekoe.domain.OAuthUser;

public interface OAuthUserService {
	/**
	 * 创建用户
	 * 
	 * @param user
	 */
	public OAuthUser createUser(OAuthUser user);
	public OAuthUser createUser(OAuthUser user, long userid);
	public OAuthUser updateUser(OAuthUser user);

	public boolean updateByChain(String name, String pwd);

	public void deleteUser(String userId);

	/**
	 * 修改密码
	 * 
	 * @param userId
	 * @param newPassword
	 */
	public void changePassword(String userId, String newPassword);

	OAuthUser findOne(long userId);

	List<OAuthUser> findAll();

	/**
	 * 根据用户名查找用户
	 * 
	 * @param username
	 * @return
	 */
	public OAuthUser findByUsername(String username);

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
	boolean checkUser(String username, String password, String salt, String encryptpwd);

	public boolean checkRestPwd(String name, long phone);

	public Pagination getListPager(int pageNumber);
	public String getRandomCode();
}
