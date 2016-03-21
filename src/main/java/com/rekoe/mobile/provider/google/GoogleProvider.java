package com.rekoe.mobile.provider.google;

import org.nutz.ioc.loader.annotation.IocBean;

import com.rekoe.annotation.PlatformProvider;
import com.rekoe.mobile.provider.googlep.GooglePProvider;
import com.rekoe.utils.Constants;

@IocBean
@PlatformProvider(name = Constants.GOOGLE)
public class GoogleProvider extends GooglePProvider {

	private static final long serialVersionUID = 7944880275608182543L;

	@Override
	public String getProviderId() {
		return Constants.GOOGLE;
	}

}
