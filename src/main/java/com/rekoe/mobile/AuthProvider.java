package com.rekoe.mobile;

public abstract class AuthProvider<T extends AbstractParam> {

	public abstract String getProviderId();

	public abstract Profile verifyResponse(T param) throws Exception;

	public abstract Class<T> getEntityClass();

}
