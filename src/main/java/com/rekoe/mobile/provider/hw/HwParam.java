package com.rekoe.mobile.provider.hw;

import java.io.Serializable;

import com.rekoe.mobile.AbstractParam;
import com.rekoe.utils.Constants;

public class HwParam extends AbstractParam implements Serializable {

	private static final long serialVersionUID = -1122775243733543089L;
	private String accessToken;
	private int pid;

	public HwParam() {
	}

	@Override
	public String getProviderId() {
		return Constants.HW;
	}

	@Override
	public int getPid() {
		return pid;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

}
