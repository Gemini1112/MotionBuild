/*
 * Copyright Walker Studio
 * All Rights Reserved.
 * 
 * 文件名称： ManyNodeTree.java
 * 摘 要：
 * 作 者： Walker
 * 创建时间： 2013-03-19
 */
package nbu.test.tree;

import java.util.List;




/**
 * 多叉树生成、遍历工具
 * 
 * @author Walker
 * @version 1.0.0.0
 */
public class ManyNodeTree 
{
	/** 树根*/
	private ManyTreeNode root;
	public  int count=0;
	/**
	 * 构造函数
	 */
	public ManyNodeTree()
	{
		root = new ManyTreeNode(new TreeNode(-1));
	}
	
	/**
	 * 生成一颗多叉树，根节点为root
	 * 
	 * @param treeNodes 生成多叉树的节点集合
	 * @return ManyNodeTree
	 */
	public ManyNodeTree createTree(List<TreeNode> treeNodes)
	{
		if(treeNodes == null || treeNodes.size() < 0)
			return null;
		
		ManyNodeTree manyNodeTree =  new ManyNodeTree();
		
		//将所有节点添加到多叉树中
		for(TreeNode treeNode : treeNodes)
		{
			if(treeNode.getParentId()==-1)
			{
				//向根添加一个节点
				manyNodeTree.getRoot().getChildList().add(new ManyTreeNode(treeNode));
			}
			else
			{
				addChild(manyNodeTree.getRoot(), treeNode);
			}
		}
		
		return manyNodeTree;
	}
	
	/**
	 * 向指定多叉树节点添加子节点
	 * 
	 * @param manyTreeNode 多叉树节点
	 * @param child 节点
	 */
	public void addChild(ManyTreeNode manyTreeNode, TreeNode child)
	{
		for(ManyTreeNode item : manyTreeNode.getChildList())
		{
			if(item.getData().getNodeId()==child.getParentId())
			{
				//找到对应的父亲
				item.getChildList().add(new ManyTreeNode(child));
				break;
			}
			else
			{
				if(item.getChildList() != null && item.getChildList().size() > 0)
				{
					addChild(item, child);
				}				
			}
		}
	}
	
	/**
	 * 遍历多叉树 
	 * 这是先序遍历
	 * @param manyTreeNode 多叉树节点
	 * @return 
	 */
	public String iteratorTree(ManyTreeNode manyTreeNode)
	{
		
		StringBuilder buffer = new StringBuilder();
		buffer.append("\n");
		
		if(manyTreeNode != null) 
		{	
			for (ManyTreeNode index : manyTreeNode.getChildList()) 
			{
				count++;
				buffer.append(count+"、"+index.getData().getText()+ ",");
				
				if (index.getChildList() != null && index.getChildList().size() > 0 ) 
				{	
					buffer.append(iteratorTree(index));
				}
			}
		}
		
		buffer.append("\n");
		
		return buffer.toString();
	}
	
	
	public  int getsearch(int id,ManyTreeNode manyTreeNode)throws Exception{
		if(manyTreeNode != null) 
		{	
			for (ManyTreeNode index : manyTreeNode.getChildList()) 
			{
				count++;
				if(index.getData().getNodeId()==id){
					break;
				}
				if (index.getChildList() != null && index.getChildList().size() > 0 ) 
				{	
					getsearch(id,index);
				}
			}
		}
		return count;
	}
	
	
	
	
	public ManyTreeNode getRoot() {
		return root;
	}

	public void setRoot(ManyTreeNode root) {
		this.root = root;
	}
	
	public static void main(String[] args)
	{
		/*
		List<TreeNode> treeNodes = new ArrayList<TreeNode>();
			treeNodes.add(new TreeNode("A", "root"));
			treeNodes.add(new TreeNode("B", "A"));
			treeNodes.add(new TreeNode("C", "A"));
			treeNodes.add(new TreeNode("D", "A"));
			treeNodes.add(new TreeNode("E", "B"));
			treeNodes.add(new TreeNode("F", "D"));
			
		
			ManyNodeTree tree = new ManyNodeTree();
			
			System.out.println(tree.iteratorTree(tree.createTree(treeNodes).getRoot()));
			*/
	}
	
}
