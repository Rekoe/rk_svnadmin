package com.rekoe.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Many;
import org.nutz.dao.entity.annotation.ManyMany;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;
import org.nutz.lang.util.NutMap;

import com.rekoe.valueadaptor.NutMapValueAdaptor;
import com.rekoe.valueadaptor.StringJsonAdaptor;

@Table("system_server")
@TableIndexes({ @Index(name = "g_pid_index", fields = { "pid" }, unique = true) })
public class GameServer implements Serializable {

	private static final long serialVersionUID = 4391955362256919755L;

	@Id
	private int id;

	@Column(hump = true)
	@Comment("平台名称")
	private String platformName;

	@Column("platform_id")
	@Comment("平台编号")
	private int pid;

	@Column("is_open")
	@Comment("是否开启")
	private boolean open;

	@Column(hump = true)
	@Comment("充值地址")
	@ColDefine(width = 225)
	private String payUrl;

	@Column(hump = true)
	@Comment("登陆服地址")
	@ColDefine(width = 225)
	private String loginUrl;

	@ManyMany(target = User.class, relation = "system_user_server", from = "SERVERID", to = "USERID")
	private List<User> users;

	@Many(target = OfficialServer.class, field = "pid")
	private List<OfficialServer> officialServers;

	@Column("ver")
	@Comment("当前版本号")
	private String version;

	@Column
	@Comment("是否提审")
	@ColDefine(type = ColType.BOOLEAN)
	private boolean verify;

	@Column("is_black_open")
	@Comment("是否开启白名单")
	@ColDefine(type = ColType.BOOLEAN)
	private boolean blackOpen;

	@Column("black_list")
	@Comment("白名单列表")
	@ColDefine(type = ColType.TEXT, adaptor = StringJsonAdaptor.class)
	private List<String> items = new ArrayList<String>();

	@Column(hump = true)
	private String clientSecret;

	@Column(hump = true)
	@Comment("认证方式")
	@Default("DEFAULT")
	protected AuthType authType;

	@Column(hump = true)
	@ColDefine(type = ColType.TEXT, adaptor = NutMapValueAdaptor.class)
	private NutMap mobileAuth;

	public NutMap getMobileAuth() {
		if (mobileAuth == null) {
			mobileAuth = NutMap.NEW();
		}
		return mobileAuth;
	}

	public void setMobileAuth(NutMap config) {
		this.mobileAuth = config;
	}

	public GameServer addMobileAuth(String id, NutMap config) {
		this.mobileAuth.addv(id, config);
		return this;
	}

	public boolean isBlackOpen() {
		return blackOpen;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public void setBlackOpen(boolean black) {
		this.blackOpen = black;
	}

	public List<String> getItems() {
		return items;
	}

	public AuthType getAuthType() {
		return authType;
	}

	public void setAuthType(AuthType authType) {
		this.authType = authType;
	}

	public void setItems(List<String> items) {
		this.items = items;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public boolean isVerify() {
		return verify;
	}

	public void setVerify(boolean verify) {
		this.verify = verify;
	}

	public List<OfficialServer> getOfficialServers() {
		return officialServers;
	}

	public void setOfficialServers(List<OfficialServer> officialServers) {
		this.officialServers = officialServers;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPlatformName() {
		return platformName;
	}

	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getPayUrl() {
		return payUrl;
	}

	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public boolean isUseMobileAuth() {
		return getMobileAuth().getBoolean("mobileAuth", false);
	}
}