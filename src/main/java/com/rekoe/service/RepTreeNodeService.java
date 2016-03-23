/**
 * 
 */
package com.rekoe.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNNodeKind;

import com.rekoe.tree.entity.Tree;
import com.rekoe.tree.entity.TreeNode;
import com.rekoe.tree.service.AbstractTreeNodeService;

/**
 * 仓库目录结构树节点服务层
 * 
 */
@IocBean
public class RepTreeNodeService extends AbstractTreeNodeService {

	private static final String AND = "$AND$";
	/**
	 * 日志
	 */
	private final Log LOG = Logs.get();

	/**
	 * 仓库服务层
	 */
	@Inject
	RepositoryService repositoryService;

	@Override
	protected List<TreeNode> getTreeNodes(Tree parent, Map<String, Object> parameters) {
		List<TreeNode> results = new ArrayList<TreeNode>();
		String pj = (String) parameters.get("pj");
		String path = (String) parameters.get("path");
		path = StringUtils.replace(path, AND, "&");
		if (StringUtils.isBlank(pj)) {
			LOG.warn("pj id is blank ");
			return null;
		}
		try {
			Collection<SVNDirEntry> entries = this.repositoryService.getDir(pj, path);
			if (entries == null) {
				return null;
			}
			for (SVNDirEntry svnDirEntry : entries) {
				TreeNode treeNode = new TreeNode(svnDirEntry.getName());
				treeNode.setLeaf(SVNNodeKind.FILE.equals(svnDirEntry.getKind()));// 叶子?
				treeNode.addParamete("pj", pj);
				if (path.endsWith("/")) {
					treeNode.addParamete("path", path + StringUtils.replace(svnDirEntry.getName(), "&", AND));
				} else {
					treeNode.addParamete("path", path + "/" + StringUtils.replace(svnDirEntry.getName(), "&", AND));
				}
				results.add(treeNode);
			}
			Collections.sort(results);// 排序
		} catch (Exception e) {
			LOG.error(e);
			results.clear();
			TreeNode errorNode = new TreeNode(e.getMessage());
			errorNode.setLeaf(true);
			results.add(errorNode);
			return results;
		}
		return results;
	}
}