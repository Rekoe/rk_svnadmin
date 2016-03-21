package com.rekoe.filter;

import javax.servlet.http.HttpServletResponse;

public class OauthCrossOriginFilter extends AbstractCrossOriginFilter {

	@Override
	protected boolean on() {
		return true;
	}

	@Override
	protected void addHeader(HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Methods", "get, post, put, delete, options");
		response.addHeader("Access-Control-Allow-Headers", "origin, content-type, accept");
		response.addHeader("Access-Control-Allow-Credentials", "true");
	}
}
