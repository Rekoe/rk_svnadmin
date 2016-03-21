package com.rekoe.mobile.provider.google;

import java.io.Serializable;

import com.rekoe.mobile.provider.googlep.GooglePParam;
import com.rekoe.utils.Constants;

public class GoogleParam extends GooglePParam implements Serializable {

	private static final long serialVersionUID = -1122775243733543089L;

	@Override
	public String getProviderId() {
		return Constants.GOOGLE;
	}
}
