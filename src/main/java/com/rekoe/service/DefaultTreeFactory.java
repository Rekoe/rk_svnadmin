/**
 * 
 */
package com.rekoe.service;

import java.util.ArrayList;
import java.util.List;

import org.nutz.ioc.loader.annotation.IocBean;

import com.rekoe.tree.entity.Tree;
import com.rekoe.tree.service.TreeFactory;


/**
 * 默认的树工厂类
 * 
 */
@IocBean
public class DefaultTreeFactory implements TreeFactory {

	public Tree find(String id) {
		for(Tree  tree:datas){
			if(tree.getId().equals(id)){
				//要给leaf设值
				if(tree.getId().equals(tree.getParentId())){
					tree.setLeaf(false);
				}else{
					tree.setLeaf(findChildren(tree.getId()).size()==0);
				}
				return tree;
			}
		}
		return null;
	}
	
	public List<Tree> findChildren(String parentId) {
		List<Tree> results = new ArrayList<Tree>();
		for(Tree  tree:datas){
			if(parentId.equals(tree.getParentId())){
				//要给leaf设值
				if(tree.getId().equals(tree.getParentId())){
					tree.setLeaf(false);
					results.add(tree);
					return results;
				}else{
					tree.setLeaf(findChildren(tree.getId()).size()==0);
					results.add(tree);
				}
			}
		}
		return results;
	}
	
	
	/**
	 * 
	 */
	private static List<Tree> datas = new ArrayList<Tree>();
	static{
		/*
		 * rep
		 *   |rep
		 *   |    |rep 
		 *   |    |rep
		 *   |rep
		 *   |
		 * rep
		 *   |    
		 */
		datas.add(new Tree("rep","rep"));
		/*
		 * com
		 *   |_dept
		 *   |   |_dept user
		 *   |   |_dept user
		 *   |_dept
		 *   |_com user
		 *   |_com user
		 * 
		 */
//		datas.add(new Tree("com",null));
		
//		datas.add(new Tree("dept","com"));
//		datas.add(new Tree("user","com"));
		
//		datas.add(new Tree("user","dept"));
	}

}
