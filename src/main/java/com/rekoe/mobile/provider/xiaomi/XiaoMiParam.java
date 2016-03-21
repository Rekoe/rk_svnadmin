package com.rekoe.mobile.provider.xiaomi;

import java.io.Serializable;

import com.rekoe.mobile.AbstractParam;
import com.rekoe.utils.Constants;

public class XiaoMiParam extends AbstractParam implements Serializable {

	private static final long serialVersionUID = -5138092630114580043L;
	private String uid;
	private String token;
	private int pid;

	public XiaoMiParam() {
		super();
	}

	public XiaoMiParam(int pid, String uid, String token) {
		super();
		this.uid = uid;
		this.uid = token;
		this.pid = pid;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	@Override
	public String getProviderId() {
		return Constants.XIAOMI;
	}

}
