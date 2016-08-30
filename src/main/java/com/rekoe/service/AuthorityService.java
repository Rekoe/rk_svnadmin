package com.rekoe.service;

public interface AuthorityService {

	/**
	 * 扫描RequiresPermissions和RequiresRoles注解
	 * 
	 * @param pkg
	 *            需要扫描的package
	 */
	void initFormPackage(String... pkgs);
}
