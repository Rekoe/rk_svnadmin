package com.rekoe.mvc.view;

import org.nutz.ioc.Ioc;
import org.nutz.mvc.View;
import org.nutz.mvc.ViewMaker;

public class DataDownViewMaker implements ViewMaker {

	public View make(Ioc ioc, String type, String value) {
		if ("down".equals(type)) {
			return new DataDownView();
		}
		return null;
	}

}
