package com.rekoe.common;

public class PlatformVO {

	private String type;
	private String name;

	public PlatformVO(String type, String name) {
		super();
		this.type = type;
		this.name = name;
	}

	public PlatformVO() {
		super();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
