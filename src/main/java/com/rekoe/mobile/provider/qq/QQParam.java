package com.rekoe.mobile.provider.qq;

import java.io.Serializable;

import com.rekoe.mobile.AbstractParam;
import com.rekoe.utils.Constants;

public class QQParam extends AbstractParam implements Serializable {

	private static final long serialVersionUID = -4898427425935858898L;
	private String openid;
	private String openkey;
	private String pf;
	private String platform;

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getPf() {
		return pf;
	}

	public void setPf(String pf) {
		this.pf = pf;
	}

	private int pid;

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getOpenkey() {
		return openkey;
	}

	public void setOpenkey(String openkey) {
		this.openkey = openkey;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	@Override
	public String getProviderId() {
		return Constants.QQ;
	}

	@Override
	public int getPid() {
		return pid;
	}

}
