package com.rekoe.domain;

import java.io.Serializable;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.PK;
import org.nutz.dao.entity.annotation.Readonly;
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
	 * 密码(加密)
	 */
	@Column
	private String psw;

	@Readonly
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPj() {
		return pj;
	}

	public void setPj(String pj) {
		this.pj = pj;
	}

	public String getUsr() {
		return usr;
	}

	public void setUsr(String usr) {
		this.usr = usr;
	}

	public String getPsw() {
		return psw;
	}

	public void setPsw(String psw) {
		this.psw = psw;
	}

}
