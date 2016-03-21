package com.rekoe.domain;

import java.io.Serializable;
import java.util.Date;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;
import org.nutz.lang.Times;

@Table("server_history")
@TableIndexes({ @Index(name = "passportid_pid_index", fields = { "openid", "pid", "sid" }, unique = false) })
public class ServerHistory implements Serializable {

	private static final long serialVersionUID = 4746739180924296807L;

	@Id
	private long id;

	@Column
	private int pid;

	@Column(hump = true)
	@ColDefine(type = ColType.TIMESTAMP)
	private Date createTime;

	@Column(hump = true)
	@ColDefine(type = ColType.TIMESTAMP)
	private Date modifyTime;

	@Column
	private long openid;

	@Column
	private int sid;

	public ServerHistory(long openid, int pid, int sid) {
		super();
		this.pid = pid;
		this.openid = openid;
		this.createTime = Times.now();
		this.modifyTime = this.createTime;
		this.sid = sid;
	}

	public ServerHistory() {
		super();
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public long getOpenid() {
		return openid;
	}

	public void setOpenid(long openid) {
		this.openid = openid;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

}
