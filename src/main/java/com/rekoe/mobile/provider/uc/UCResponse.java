package com.rekoe.mobile.provider.uc;

public class UCResponse {

	private UCResponseState state;
	private UCResponseData data;

	public UCResponse(UCResponseState state, UCResponseData data) {
		this.state = state;
		this.data = data;
	}

	public UCResponse(UCResponseState state) {
		this.state = state;
	}

	public UCResponseState getState() {
		return state;
	}

	public UCResponseData getData() {
		return data;
	}

	public void setData(UCResponseData data) {
		this.data = data;
	}
}
