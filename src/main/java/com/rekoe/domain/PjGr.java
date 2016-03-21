package com.rekoe.domain;

import java.io.Serializable;

import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;

/**
 * 组
 * 
 */
@Table("pj_gr")
public class PjGr implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6413099229527347938L;
	/**
	 * 项目
	 */
	@Name
	private String pj;
	/**
	 * 组
	 */
	@Comment
	private String gr;
	/**
	 * 描述
	 */
	@Comment
	private String des;

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
