package com.rekoe.mobile.provider.guest;

import java.io.Serializable;

import com.rekoe.mobile.AbstractParam;
import com.rekoe.utils.Constants;

public class GuestParam extends AbstractParam implements Serializable {

	private static final long serialVersionUID = 1240692439054026809L;
	private String openid;
	private int pid;

	public GuestParam() {
		super();
	}

	public GuestParam(int pid, String passportid) {
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
		return Constants.GUEST;
	}

	@Override
	public int getPid() {
		return this.pid;
	}
}
