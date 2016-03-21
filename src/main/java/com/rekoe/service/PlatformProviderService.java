package com.rekoe.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nutz.ioc.Ioc;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.resource.Scans;

import com.rekoe.annotation.PlatformProvider;
import com.rekoe.mobile.AbstractParam;
import com.rekoe.mobile.provider.AbstractProvider;

@IocBean
public class PlatformProviderService {

	private Map<String, AbstractProvider<AbstractParam>> providersMap = new HashMap<>();

	@Inject("refer:$ioc")
	private Ioc ioc;

	@SuppressWarnings("unchecked")
	public void init() {
		List<Class<?>> clazzList = Scans.me().scanPackage("com.rekoe.mobile.provider");
		for (Class<?> clzz : clazzList) {
			PlatformProvider platformProvider = clzz.getAnnotation(PlatformProvider.class);
			if (Lang.isEmpty(platformProvider)) {
				continue;
			}
			AbstractProvider<AbstractParam> provider = (AbstractProvider<AbstractParam>) ioc.get(clzz);
			providersMap.put(platformProvider.name(), provider);
		}
	}

	public AbstractProvider<AbstractParam> getAbstractProvider(String key) {
		return providersMap.get(key);
	}
}
