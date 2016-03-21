package com.rekoe.mobile.provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nutz.lang.Lang;

public class PlatformProviderFactory {

	private Map<String, PlatformProvider> platforms = new HashMap<String, PlatformProvider>();

	public void setPlatformProviders(List<List<String>> platformProviders) {
		int len = platformProviders.size();
		for (int i = 0; i < len; i++) {
			List<String> args = platformProviders.get(i);
			int j = args.size();
			if (j != 3) {
				continue;
			}
			platforms.put(args.get(1), new PlatformProvider(args.get(0), args.get(2), args.get(1)));
		}
	}

	public PlatformProvider getPlatformProvider(String pfid) {
		PlatformProvider pf = platforms.get(pfid);
		if (Lang.isEmpty(pf)) {
			Lang.makeThrow("Can`t define type[%s]", pfid);
		}
		return pf;
	}
}