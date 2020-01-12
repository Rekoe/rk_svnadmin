package com.rekoe.domain;

import java.io.Serializable;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.PK;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

@Table("pj_gr_auth")
@PK({ "pj", "res", "gr" })
@TableIndexes({ @Index(name = "FK_Reference_6", fields = { "pj", "gr" }, unique = false) })
public class PjGrAuth  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5801319002448310527L;
	@Comment
	private String pj;
	@Comment
	private String gr;
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

	public String getGr() {
		return gr;
	}

	public void setGr(String gr) {
		this.gr = gr;
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
