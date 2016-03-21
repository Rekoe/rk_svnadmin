package com.rekoe.mobile.provider.sg;

import java.io.Serializable;

import com.rekoe.mobile.AbstractParam;
import com.rekoe.utils.Constants;

public class SGParam extends AbstractParam implements Serializable {

	private static final long serialVersionUID = 1240692439054026809L;
	private String openid;
	private int pid;

	public SGParam() {
		super();
	}

	public SGParam(int pid, String passportid) {
		super();
		this.openid = passportid;
		this.pid = pid;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	@Override
	public String getProviderId() {
		return Constants.SG;
	}

	@Override
	public int getPid() {
		return this.pid;
	}
}
