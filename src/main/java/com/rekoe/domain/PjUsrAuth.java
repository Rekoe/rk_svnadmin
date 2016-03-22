package com.rekoe.domain;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.PK;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

@Table("pj_usr_auth")
@PK({ "pj", "res", "usr" })
@TableIndexes({ @Index(name = "FK_Reference_8", fields = { "usr" }, unique = false) })
public class PjUsrAuth {

	@Comment
	private String pj;
	@Comment
	private String usr;
	@Comment
	@ColDefine(width = 200)
	private String res;
	@Comment
	private String rw;

	public String getPj() {
		return pj;
	}

	public void setPj(String pj) {
		this.pj = pj;
	}

	public String getUsr() {
		return usr;
	}

	public void setUsr(String usr) {
		this.usr = usr;
	}

	public String getRes() {
		return res;
	}

	public void setRes(String res) {
		this.res = res;
	}

	public String getRw() {
		return rw;
	}

	public void setRw(String rw) {
		this.rw = rw;
	}

}
