package com.rekoe.domain;

import java.io.Serializable;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.EL;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.Table;

/**
 * @author 科技㊣²º¹³<br />
 *         2014年4月19日 上午8:54:23<br />
 *         http://www.rekoe.com<br />
 *         QQ:5382211<br />
 */
@Table("cdkey_category")
public class CdkeyCategory implements Serializable {

	private static final long serialVersionUID = 7685127380108984960L;
	@Name
	@Prev(els = { @EL("uuid()") })
	private String id;

	@Column
	private String name;

	@Column(hump = true)
	private int cdkeyType;

	public CdkeyCategory() {
		super();
	}

	public CdkeyCategory(String name, int cdkeyType) {
		super();
		this.name = name;
		this.cdkeyType = cdkeyType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCdkeyType() {
		return cdkeyType;
	}

	public void setCdkeyType(int cdkeyType) {
		this.cdkeyType = cdkeyType;
	}
}
