package org.nutz.web.ajax;

import org.nutz.lang.util.NutMap;
import org.nutz.web.ajax.AjaxReturn;

public abstract class Ajax {

	public static AjaxReturn ok() {
		AjaxReturn re = new AjaxReturn();
		re.setOk(true);
		return re;
	}

	public static AjaxReturn fail() {
		AjaxReturn re = new AjaxReturn();
		re.setOk(false);
		return re;
	}

	public static AjaxReturn expired() {
		AjaxReturn re = new AjaxReturn();
		re.setOk(false);
		re.setMsg("ajax.expired");
		return re;
	}

	/**
	 * @return 获得一个map，用来存放返回的结果。
	 */
	public static NutMap one() {
		return new NutMap();
	}

}
