package com.rekoe.domain;

import java.util.Date;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

@Table("cdkey_${taskid}")
@TableIndexes({ @Index(name = "cdk_${taskid}_index", fields = { "cdkey" }, unique = true), @Index(name = "cdk_${taskid}_used_index", fields = { "used" }, unique = false) })
public class CdkeyEntity {

	@Id
	private long id;
	@Column
	private String cdkey;

	@Column("is_used")
	@ColDefine(type = ColType.BOOLEAN)
	private boolean used;

	@Column(hump = true)
	@ColDefine(type = ColType.TIMESTAMP)
	private Date createTime;

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

	public CdkeyEntity() {
		super();
	}

	public CdkeyEntity(String cdkey, Date createTime) {
		super();
		this.cdkey = cdkey;
		this.createTime = createTime;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}