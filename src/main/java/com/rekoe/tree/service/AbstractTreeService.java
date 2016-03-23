package com.rekoe.tree.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.log.Logs;

import com.rekoe.service.DefaultTreeFactory;
import com.rekoe.service.RepTreeNodeService;
import com.rekoe.tree.entity.Tree;

/**
 * 抽象树服务层
 * 
 */
public abstract class AbstractTreeService implements TreeService {

	/**
	 * 日志
	 */
	private static final org.nutz.log.Log LOG = Logs.get();

	@Inject
	private DefaultTreeFactory defaultTreeFactory;
	
	@Inject
	private RepTreeNodeService repTreeNodeService;
	public String getHTML(Map<String, Object> parameters) {
		try {
			String treeId = (String) parameters.get(TREE_ID_VAR);
			String parentId = (String) parameters.get(TREE_PARENTID_VAR);

			if (StringUtils.isBlank(treeId) && StringUtils.isBlank(parentId)) {
				return null;
			}
			StringBuffer html = new StringBuffer();
			if (StringUtils.isNotBlank(parentId)) {
				// 找出所有的子树
				List<Tree> treeList = defaultTreeFactory.findChildren(parentId);
				for (Tree tree : treeList) {
					if (tree == null) {
						continue;
					}
					parseTree(html, tree, parameters);
				}
			} else if (StringUtils.isNotBlank(treeId)) {
				// 说明是第一层
				Tree tree = defaultTreeFactory.find(treeId);
				if (tree == null) {
					LOG.info("not found tree. id = " + treeId);
					return null;
				}
				parseTree(html, tree, parameters);
			}
			return html.toString();
		} catch (Exception e) {
			LOG.error(e);
			return null;
		}
	}

	/**
	 * @param treeHtml
	 *            html
	 * @param tree
	 *            树
	 * @param parameters
	 *            参数
	 */
	protected void parseTree(StringBuffer treeHtml, Tree tree, Map<String, Object> parameters) {
		StringBuffer html;
		try {
			html = repTreeNodeService.getHTML(tree, parameters);
		} catch (Exception e) {
			LOG.error(e);
			html = null;
		} finally {
		}
		if (html == null) {
			LOG.debug("not found tree html data." + tree);
			return;
		}
		treeHtml.append(html);
	}
}
