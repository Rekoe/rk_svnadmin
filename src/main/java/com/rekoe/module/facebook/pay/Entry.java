package com.rekoe.module.facebook.pay;

import java.util.List;

import org.nutz.json.JsonField;

public class Entry {

	private String id;
	private long time;
	@JsonField("changed_fields")
	private List<String> changedFields;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public List<String> getChangedFields() {
		return changedFields;
	}

	public void setChangedFields(List<String> changedFields) {
		this.changedFields = changedFields;
	}
}
