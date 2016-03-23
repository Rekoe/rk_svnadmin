package com.rekoe.tree.entity;

import java.io.Serializable;

/**
 * 树
 */
public class Tree implements Serializable {
	/**
	 * 序列化ID
	 */
	private static final long serialVersionUID = -785319127620330061L;
	/**
	 * ID
	 */
	private String id;
	/**
	 * 父节点ID
	 */
	private String parentId;
	/**
	 * 是否是叶子
	 */
	private boolean leaf;

	/**
	 * 默认构造函数
	 */
	public Tree() {
	}

	/**
	 * 构造函数
	 * 
	 * @param id
	 *            ID
	 * @param parentId
	 *            父节点ID
	 * @param treeNodeService
	 *            节点服务层
	 */
	public Tree(String id, String parentId) {
		this.id = id;
		this.parentId = parentId;
	}

	/**
	 * @return ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            ID
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return 父节点ID
	 */
	public String getParentId() {
		return parentId;
	}

	/**
	 * @param parentId
	 *            父节点ID
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return 是否是叶子
	 */
	public boolean isLeaf() {
		return leaf;
	}

	/**
	 * @param leaf
	 *            是否是叶子
	 */
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

}
