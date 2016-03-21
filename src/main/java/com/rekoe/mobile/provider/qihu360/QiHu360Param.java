package com.rekoe.mobile.provider.qihu360;

import java.io.Serializable;

import com.rekoe.mobile.AbstractParam;
import com.rekoe.utils.Constants;

public class QiHu360Param extends AbstractParam implements Serializable {

	private static final long serialVersionUID = -4558333251854028360L;
	private String sessionid;
	private int pid;

	public String getSessionid() {
		return sessionid;
	}

	public void setSessionid(int pid, String sessionid) {
		this.sessionid = sessionid;
		this.pid = pid;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	@Override
	public String getProviderId() {
		return Constants.QIHU360;
	}

}
