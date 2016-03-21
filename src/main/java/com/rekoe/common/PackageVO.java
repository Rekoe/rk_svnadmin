package com.rekoe.common;

import java.io.Serializable;

public class PackageVO implements Serializable {
	private static final long serialVersionUID = 4225042323738026254L;
	private String productId;
	private int itemid;
	private float money;

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public int getItemid() {
		return itemid;
	}

	public void setItemid(int itemid) {
		this.itemid = itemid;
	}

	public float getMoney() {
		return money;
	}

	public void setMoney(float money) {
		this.money = money;
	}
}
