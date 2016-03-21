package com.rekoe.module.facebook.pay;

import org.nutz.json.JsonField;

public class Dispute {

	@JsonField("user_comment")
	private String userComment;
	@JsonField("time_created")
	private String timeCreated;
	@JsonField("user_email")
	private String userEmail;
	private String status;
	private String reason;

	public String getUserComment() {
		return userComment;
	}

	public void setUserComment(String userComment) {
		this.userComment = userComment;
	}

	public String getTimeCreated() {
		return timeCreated;
	}

	public void setTimeCreated(String timeCreated) {
		this.timeCreated = timeCreated;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}
