package com.rekoe.service;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.nutz.ioc.loader.annotation.IocBean;

import com.rekoe.domain.OAuthUser;

@IocBean
public class PasswordHelper {

	private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();

	private String algorithmName = "md5";
	private int hashIterations = 2;

	public void setRandomNumberGenerator(RandomNumberGenerator randomNumberGenerator) {
		this.randomNumberGenerator = randomNumberGenerator;
	}

	public void setAlgorithmName(String algorithmName) {
		this.algorithmName = algorithmName;
	}

	public void setHashIterations(int hashIterations) {
		this.hashIterations = hashIterations;
	}

	public void encryptPassword(OAuthUser user) {
		user.setSalt(buildSalt());
		user.setPassword(encryptPassword(user.getUserName(), user.getPassword(), user.getSalt()));
	}

	/**
	 * 根据用户名和盐值加密
	 * 
	 * @param username
	 * @param password
	 * @param salt
	 */
	public String encryptPassword(String username, String password, String salt) {
		String pwd = new SimpleHash(algorithmName, password, ByteSource.Util.bytes(salt), hashIterations).toHex();
		return pwd;
	}

	public String buildSalt() {
		return randomNumberGenerator.nextBytes().toHex();
	}
}
