package com.rekoe.domain;

import java.io.Serializable;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.EL;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;
import org.nutz.json.JsonField;

@Table("sg_oauth_user")
@TableIndexes({ @Index(name = "sg_user_data_index", fields = { "userName" }, unique = true), @Index(name = "sg_user_phone_index", fields = { "userName", "phoneCode" }, unique = false) })
public class OAuthUser implements Serializable {

	private static final long serialVersionUID = -2879607754801933480L;

	@Name
	@Prev(els = @EL("uuid()"))
	@JsonField(ignore = true)
	private String id;

	@Column(hump = true)
	@JsonField(value = "name")
	private String userName;

	@Column
	@JsonField(ignore = true)
	private String password;

	@Column
	@JsonField(ignore = true)
	private String salt;

	@Column(hump = true)
	@ColDefine(type = ColType.INT, width = 16)
	@JsonField(ignore = true)
	private long phoneCode;

	@Column(hump = true)
	@ColDefine(type = ColType.INT, width = 16)
	@JsonField(value = "uid")
	private long userId;

	@Column("is_locked")
	@JsonField(value = "locked")
	private boolean locked;

	@Column("is_bind")
	@JsonField(ignore = true)
	@Default("0")
	private boolean bind;

	@Column(hump = true)
	@Comment("绑定前的渠道")
	private String bindProviderId;

	public String getBindProviderId() {
		return bindProviderId;
	}

	public void setBindProviderId(String bindProviderId) {
		this.bindProviderId = bindProviderId;
	}

	public boolean isBind() {
		return bind;
	}

	public void setBind(boolean bind) {
		this.bind = bind;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public long getPhoneCode() {
		return phoneCode;
	}

	public void setPhoneCode(long phoneCode) {
		this.phoneCode = phoneCode;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}
}