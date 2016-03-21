package com.rekoe.mobile.provider.sg;

import org.nutz.ioc.loader.annotation.IocBean;

import com.rekoe.annotation.PlatformProvider;
import com.rekoe.mobile.Profile;
import com.rekoe.mobile.provider.AbstractProvider;
import com.rekoe.utils.Constants;

@IocBean
@PlatformProvider(name = Constants.SG)
public class SGProvider extends AbstractProvider<SGParam> {

	private static final long serialVersionUID = 2882126009485835543L;

	@Override
	public Profile verifyResponse(SGParam param) throws Exception {
		return new Profile(getProviderId(), param.getOpenid(), param.getPid());
	}

	@Override
	public String getProviderId() {
		return Constants.SG;
	}

}
