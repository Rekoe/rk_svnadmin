package com.rekoe.domain;

import java.io.Serializable;

import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.PK;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

/**
 * 项目组用户
 */
@Table("pj_gr_usr")
@PK({ "pj", "usr", "gr" })
@TableIndexes({ @Index(name = "FK_Reference_10", fields = { "pj", "gr" }, unique = false), @Index(name = "FK_Reference_9", fields = { "usr" }, unique = false) })
public class PjGrUsr implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2031955995574649327L;
	/**
	 * 项目
	 */
	@Name
	private String pj;
	/**
	 * 用户
	 */
	@Comment
	private String usr;
	/**
	 * 用户姓名
	 */
	@Comment
	private String usrName;
	/**
	 * 组
	 */
	@Comment
	private String gr;

	/**
	 * @return 项目
	 */
	public String getPj() {
		return pj;
	}

	/**
	 * @param pj
	 *            项目
	 */
	public void setPj(String pj) {
		this.pj = pj;
	}

	/**
	 * @return 用户
	 */
	public String getUsr() {
		return usr;
	}

	/**
	 * @return 用户姓名
	 */
	public String getUsrName() {
		return usrName;
	}

	/**
	 * @param usrName
	 *            用户姓名
	 */
	public void setUsrName(String usrName) {
		this.usrName = usrName;
	}

	/**
	 * @param usr
	 *            用户
	 */
	public void setUsr(String usr) {
		this.usr = usr;
	}

	/**
	 * @return 组
	 */
	public String getGr() {
		return gr;
	}

	/**
	 * @param gr
	 *            组
	 */
	public void setGr(String gr) {
		this.gr = gr;
	}

}
