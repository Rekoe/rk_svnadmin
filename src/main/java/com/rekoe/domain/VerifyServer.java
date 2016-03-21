package com.rekoe.domain;

import java.io.Serializable;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.EL;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

/**
 * 提审服
 * 
 * @author kouxian
 *
 */
@Table("verify_server")
@TableIndexes({ @Index(name = "g_sid_index", fields = { "pid" }, unique = true) })
public class VerifyServer implements Serializable {

	private static final long serialVersionUID = -2235259090910419768L;

	@Name
	@Prev(els = { @EL("uuid()") })
	private String id;

	@Column
	@ColDefine(width = 225)
	private String url;

	@Column
	private String name;

	@Column(hump = true)
	@Comment("登陆服地址")
	@ColDefine(width = 225)
	private String loginUrl;

	@Column(hump = true)
	@Comment("充值地址")
	@ColDefine(width = 225)
	private String payUrl;

	@Column("pid")
	private int pid;

	@One(target = GameServer.class, field = "pid")
	private GameServer gameServer;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public String getPayUrl() {
		return payUrl;
	}

	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}

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

}
