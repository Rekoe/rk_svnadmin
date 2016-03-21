package com.rekoe.valueadaptor;


public class StringJsonAdaptor extends JsonValueAdaptor {

	@Override
	public Class<?> JsonClassType() {
		return String.class;
	}
}
