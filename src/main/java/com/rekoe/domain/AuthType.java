package com.rekoe.domain;

public enum AuthType {

	DEFAULT("DEFAULT"), TOKEN("TOKEN"), QQ_HTML5("QQ_HTML5");

	public String display;

	AuthType(String display) {
		this.display = display;
	}

	public String getDisplay() {
		return display;
	}
}
