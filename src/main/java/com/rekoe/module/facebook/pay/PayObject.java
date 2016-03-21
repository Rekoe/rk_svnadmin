package com.rekoe.module.facebook.pay;

import java.util.List;

import org.nutz.json.JsonField;

//{"object":"payments","entry":[{"id":"577551589029171","time":1402986080,"changed_fields":["actions"]}]}
public class PayObject {

	private String object;
	@JsonField("entry")
	private List<Entry> entryList;

	public List<Entry> getEntryList() {
		return entryList;
	}

	public void setEntryList(List<Entry> entryList) {
		this.entryList = entryList;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

}
