package com.rekoe.mobile.provider.a91;

import java.io.Serializable;

import com.rekoe.mobile.AbstractParam;
import com.rekoe.utils.Constants;

public class A91Param extends AbstractParam  implements Serializable{
	
	private static final long serialVersionUID = -4493840542767841651L;
	private String sessionid;
	private int pid;
	
	public void setSessionid(int pid, String sessionid) {
		this.sessionid = sessionid;
		this.pid = pid;
	}
	
	public String getSessionid() {
		return sessionid;
	}
	
	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}
	
	public int getPid() {
		return pid;
	}
	
	public void setPid(int pid) {
		this.pid = pid;
	}

	@Override
	public String getProviderId(){
		return Constants.A91;
	}

}
