package com.rekoe.mobile.provider.hw;

public class UserInfo {
	private String userID;

	private String userName;
    
    private String languageCode;
    
    private int userState;
    
    private int userValidStatus;
    
    /**
	 * @return the userID
	 */
	public String getUserID(){
		return userID;
	}

	/**
	 * @param userID the userID to set
	 */
	public void setUserID(String userID){
		this.userID = userID;
	}

	/**
	 * @return the userName
	 */
	public String getUserName(){
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName){
		this.userName = userName;
	}

	/**
	 * @return the languageCode
	 */
	public String getLanguageCode(){
		return languageCode;
	}

	/**
	 * @param languageCode the languageCode to set
	 */
	public void setLanguageCode(String languageCode){
		this.languageCode = languageCode;
	}

	/**
	 * @return the userState
	 */
	public int getUserState(){
		return userState;
	}

	/**
	 * @param userState the userState to set
	 */
	public void setUserState(int userState){
		this.userState = userState;
	}

	/**
	 * @return the userValidStatus
	 */
	public int getUserValidStatus(){
		return userValidStatus;
	}

	/**
	 * @param userValidStatus the userValidStatus to set
	 */
	public void setUserValidStatus(int userValidStatus){
		this.userValidStatus = userValidStatus;
	}

}
