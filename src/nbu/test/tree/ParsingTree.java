package nbu.test.tree;

import java.util.ArrayList;
import java.util.List;
import nbu.nlp.examples.Relationship;

public class ParsingTree {
	public static void tree(List<Relationship> relation,int id,List<TreeNode> treelist)throws Exception{
		
		List<Integer> list=new ArrayList<Integer>();
		for(int i=0;i<relation.size();i++){
			if(relation.get(i).getsecondId()==relation.get(id).getfirstId()){
				treelist.add(new TreeNode(relation.get(i).getfirstId(),relation.get(id).getfirstId(),relation.get(i).getfirstCont()));
				list.add(relation.get(i).getfirstId());
			}
		}
		for(int j=0;j<list.size();j++){
			ParsingTree.tree(relation,list.get(j),treelist);
		}
	
}

}
