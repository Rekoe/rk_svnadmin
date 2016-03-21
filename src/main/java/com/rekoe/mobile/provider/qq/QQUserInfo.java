package com.rekoe.mobile.provider.qq;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QQUserInfo implements Serializable {

	private static final long serialVersionUID = -6009549946309845409L;
	private int ret = -1;
	private int is_lost = 1;
	private String nickname;
	private String gender;
	private String country;
	private String province;
	private String city;
	private String figureurl;
	private int is_yellow_vip;
	private int is_yellow_year_vip;
	private int yellow_vip_level;
	private int is_yellow_high_vip;
	// 是否蓝钻，是返回蓝钻等级，不是返回-1
	private boolean is_blue_vip;
	// 是否年费蓝钻
	private boolean is_blue_year_vip;
	// 也许是豪华蓝钻
	private boolean is_super_blue_vip;
	// 蓝钻等级
	private int blue_vip_level;
	private int a3366_level;
	private String a3366_level_name;
	private int a3366_grow_level;
	private int a3366_grow_value;

	public int getA3366_level() {
		return a3366_level;
	}

	public void setA3366_level(int a3366_level) {
		this.a3366_level = a3366_level;
	}

	public String getA3366_level_name() {
		return a3366_level_name;
	}

	public void setA3366_level_name(String a3366_level_name) {
		this.a3366_level_name = a3366_level_name;
	}

	public int getA3366_grow_level() {
		return a3366_grow_level;
	}

	public void setA3366_grow_level(int a3366_grow_level) {
		this.a3366_grow_level = a3366_grow_level;
	}

	public int getA3366_grow_value() {
		return a3366_grow_value;
	}

	public void setA3366_grow_value(int a3366_grow_value) {
		this.a3366_grow_value = a3366_grow_value;
	}

	public int getRet() {
		return ret;
	}

	public void setRet(int ret) {
		this.ret = ret;
	}

	public int getIs_lost() {
		return is_lost;
	}

	public void setIs_lost(int is_lost) {
		this.is_lost = is_lost;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		if (!checkName(nickname))
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

	public int getIs_yellow_vip() {
		return is_yellow_vip;
	}

	public void setIs_yellow_vip(int is_yellow_vip) {
		this.is_yellow_vip = is_yellow_vip;
	}

	public int getIs_yellow_year_vip() {
		return is_yellow_year_vip;
	}

	public void setIs_yellow_year_vip(int is_yellow_year_vip) {
		this.is_yellow_year_vip = is_yellow_year_vip;
	}

	public int getYellow_vip_level() {
		return yellow_vip_level;
	}

	public void setYellow_vip_level(int yellow_vip_level) {
		this.yellow_vip_level = yellow_vip_level;
	}

	public int getIs_yellow_high_vip() {
		return is_yellow_high_vip;
	}

	public void setIs_yellow_high_vip(int is_yellow_high_vip) {
		this.is_yellow_high_vip = is_yellow_high_vip;
	}

	public boolean isIs_blue_vip() {
		return is_blue_vip;
	}

	public void setIs_blue_vip(boolean is_blue_vip) {
		this.is_blue_vip = is_blue_vip;
	}

	public boolean isIs_blue_year_vip() {
		return is_blue_year_vip;
	}

	public void setIs_blue_year_vip(boolean is_blue_year_vip) {
		this.is_blue_year_vip = is_blue_year_vip;
	}

	public boolean isIs_super_blue_vip() {
		return is_super_blue_vip;
	}

	public void setIs_super_blue_vip(boolean is_super_blue_vip) {
		this.is_super_blue_vip = is_super_blue_vip;
	}

	public int getBlue_vip_level() {
		return blue_vip_level;
	}

	public void setBlue_vip_level(int blue_vip_level) {
		this.blue_vip_level = blue_vip_level;
	}

	public static boolean checkName(String name) {
		if (isMatches("[\\w]*", name))
			return false;
		return isMatches("[\u1F601-\u1F64F]+", name);
	}

	public static boolean isMatches(String regex, String str) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		boolean b = m.matches();
		return b;
	}
}
