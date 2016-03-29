package com.rekoe.service;

import java.util.Map;

public interface EmailService {

	public boolean send(String to, String subject, String templateFile, Map<String, Object> root);

	/**
	 * 密码重置
	 * 
	 * @param to
	 * @param root
	 * @return
	 */
	public boolean restpwd(String to, Map<String, Object> root);

	/**
	 * 项目开启
	 * 
	 * @param to
	 * @param root
	 * @return
	 */
	public boolean projectOpen(String to, Map<String, Object> root);

}
