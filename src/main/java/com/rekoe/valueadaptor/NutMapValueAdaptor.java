package com.rekoe.valueadaptor;

import org.nutz.lang.util.NutMap;

public class NutMapValueAdaptor extends MapValueAdaptor {

	public Class<?> JsonClassType() {
		return NutMap.class;
	}
}
