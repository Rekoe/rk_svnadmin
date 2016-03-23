/**
 * 
 */
package com.rekoe.service;

import java.util.Map;

import org.nutz.ioc.loader.annotation.IocBean;

import com.rekoe.tree.service.AbstractTreeService;

/**
 * 默认的树服务层
 * 
 */
@IocBean(name = "treeService")
public class DefaultTreeService extends AbstractTreeService implements AjaxService {


	public com.rekoe.domain.Ajax execute(Map<String, Object> parameters) {
		com.rekoe.domain.Ajax result = new com.rekoe.domain.Ajax();
		result.setContentType(CONTENTTYPE_HTML);
		result.setResult(this.getHTML(parameters));
		return result;
	}

}
