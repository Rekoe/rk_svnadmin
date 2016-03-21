package com.rekoe.domain;

import java.io.Serializable;

import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;

/**
 * 项目
 */
@Table("pj")
public class Pj implements Serializable{
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
	 * 仓库位置
	 */
	@Comment
	private String path;
	/**
	 * 访问项目的svn地址
	 */
	@Comment
	private String url;
	/**
	 * 描述
	 */
	@Comment
	private String des;
	/**
	 * 类型
	 */
	@Comment
	private String type;

	/**
	 * 用户是否是这个项目的管理员
	 */
	@Comment
	private boolean manager;

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
	 * @return 仓库位置
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path
	 *            仓库位置
	 */
	public void setPath(String path) {
		this.path = path;
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
	 * @return 访问项目的svn地址
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            访问项目的svn地址
	 */
	public void setUrl(String url) {
		this.url = url;
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
