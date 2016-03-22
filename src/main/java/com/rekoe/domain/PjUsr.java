package com.rekoe.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.PK;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

/**
 * 项目用户 。只对单库方式有用,包括svn协议和http协议(单库)，可以每个项目设置用户的密码
 */
@Table("pj_usr")
@PK({ "usr", "pj" })
@TableIndexes({ @Index(name = "FK_Reference_5", fields = { "pj" }, unique = false) })
public class PjUsr extends Usr {
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
