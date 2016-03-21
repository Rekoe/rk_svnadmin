package com.rekoe.filter;

import javax.servlet.http.HttpServletResponse;

import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionFilter;
import org.nutz.mvc.View;

public abstract class AbstractCrossOriginFilter implements ActionFilter {

	@Inject
	protected PropertiesProxy conf;

	@Override
	public View match(ActionContext ac) {
		if (on()) {
			HttpServletResponse response = ac.getResponse();
			addHeader(response);
		}
		return null;
	}

	protected abstract boolean on();
	
	protected abstract void addHeader(HttpServletResponse response);
}
