package com.rekoe.mobile;

import java.io.Serializable;

import org.nutz.lang.util.NutMap;

public class Profile implements Serializable {

	public static final String TOKEN = "token";
	private static final long serialVersionUID = -3916223452935420808L;

	private String providerId;
	private String passportid;
	private int pid;
	private NutMap pros = NutMap.NEW();

	public Profile(String providerId, String passportid, int pid) {
		super();
		this.providerId = providerId;
		this.passportid = passportid;
		this.pid = pid;
	}

	public Profile(String providerId, long passportid, int pid) {
		super();
		this.providerId = providerId;
		this.passportid = String.valueOf(passportid);
		this.pid = pid;
	}

	public String getToken() {
		return pros.getString(TOKEN);
	}

	public int getPid() {
		return pid;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public String getPassportid() {
		return passportid;
	}

	public void setPassportid(String passportid) {
		this.passportid = passportid;
	}

	public Profile addv(String key, Object obj) {
		pros.addv(key, obj);
		return this;
	}

	public NutMap getPros() {
		return pros;
	}

}
