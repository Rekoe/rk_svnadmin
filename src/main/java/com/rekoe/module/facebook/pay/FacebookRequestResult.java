package com.rekoe.module.facebook.pay;

import com.restfb.Facebook;

public class FacebookRequestResult {

	@Facebook
	private boolean success;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
}
