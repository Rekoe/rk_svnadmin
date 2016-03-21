package com.rekoe.domain;

import java.io.Serializable;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

@Table("sg_oauth_client")
@TableIndexes({ @Index(name = "sg_oauth_client_index", fields = { "clientName" }, unique = true), @Index(name = "sg_oc_secret_index", fields = { "clientId", "clientSecret" }, unique = false) })
public class OAuthClient implements Serializable {

	private static final long serialVersionUID = 2345879369118031587L;
	@Id
	private long id;

	@Column(hump = true)
	private String clientName;

	@Column(hump = true)
	private String clientId;

	@Column(hump = true)
	private String clientSecret;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	@Override
	public String toString() {
		return "Client{" + "id=" + id + ", clientName='" + clientName + '\'' + ", clientId='" + clientId + '\'' + ", clientSecret='" + clientSecret + '\'' + '}';
	}
}
