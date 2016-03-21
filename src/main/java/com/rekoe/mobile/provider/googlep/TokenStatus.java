package com.rekoe.mobile.provider.googlep;

public class TokenStatus {

	private boolean valid;
	private String gplus_id;
	private String message;
	
	public TokenStatus() {
		valid = false;
		gplus_id = "";
		message = "";
	}

	public void setValid(boolean v) {
		this.valid = v;
	}

	public void setId(String gplus_id) {
		this.gplus_id = gplus_id;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getGplus_id() {
		return gplus_id;
	}

	public void setGplus_id(String gplus_id) {
		this.gplus_id = gplus_id;
	}

	public boolean isValid() {
		return valid;
	}

	public String getMessage() {
		return message;
	}
	
}
