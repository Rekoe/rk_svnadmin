package com.rekoe.domain;

import java.io.Serializable;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.PK;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

/**
 * 项目用户 。只对单库方式有用,包括svn协议和http协议(单库)，可以每个项目设置用户的密码
 */
@Table("pj_usr")
@PK({ "usr", "pj" })
@TableIndexes({ @Index(name = "FK_Reference_5", fields = { "pj" }, unique = false) })
public class PjUsr implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5863709168694149752L;
	/**
	 * 项目ID
	 */
	@Column
	private String pj;

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
	@Column
	private String role;

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

	/**
	 * @return 项目ID
	 */
	public String getPj() {
		return pj;
	}

	/**
	 * @param pj
	 *            项目ID
	 */
	public void setPj(String pj) {
		this.pj = pj;
	}

}
