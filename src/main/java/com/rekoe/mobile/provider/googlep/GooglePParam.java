package com.rekoe.mobile.provider.googlep;

import java.io.Serializable;

import com.rekoe.mobile.AbstractParam;
import com.rekoe.utils.Constants;

public class GooglePParam extends AbstractParam implements Serializable {

	private static final long serialVersionUID = -1122775243733543089L;

	private String token;

	private int pid;

	public GooglePParam() {
	}

	@Override
	public String getProviderId() {
		return Constants.GOOGLE_PLAY;
	}

	@Override
	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
