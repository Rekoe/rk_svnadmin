package com.rekoe.mobile.provider.uc;

import java.io.Serializable;

import com.rekoe.mobile.AbstractParam;
import com.rekoe.utils.Constants;

public class UcParam extends AbstractParam  implements Serializable{

	private static final long serialVersionUID = -2473883638175694140L;
	private String sid;
	private int pid;

	public String getSid() {
		return sid;
	}

	public UcParam() {
	}

	public UcParam(int pid, String sid) {
		this.sid = sid;
		this.pid = pid;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	@Override
	public String getProviderId() {
		return Constants.UC;
	}

}
