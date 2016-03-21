package com.rekoe.domain;

import java.io.Serializable;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

/**
 * 正式服
 * 
 * @author kouxian
 *
 */
@Table("official_server")
@TableIndexes({ @Index(name = "g_sid_pid_index", fields = { "pid", "sid" }, unique = true) })
public class OfficialServer implements Serializable {

	private static final long serialVersionUID = 6978032147399501026L;

	@Id
	private long id;

	@Column
	private int sid;

	@Column
	@ColDefine(width = 225)
	private String url;

	@Column
	private String name;

	@Column
	private short status;

	@Column("pid")
	private int pid;

	@One(target = GameServer.class, field = "pid")
	private GameServer gameServer;

	@Column(hump = true)
	@Default("0")
	@ColDefine(type = ColType.BOOLEAN)
	@Comment("是否开启白名单")
	private boolean openWhiteList;

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public GameServer getGameServer() {
		return gameServer;
	}

	public void setGameServer(GameServer gameServer) {
		this.gameServer = gameServer;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public short getStatus() {
		return status;
	}

	public void setStatus(short status) {
		this.status = status;
	}

	public boolean isOpenWhiteList() {
		return openWhiteList;
	}

	public void setOpenWhiteList(boolean openWhiteList) {
		this.openWhiteList = openWhiteList;
	}

}
