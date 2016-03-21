package com.rekoe.module.facebook;

/**
 * @author 科技㊣²º¹³<br />
 *         2014年6月15日 下午8:35:20<br />
 *         http://www.rekoe.com<br />
 *         QQ:5382211<br />
 */
public class ReFund {

	private String challenge;
	private String mode;
	private String verify_token;

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getVerify_token() {
		return verify_token;
	}

	public void setVerify_token(String verify_token) {
		this.verify_token = verify_token;
	}

	public String getChallenge() {
		return challenge;
	}

	public void setChallenge(String challenge) {
		this.challenge = challenge;
	}
}
