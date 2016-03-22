package com.rekoe.domain;

import java.io.Serializable;

import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;

/**
 * 用户
 * 
 */
@Table("usr")
public class Usr implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8251147689572549482L;
	/**
	 * 用户ID
	 */
	@Name
	private String usr;
	/**
	 * 用户姓名
	 */
	@Comment
	private String name;
	/**
	 * 密码(加密)
	 */
	@Comment
	private String psw;
	/**
	 * 角色
	 */
	@Comment
	private String role;

	/**
	 * @return 用户ID
	 */
	public String getUsr() {
		return usr;
	}

	/**
	 * @param usr
	 *            用户ID
	 */
	public void setUsr(String usr) {
		this.usr = usr;
	}

	/**
	 * @return 用户姓名
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            用户姓名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return 密码(加密)
	 */
	public String getPsw() {
		return psw;
	}

	/**
	 * @param psw
	 *            密码(加密)
	 */
	public void setPsw(String psw) {
		this.psw = psw;
	}

	/**
	 * @return 角色
	 */
	public String getRole() {
		return role;
	}

	/**
	 * @param role
	 *            角色
	 */
	public void setRole(String role) {
		this.role = role;
	}

}
