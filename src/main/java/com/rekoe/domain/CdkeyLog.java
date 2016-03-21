package com.rekoe.domain;

import java.io.Serializable;
import java.util.Date;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.EL;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.Table;

@Table("cdkey_log")
public class CdkeyLog implements Serializable {

	private static final long serialVersionUID = -6797252917202955579L;

	@Id
	private long id;
	@Column
	private int pid;
	@Column(hump = true)
	private String providerId;
	@Column
	private String mac;

	@Column
	@ColDefine(type = ColType.TIMESTAMP)
	@Prev(els = @EL("$me.now()"))
	private Date createTime;

	public CdkeyLog() {
		super();
	}

	public CdkeyLog(int pid, String providerId, String mac) {
		super();
		this.pid = pid;
		this.providerId = providerId;
		this.mac = mac;
	}

	public Date now() {
		return new Date();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
