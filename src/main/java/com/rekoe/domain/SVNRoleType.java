package com.rekoe.domain;

public enum SVNRoleType {

	admin("管理员"), small("普通");

	public String display;

	SVNRoleType(String display) {
		this.display = display;
	}

	public String getDisplay() {
		return display;
	}
}
