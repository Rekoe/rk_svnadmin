package com.rekoe.mobile.provider.a91;

import org.nutz.json.JsonField;

public class A91ReturnParam {
	
	@JsonField("AppID")
	private  int appID;
	@JsonField("ResultCode")
	private  int resultCode;
	@JsonField("ResultMsg")
	private  String resultMsg;
	@JsonField("Sign")
	private  String sign;
	@JsonField("Content")
	private  String content;
	


	public int getAppID() {
		return appID;
	}
	public void setAppID(int appID) {
		this.appID = appID;
	}
	public int getResultCode() {
		return resultCode;
	}
	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultMsg() {
		return resultMsg;
	}
	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	

	
}
