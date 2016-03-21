package com.rekoe.module;

public class CdkeyBean {

	private String sign;
	private String cdkey;
	private String mac;
	private int pid;
	private String providerId;

	public CdkeyBean() {
		super();
	}

	public CdkeyBean(String sign, String cdkey, String mac, int pid, String providerId) {
		super();
		this.sign = sign;
		this.cdkey = cdkey;
		this.mac = mac;
		this.pid = pid;
		this.providerId = providerId;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public String getCdkey() {
		return cdkey;
	}

	public void setCdkey(String cdkey) {
		this.cdkey = cdkey;
	}
}