package com.rekoe.domain;

import java.io.Serializable;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Readonly;
import org.nutz.dao.entity.annotation.Table;

/**
 * 项目
 */
@Table("pj")
public class Pj implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3570692240378418844L;
	/**
	 * 项目ID
	 */
	@Name
	private String pj;
	/**
	 * 描述
	 */
	@Column
	private String des;
	/**
	 * 类型
	 */
	@Column
	private String type;

	/**
	 * 用户是否是这个项目的管理员
	 */
	@Readonly
	private boolean manager;

	@Column("is_init_templ")
	@Default("0")
	@Comment("是否初始化模板")
	private boolean initTempl;

	public boolean isInitTempl() {
		return initTempl;
	}

	public void setInitTempl(boolean initTempl) {
		this.initTempl = initTempl;
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

	/**
	 * @return 描述
	 */
	public String getDes() {
		return des;
	}

	/**
	 * @param des
	 *            描述
	 */
	public void setDes(String des) {
		this.des = des;
	}

	/**
	 * @return 类型
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            类型
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return 用户是否是这个项目的管理员
	 */
	public boolean isManager() {
		return manager;
	}

	/**
	 * @param manager
	 *            用户是否是这个项目的管理员
	 */
	public void setManager(boolean manager) {
		this.manager = manager;
	}

}
