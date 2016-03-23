package com.rekoe.service;

import java.util.Map;


/**
 * ajax服务层接口
 */
public interface AjaxService {
	/**
	 * html
	 */
	public static final String CONTENTTYPE_HTML="text/html; charset=UTF-8";
	/**
	 * xml
	 */
	public static final String CONTENTTYPE_XML="text/xml; charset=UTF-8";
	/**
	 * json
	 */
	public static final String CONTENTTYPE_JSON="application/json; charset=UTF-8";
	/**
	 * javascript
	 */
	public static final String CONTENTTYPE_JAVASCRIPT="text/javascript; charset=UTF-8";
	/**
	 * text
	 */
	public static final String CONTENTTYPE_TEXT="text/plain; charset=UTF-8";
	
	/**
	 * 执行逻辑
	 * @param parameters 参数
	 * @return 结果
	 */
	com.rekoe.domain.Ajax execute(Map<String,Object> parameters);
}
