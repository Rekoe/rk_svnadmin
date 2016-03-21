package com.rekoe.common;

import java.io.Serializable;
import java.util.List;

public class AppleVO implements Serializable {

	private static final long serialVersionUID = 6286981086440847759L;

	private boolean validate;
	private List<PackageVO> items;

	public boolean isValidate() {
		return validate;
	}

	public void setValidate(boolean validate) {
		this.validate = validate;
	}

	public List<PackageVO> getItems() {
		return items;
	}

	public void setItems(List<PackageVO> items) {
		this.items = items;
	}
}
