package com.rekoe.mobile.provider.msdk;

import java.io.Serializable;

import com.rekoe.mobile.AbstractParam;
import com.rekoe.utils.Constants;

public class MSDKParam extends AbstractParam implements Serializable {
	private static final long serialVersionUID = -8345954529141180606L;
	private String openid;
	private String ip;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	private int pid;

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
		return Constants.MSDK;
	}

	@Override
	public int getPid() {
		return pid;
	}

}
