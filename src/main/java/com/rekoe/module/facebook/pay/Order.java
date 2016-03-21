package com.rekoe.module.facebook.pay;

import java.util.List;

import org.nutz.json.JsonField;

public class Order {

	private String id;
	private Application application;
	private PlatformUser user;
	private List<Action> actions;
	@JsonField("refundable_amount")
	private RefundableAmount refundableAmount;
	private String country;
	@JsonField("request_id")
	private String requestID;
	@JsonField("created_time")
	private String createdTime;
	@JsonField("payout_foreign_exchange_rate")
	private int payoutForeignExchangeRate;
	private List<Items> items;
	private List<Dispute> disputes;
	private int test;

	public int getTest() {
		return test;
	}

	public void setTest(int test) {
		this.test = test;
	}

	public List<Dispute> getDisputes() {
		return disputes;
	}

	public void setDisputes(List<Dispute> disputes) {
		this.disputes = disputes;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

	public PlatformUser getUser() {
		return user;
	}

	public void setUser(PlatformUser user) {
		this.user = user;
	}

	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}

	public RefundableAmount getRefundableAmount() {
		return refundableAmount;
	}

	public void setRefundableAmount(RefundableAmount refundableAmount) {
		this.refundableAmount = refundableAmount;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getRequestID() {
		return requestID;
	}

	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	public int getPayoutForeignExchangeRate() {
		return payoutForeignExchangeRate;
	}

	public void setPayoutForeignExchangeRate(int payoutForeignExchangeRate) {
		this.payoutForeignExchangeRate = payoutForeignExchangeRate;
	}

	public List<Items> getItems() {
		return items;
	}

	public void setItems(List<Items> items) {
		this.items = items;
	}
}
