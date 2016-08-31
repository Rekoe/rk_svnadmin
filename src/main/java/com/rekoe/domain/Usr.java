package com.rekoe.domain;

import java.io.Serializable;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Default;
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
	@Column
	private String name;
	/**
	 * 密码(加密)
	 */
	@Column
	private String psw;
	/**
	 * 角色
	 */
	@Column("role")
	@Default("small")
	private SVNRoleType role;

	@Column
	private String email;

	/**
	 * @return 用户ID
	 */
	public String getUsr() {
		return usr;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public SVNRoleType getRole() {
		return role;
	}

	public void setRole(SVNRoleType role) {
		this.role = role;
	}

}
