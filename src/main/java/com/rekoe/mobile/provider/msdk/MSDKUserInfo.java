package com.rekoe.mobile.provider.msdk;

import java.io.Serializable;

import org.nutz.json.JsonField;

public class MSDKUserInfo implements Serializable {

	private static final long serialVersionUID = 580303315270742465L;

	private int ret = -1;
	@JsonField(value = "is_lost")
	private int isLost = 1;
	private String nickname;
	private String gender;
	private String country;
	private String province;
	private String city;
	private String figureurl;
	@JsonField(value = "is_yellow_vip")
	private int isYellowVip;
	@JsonField(value = "is_yellow_year_vip")
	private int isYellowYearVip;
	@JsonField(value = "yellow_vip_level")
	private int yellowVipLevel;
	@JsonField(value = "is_yellow_high_vip")
	private int isYellowHighVip;
	@JsonField(value = "is_blue_vip")
	private boolean isBlueVip;
	@JsonField(value = "is_blue_year_vip")
	private boolean isBlueYearVip;
	@JsonField(value = "is_super_blue_vip")
	private boolean isSuperBlueVip;
	@JsonField(value = "blue_vip_level")
	private int blueVipLevel;
	@JsonField(value = "a3366_level")
	private int a3366Level;
	@JsonField(value = "a3366_level_name")
	private String a3366LevelName;
	@JsonField(value = "a3366_grow_level")
	private int a3366GrowLevel;
	@JsonField(value = "a3366_grow_value")
	private int a3366GrowValue;

	public int getRet() {
		return ret;
	}

	public void setRet(int ret) {
		this.ret = ret;
	}

	public int getIsLost() {
		return isLost;
	}

	public void setIsLost(int isLost) {
		this.isLost = isLost;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getFigureurl() {
		return figureurl;
	}

	public void setFigureurl(String figureurl) {
		this.figureurl = figureurl;
	}

	public int getIsYellowVip() {
		return isYellowVip;
	}

	public void setIsYellowVip(int isYellowVip) {
		this.isYellowVip = isYellowVip;
	}

	public int getIsYellowYearVip() {
		return isYellowYearVip;
	}

	public void setIsYellowYearVip(int isYellowYearVip) {
		this.isYellowYearVip = isYellowYearVip;
	}

	public int getYellowVipLevel() {
		return yellowVipLevel;
	}

	public void setYellowVipLevel(int yellowVipLevel) {
		this.yellowVipLevel = yellowVipLevel;
	}

	public int getIsYellowHighVip() {
		return isYellowHighVip;
	}

	public void setIsYellowHighVip(int isYellowHighVip) {
		this.isYellowHighVip = isYellowHighVip;
	}

	public boolean isBlueVip() {
		return isBlueVip;
	}

	public void setBlueVip(boolean isBlueVip) {
		this.isBlueVip = isBlueVip;
	}

	public boolean isBlueYearVip() {
		return isBlueYearVip;
	}

	public void setBlueYearVip(boolean isBlueYearVip) {
		this.isBlueYearVip = isBlueYearVip;
	}

	public boolean isSuperBlueVip() {
		return isSuperBlueVip;
	}

	public void setSuperBlueVip(boolean isSuperBlueVip) {
		this.isSuperBlueVip = isSuperBlueVip;
	}

	public int getBlueVipLevel() {
		return blueVipLevel;
	}

	public void setBlueVipLevel(int blueVipLevel) {
		this.blueVipLevel = blueVipLevel;
	}

	public int getA3366Level() {
		return a3366Level;
	}

	public void setA3366Level(int a3366Level) {
		this.a3366Level = a3366Level;
	}

	public String getA3366LevelName() {
		return a3366LevelName;
	}

	public void setA3366LevelName(String a3366LevelName) {
		this.a3366LevelName = a3366LevelName;
	}

	public int getA3366GrowLevel() {
		return a3366GrowLevel;
	}

	public void setA3366GrowLevel(int a3366GrowLevel) {
		this.a3366GrowLevel = a3366GrowLevel;
	}

	public int getA3366GrowValue() {
		return a3366GrowValue;
	}

	public void setA3366GrowValue(int a3366GrowValue) {
		this.a3366GrowValue = a3366GrowValue;
	}
}