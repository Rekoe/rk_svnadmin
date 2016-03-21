package com.rekoe.domain;

import java.io.Serializable;
import java.util.Date;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.json.JsonField;

/**
 * @author 科技㊣²º¹³<br/>
 *         2014年2月3日 下午4:48:45<br/>
 *         http://www.rekoe.com<br/>
 *         QQ:5382211
 */
@Table("sg_notice")
public class Notice implements Serializable {

	private static final long serialVersionUID = 1566985676414681818L;
	
	@Id(auto=true)
	private long id;

	@Column(hump = true)
	@ColDefine(type = ColType.TIMESTAMP)
	@JsonField(ignore = true)
	private Date createDate;

	@Column
	@Default("#FF0000")
	private String color;

	@Column(hump = true)
	@ColDefine(type = ColType.TIMESTAMP)
	@JsonField(ignore = true)
	private Date modifyDate;

	@Column
	@ColDefine(type = ColType.TEXT)
	@JsonField(ignore = true)
	private String content;

	@Column("is_publication")
	@ColDefine(type = ColType.BOOLEAN)
	@JsonField(ignore = true)
	private boolean publication;

	@Column("is_top")
	@ColDefine(type = ColType.BOOLEAN)
	@JsonField(ignore = true)
	private boolean top;

	@Column
	@ColDefine(type = ColType.VARCHAR, width = 255)
	private String title;

	@Column
	@Default("1001")
	@JsonField(ignore = true)
	private int pid;

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isPublication() {
		return publication;
	}

	public void setPublication(boolean publication) {
		this.publication = publication;
	}

	public boolean isTop() {
		return top;
	}

	public void setTop(boolean top) {
		this.top = top;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

}