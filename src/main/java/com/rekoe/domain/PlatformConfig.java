package com.rekoe.domain;

import java.io.Serializable;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.EL;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;
import org.nutz.lang.util.NutMap;

import com.rekoe.valueadaptor.NutMapValueAdaptor;

@Table("platform_config")
@TableIndexes({ @Index(name = "p_pid_index", fields = { "sid", "provider" }, unique = true) })
public class PlatformConfig implements Serializable {

	private static final long serialVersionUID = -4694161925835479316L;

	@Name
	@Prev(els = @EL("uuid()"))
	private String id;

	@Column
	private int sid;

	@Column
	@Comment("平台")
	private String provider;

	@Column
	@ColDefine(type = ColType.TEXT, adaptor = NutMapValueAdaptor.class)
	private NutMap config;

	public PlatformConfig() {
		super();
	}

	public PlatformConfig(int sid, NutMap config) {
		super();
		this.sid = sid;
		this.provider = config.getString("provider");
		this.config = config;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public NutMap getConfig() {
		if (config == null) {
			config = NutMap.NEW();
		}
		return config;
	}

	public void setConfig(NutMap config) {
		this.config = config;
	}

	public PlatformConfig addPlatform(String id, NutMap config) {
		this.config.addv(id, config);
		return this;
	}
}
