package com.rekoe.tree.service;

import java.util.List;

import com.rekoe.tree.entity.Tree;

/**
 * 树工厂类
 * @author <a href="mailto:yuanhuiwu@gmail.com">Huiwu Yuan</a>
 * @since 3.0.2
 * 
 */
public interface TreeFactory {

	/**
	 * @param id 树ID
	 * @return 查找树
	 */
	Tree find(String id);

	/**
	 * 查找子树
	 * 
	 * @param parentId 父树ID
	 * @return 树的子树
	 */
	List<Tree> findChildren(String parentId);

}
