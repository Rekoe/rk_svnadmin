package com.rekoe.mobile.provider;

public class PlatformProvider {

	private final String providerId;
	private final String name;
	private final String pfid;

	public PlatformProvider(final String providerId, final String name, final String pfid) {
		this.providerId = providerId;
		this.name = name;
		this.pfid = pfid;
	}

	public String getProviderId() {
		return providerId;
	}

	public String getName() {
		return name;
	}

	public String getPfid() {
		return pfid;
	}

}
