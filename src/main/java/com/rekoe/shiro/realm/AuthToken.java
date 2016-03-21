package com.rekoe.shiro.realm;

import org.apache.shiro.authc.AuthenticationToken;

import com.rekoe.mobile.AbstractParam;

public class AuthToken implements AuthenticationToken {

	private static final long serialVersionUID = -8469592895652806116L;

	private AbstractParam param;

	public AuthToken(AbstractParam param) {
		this.param = param;
	}

	@Override
	public AbstractParam getPrincipal() {
		return param;
	}

	@Override
	public AbstractParam getCredentials() {
		return param;
	}

	public AbstractParam getParam() {
		return param;
	}

}
