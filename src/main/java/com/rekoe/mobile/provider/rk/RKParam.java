package com.rekoe.mobile.provider.rk;

import java.io.Serializable;

import com.rekoe.mobile.AbstractParam;
import com.rekoe.utils.Constants;

public class RKParam extends AbstractParam implements Serializable {

	private static final long serialVersionUID = 1240692439054026809L;
	private String code;

	private int pid;

	public RKParam() {
		super();
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	@Override
	public String getProviderId() {
		return Constants.RK;
	}

	@Override
	public int getPid() {
		return this.pid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
