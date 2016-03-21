package com.rekoe.domain;

import java.io.Serializable;
import java.util.Date;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;
import org.nutz.lang.Times;

@Table("platform_user")
@TableIndexes({ @Index(name = "passportid_pfid_pid_index", fields = { "passportid", "pfid", "pid" }, unique = true) })
public class PlatformUser implements Serializable {

	private static final long serialVersionUID = -2735176124180310964L;

	@Id
	private long id;

	@Column
	private String passportid;

	@Column(hump = true)
	@ColDefine(type = ColType.TIMESTAMP, insert = true)
	private Date createTime;

	@Column("is_locked")
	private boolean locked;

	@Column
	private long openid;

	@Column("pfid")
	@Comment("渠道号")
	@ColDefine(type = ColType.VARCHAR, width = 50)
	private String pfid;

	@Column("pid")
	@Comment("平台号")
	private int pid;

	@Column
	@Comment("IP")
	@Default("127.0.0.1")
	private String addr;

	public PlatformUser() {
		super();
	}

	public PlatformUser(int pid, String passportid, long openid, String pfid, String addr) {
		this.pid = pid;
		this.passportid = passportid;
		this.createTime = Times.now();
		this.openid = openid;
		this.pfid = pfid;
		this.addr = addr;
	}

	public PlatformUser(int pid, String passportid, long openid, String pfid) {
		this.pid = pid;
		this.passportid = passportid;
		this.createTime = Times.now();
		this.openid = openid;
		this.pfid = pfid;
	}

	public long getOpenid() {
		return openid;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public void setOpenid(long openid) {
		this.openid = openid;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPassportid() {
		return passportid;
	}

	public void setPassportid(String passportid) {
		this.passportid = passportid;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public String getPfid() {
		return pfid;
	}

	public void setPfid(String pfid) {
		this.pfid = pfid;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

}
