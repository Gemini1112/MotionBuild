package nbu.nlp.examples;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List; 
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
//import java.io.FileNotFoundException;
import java.io.InputStreamReader;

import nbu.file.txt.write.Text;
import nbu.test.tree.ManyNodeTree;
import nbu.test.tree.TreeNode;
import pro.mysql.connection.*;
import edu.hit.ir.ltpService.LTML;
import edu.hit.ir.ltpService.LTPOption;
import edu.hit.ir.ltpService.LTPService;
import edu.hit.ir.ltpService.SRL;
import edu.hit.ir.ltpService.Word;

public class MotionParsing {
	
	public static void main(String args[])throws Exception{
		
//		new MotionParsing().MotionComput("E:\\test0.txt","E:/llp.txt");
        
    }
	
	public static String isWord(String word)throws Exception{
		
		String happy="happy";
		String good="good";
		String angery="angery";
		String sad="sad";
		String fear="fear";
		String bad="bad";
		String shock="shock";
		
		List<String> texthappy=new ArrayList<String>();
		List<String> textgood=new ArrayList<String>();
		List<String> textanger=new ArrayList<String>();
		List<String> textsad=new ArrayList<String>();
		List<String> textfear=new ArrayList<String>();
		List<String> textbad=new ArrayList<String>();
		List<String> textshock=new ArrayList<String>();
		
		
		
		Statement stmt = null ;
		ResultSet rs1 = null,rs2=null,rs3=null,rs4=null,rs5=null,rs6=null,rs7=null;
		Connection con = null ;
		
		String driver=MySqlConnection.getDriver();
		String url=MySqlConnection.getUrl();
		String username=MySqlConnection.getUser();
		String password=MySqlConnection.getPassword();
		
		String slt_happy="select text from cidian where (class='PA' or class='PE') and polarity!=0";
		String slt_good="select text from cidian where (class='PD' or class='PH' or class='PG' or class='PB' or class='Pk') and and polarity!=0 ";
		String slt_anger="select text from cidian where class='NA' and polarity!=0";
		String slt_sad="select text from cidian where (class='NB' or class='NJ' or class='NH' or class='PF') and polarity!=0";
		String slt_fear="select text from cidian where (class='NI' or class='NC' or class='NG') and polarity!=0";
		String slt_bad="select text from cidian where (class='NE' or class='ND' or class='NN' or class='NK' or class='NL') and polarity!=0";
		String slt_shock="select text from cidian where class='PC' and polarity!=0";
		
		try{
			Class.forName (driver);
			con = DriverManager.getConnection (url, username, password); 
			stmt = con.createStatement();
			
			rs1=stmt.executeQuery(slt_happy);
			while(rs1.next()){
				texthappy.add(rs1.getString("text"));
			}
			rs2=stmt.executeQuery(slt_good);
			while(rs2.next()){
				textgood.add(rs2.getString("text"));
			}
			rs3=stmt.executeQuery(slt_anger);
			while(rs3.next()){
				textanger.add(rs3.getString("text"));
			}
			rs4=stmt.executeQuery(slt_sad);
			while(rs4.next()){
				textsad.add(rs4.getString("text"));
			}
			rs5=stmt.executeQuery(slt_fear);
			while(rs5.next()){
				textfear.add(rs5.getString("text"));
			}
			rs6=stmt.executeQuery(slt_bad);
			while(rs6.next()){
				textbad.add(rs6.getString("text"));
			}
			rs7=stmt.executeQuery(slt_shock);
			while(rs7.next()){
				textshock.add(rs7.getString("text"));
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		for(int i=0;i<texthappy.size();i++){
			if(texthappy.get(i).equals(word)){
				return happy;
			}
		}
		for(int i=0;i<textgood.size();i++){
			if(textgood.get(i).equals(word)){
				return good;
			}
		}
		for(int i=0;i<textanger.size();i++){
			if(textanger.get(i).equals(word)){
				return angery;
			}
		}
		for(int i=0;i<textsad.size();i++){
			if(textsad.get(i).equals(word)){
				return sad;
			}
		}
		for(int i=0;i<textfear.size();i++){
			if(textfear.get(i).equals(word)){
				return fear;
			}
		}
		for(int i=0;i<textbad.size();i++){
			if(textbad.get(i).equals(word)){
				return bad;
			}
		}
		for(int i=0;i<textshock.size();i++){
			if(textshock.get(i).equals(word)){
				return shock;
			}
		}
		return "neutral";
	}
	
	
	public static Relationship isADV(int id,ArrayList<Relationship> list)throws Exception{
		
		for(int i=0;i<list.size();i++){
			if(list.get(i).getsecondId()==id&&list.get(i).getrelation().equals("ADV")){
				return list.get(i);
			}
		}
		return null;
		
	}
	
			
	public static void tree(List<Relationship> relation,int id,List<TreeNode> treelist)throws Exception{
		
			List<Integer> list=new ArrayList<Integer>();
			for(int i=0;i<relation.size();i++){
				if(relation.get(i).getsecondId()==relation.get(id).getfirstId()){
					treelist.add(new TreeNode(relation.get(i).getfirstId(),relation.get(id).getfirstId(),relation.get(i).getfirstCont()));
					list.add(relation.get(i).getfirstId());
				}
			}
			for(int j=0;j<list.size();j++){
				MotionParsing.tree(relation,list.get(j),treelist);
			}
		
	}
	
	
	public static ArrayList<Relationship> ADV(ArrayList<Relationship> relation)throws Exception{
		ArrayList<Relationship> relationADVlist=new ArrayList<Relationship>();
		
		for(int i=0;i<relation.size();i++){
			Relationship relationADV=new Relationship();
			if(relation.get(i).getrelation().equals("ADV")&&(MotionParsing.isWord(relation.get(i).getsecondCont()))!=null){
				relationADV.setfirstId(relation.get(i).getfirstId());
				relationADV.setfirstCont(relation.get(i).getfirstCont());
				relationADV.setfirstPos(relation.get(i).getfirstPos());
				relationADV.setsecondId(relation.get(i).getsecondId());
				relationADV.setsecondCont(relation.get(i).getsecondCont());
				relationADV.setsecondPos(relation.get(i).getsecondPos());
				relationADV.setrelation(relation.get(i).getrelation());
				if(MotionParsing.isWord(relation.get(i).getfirstCont()).equals("happy")||MotionParsing.isWord(relation.get(i).getfirstCont()).equals("good"))
					relationADV.setrawScore(1.0);
				else
					relationADV.setrawScore(-1.0);
				relationADVlist.add(relationADV);
			}
			else
			{
				if(relation.get(i).getrelation().equals("CMP")||relation.get(i).getrelation().equals("VOB")){
					if(MotionParsing.isWord(relation.get(i).getfirstCont())!=null){
						Relationship reship=new Relationship();
						if((reship=MotionParsing.isADV(relation.get(i).getsecondId(),relation))!=null){
							relationADV.setfirstId(reship.getfirstId());
							relationADV.setfirstCont(reship.getfirstCont());
							relationADV.setfirstPos(reship.getfirstPos());
							relationADV.setsecondId(relation.get(i).getfirstId());
							relationADV.setsecondCont(relation.get(i).getfirstCont());
							relationADV.setsecondPos(relation.get(i).getfirstPos());
							relationADV.setrelation("ADV");
							if(MotionParsing.isWord(relation.get(i).getfirstCont()).equals("happy")||MotionParsing.isWord(relation.get(i).getfirstCont()).equals("good"))
								relationADV.setrawScore(1.0);
							else
								relationADV.setrawScore(-1.0);
							relationADVlist.add(relationADV);
						}
					}
				}
			}
		}
		return relationADVlist;
	}
	
	
	public static double getscore(String tablename,String word)throws Exception{
		double score=0;
		
		Statement stmt = null ;
		ResultSet rs = null;
		Connection con = null ;
		
		String slt="select intensity from "+tablename+" where text='"+word+"'";
		
		String driver=MySqlConnection.getDriver();
		String url=MySqlConnection.getUrl();
		String username=MySqlConnection.getUser();
		String password=MySqlConnection.getPassword();
		
		
		try{
			Class.forName (driver);
			con = DriverManager.getConnection (url, username, password); 
			stmt = con.createStatement();
			rs=stmt.executeQuery(slt);
			if(rs.next()){
				score=rs.getDouble("intensity");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		if(MotionParsing.isWord(word).equals("positive"))
			return score;
		else
			return (0-score);
	}
	
	
	public static boolean isAdverb(String word)throws Exception{
		
		boolean ok=false;
		Statement stmt = null ;
		ResultSet rs = null;
		Connection con = null ;
		
		String driver=MySqlConnection.getDriver();
		String url=MySqlConnection.getUrl();
		String username=MySqlConnection.getUser();
		String password=MySqlConnection.getPassword();
		
		String selt="select * from fuci where text='"+word+"'";
		
		try{
			Class.forName (driver);
			con = DriverManager.getConnection (url, username, password); 
			stmt = con.createStatement();
			rs=stmt.executeQuery(selt);
			if(rs.next()){
				ok=true;
			}
			else{
				ok=false;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return ok;	
	}
	
	
	public static double getAdverbScore(String word)throws Exception{
		
		double score = 0;
		Statement stmt = null ;
		ResultSet rs = null;
		Connection con = null ;
		
		String driver=MySqlConnection.getDriver();
		String url=MySqlConnection.getUrl();
		String username=MySqlConnection.getUser();
		String password=MySqlConnection.getPassword();
		
		String selt="select score from fuci where text='"+word+"'";
		
		try{
			Class.forName (driver);
			con = DriverManager.getConnection (url, username, password); 
			stmt = con.createStatement();
			rs=stmt.executeQuery(selt);
			if(rs.next()){
				score = rs.getDouble("score");
			}else
				score=0;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return score;	
	}
	
	
	public static boolean isNot(String word)throws Exception{
		boolean ok=false;
		Statement stmt = null ;
		ResultSet rs = null;
		Connection con = null ;
		
		String driver=MySqlConnection.getDriver();
		String url=MySqlConnection.getUrl();
		String username=MySqlConnection.getUser();
		String password=MySqlConnection.getPassword();
		
		String selt="select * from negtive where text='"+word+"'";
		
		try{
			Class.forName (driver);
			con = DriverManager.getConnection (url, username, password); 
			stmt = con.createStatement();
			rs=stmt.executeQuery(selt);
			if(rs.next()){
				ok=true;
			}
			else
			{
				ok=false;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return ok;	
	}

	
	public static double getNotScore(String word)throws Exception{
		
		double score=0;
		Statement stmt = null ;
		ResultSet rs = null;
		Connection con = null ;
		
		String driver=MySqlConnection.getDriver();
		String url=MySqlConnection.getUrl();
		String username=MySqlConnection.getUser();
		String password=MySqlConnection.getPassword();
		
		String selt="select score from negtive where text='"+word+"'";
		
		try{
			Class.forName (driver);
			con = DriverManager.getConnection (url, username, password); 
			stmt = con.createStatement();
			rs=stmt.executeQuery(selt);
			if(rs.next()){
				score = rs.getDouble("score");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return score;	
	}

	
	public static ArrayList<Relationship> isADVWord(int id,ArrayList<Relationship> relationADV)throws Exception{
		ArrayList<Relationship> list=new ArrayList<Relationship>();
		for(int i=0;i<relationADV.size();i++)
		{
			if(relationADV.get(i).getsecondId()==id){
				Relationship relationship=relationADV.get(i);
				list.add(relationship);
			}
		}
		return list;
	}

	public  void MotionComput(String f,String dir,String motionType) throws Exception{
//		String f="E:\\test0.txt";
		File file=new File(f);
		FileInputStream fInputStream = new FileInputStream(file);
		List<String> Sentences=new ArrayList<String>();
		BufferedReader br = new BufferedReader(new InputStreamReader(fInputStream, "UTF-8"));
		String str=null;
		//读取每条微博，每条微博作为一个情感句
		while((str=br.readLine())!=null){
			Sentences.add(str);
		}
		br.close();
		//语言云认证
		LTPService ls = new LTPService("nbu13jsjfh@163.com:gFLSVHzo"); 
        try {
        	double Score=0;
        	int DocuementCount=0;
        	ArrayList<Double> sentenceScore=new ArrayList<Double>();//每个情感句的情感分值列表
        	int motionSentence_count=Sentences.size();//情感句个数
        	int sentenceZeroScoreCount=0;//得分为0的情感句条数
        	ArrayList<Integer> SentenceMotionWordCount=new ArrayList<Integer>();//每条情感句（微博）中包含的情感词个数
        	double DocuementMotionScore;//整个微博博文的最终情感倾向性得分，即最终得分
        	/*每一条博文算作一个情感句，对每个情感句作分析*/
        	int m=0;
        	for(;m<Sentences.size();m++){
        		ls.setEncoding(LTPOption.UTF8);
        		LTML ltml = ls.analyze(LTPOption.PARSER,Sentences.get(m));//调用语言云进行依存句分析
        		
        		int sentNum = ltml.countSentence();//注意：sentNum表示的是该条情感句中包含的句子数
        		
        		ArrayList<Double> wordSentimentScorelist=new ArrayList<Double>();//有修饰的情感词对组得分列表
        		ArrayList<Double> notSentimentScorelist=new ArrayList<Double>();//无修饰的情感对组得分列表
        		
        		double sentenceSentimentScore=0;//情感句修正情感得分
        		int ADVmotion_count=0;		//有修饰的情感词个数
        		int notADVmotion_count=0;	//无修饰的情感词个数
        		
        		
        		/*每条微博可能会有好几个句子，逐句进行分析计算出情感句倾向值*/
        		for(int i = 0; i< sentNum; ++i){
        			
        			ArrayList<Word> wordList = ltml.getWords(i);//分词结果保存在wordList中
        			
        			ArrayList<Relationship> relationshipList=new ArrayList<Relationship>();//关系对列表
            		
        			ArrayList<MotionWord> motionWordslist=new ArrayList<MotionWord>();//每句中情感词列表
        			Text.writerFileIsAppend(dir, ltml.getSentenceContent(i)+"\r\n");//该句写入文件
        			System.out.println(ltml.getSentenceContent(i));
        			/*分析该句的依存信息，分析关系对组*/
        			for(int j = 0; j < wordList.size(); ++j){
        				
        				Relationship relation=new Relationship();
        				
        				
        				if(wordList.get(j).getParserParent()!=-1){
        					//判断是否root节点，如果不是root节点
        					if(MotionParsing.isWord(wordList.get(j).getWS())!=null){
        						//该词是情感词，注意我们要的是含有情感词关系对组
        						
        						MotionWord word=new MotionWord();
        						word.setId(wordList.get(j).getID());
        						word.setText(wordList.get(j).getWS());
        						if(MotionParsing.isWord(wordList.get(j).getWS()).equals("happy")||MotionParsing.isWord(wordList.get(j).getWS()).equals("good"))
        							word.setScore(1.0);//积极的情感词初始分值：1.0；
        						else
        							word.setScore(-1.0);//消极的情感词分值是-1.0
        						
        						motionWordslist.add(word);
        					}
        					//设置关系对组
							relation.setfirstId(wordList.get(j).getID());
							relation.setfirstCont(wordList.get(j).getWS());
							relation.setfirstPos(wordList.get(j).getPOS());
							relation.setsecondId(wordList.get(j).getParserParent());
							relation.setsecondCont(wordList.get(wordList.get(j).getParserParent()).getWS());
							relation.setsecondPos(wordList.get(wordList.get(j).getParserParent()).getPOS());
							relation.setrelation(wordList.get(j).getParserRelation());
							relationshipList.add(relation);
						}else
						{	
							//判断是否root节点，如果是root节点
							if(MotionParsing.isWord(wordList.get(j).getWS())!=null){
								MotionWord word=new MotionWord();
        						word.setId(wordList.get(j).getID());
        						word.setText(wordList.get(j).getWS());
        						if(MotionParsing.isWord(wordList.get(j).getWS()).equals("happy")||MotionParsing.isWord(wordList.get(j).getWS()).equals("good"))
        							word.setScore(1.0);
        						else
        							word.setScore(-1.0);
        						motionWordslist.add(word);
        					}
							relation.setfirstId(wordList.get(j).getID());
							relation.setfirstCont(wordList.get(j).getWS());
							relation.setfirstPos(wordList.get(j).getPOS());
							relation.setsecondId(wordList.get(j).getParserParent());
							relation.setsecondCont("Root");
							relation.setsecondPos("null");
							relation.setrelation(wordList.get(j).getParserRelation());
							relationshipList.add(relation);
						}
        				
        				/*每个词的依存信息写入文件*/
        				Text.writerFileIsAppend(dir, 
        						"\t" + wordList.get(j).getWS()
        						+"\t" + wordList.get(j).getPOS()
        						+"\t" + wordList.get(j).getNE()
        						+"\t" + wordList.get(j).getParserParent()
        						+"\t" + wordList.get(j).getParserRelation());
//        				System.out.print("\t" + wordList.get(j).getWS());
//        				System.out.print("\t" + wordList.get(j).getPOS());
//        				System.out.print("\t" + wordList.get(j).getNE());
//        				System.out.print("\t" + wordList.get(j).getParserParent() + 
//        						"\t" + wordList.get(j).getParserRelation());
        				
        				if(ltml.hasSRL() && wordList.get(j).isPredicate()){
        					ArrayList<SRL> srls = wordList.get(j).getSRLs();
        					System.out.println();
        					for(int k = 0; k <srls.size(); ++k){
        						System.out.println("\t\t" + srls.get(k).type + 
        								"\t" + srls.get(k).beg +
        								"\t" + srls.get(k).end);
        						Text.writerFileIsAppend(dir, "\t\t" + srls.get(k).type + 
        								"\t" + srls.get(k).beg +
        								"\t" + srls.get(k).end);
        					}
        				}
        				Text.writerFileIsAppend(dir, "\r\n");
        				System.out.println();
        			
            		}
        			
        			/*依存句分析出来的关系对组（所有）写入文件*/
        			for(int s=0;s<relationshipList.size();s++){
        				Text.writerFileIsAppend(dir, "\t<"
        						+relationshipList.get(s).getfirstId()
        						+"\t"+relationshipList.get(s).getfirstCont()
        						+"\t"+relationshipList.get(s).getfirstPos()
        						+"\t"+relationshipList.get(s).getsecondId()
        						+"\t"+relationshipList.get(s).getsecondCont()
        						+"\t"+relationshipList.get(s).getsecondPos()
        						+"\t"+relationshipList.get(s).getrelation()+">"+"\r\n");
//						System.out.println("\t<"
//								+relationshipList.get(s).getfirstId()
//								+"\t"+relationshipList.get(s).getfirstCont()
//								+"\t"+relationshipList.get(s).getfirstPos()
//								+"\t"+relationshipList.get(s).getsecondId()
//								+"\t"+relationshipList.get(s).getsecondCont()
//								+"\t"+relationshipList.get(s).getsecondPos()
//								+"\t"+relationshipList.get(s).getrelation()+">");
					}
        			//建立一棵语法树
        			int id=0;
        			List<TreeNode> treeNodes = new ArrayList<TreeNode>();
					for(int r=0;r<relationshipList.size();r++){
						if(relationshipList.get(r).getsecondId()==-1){
        					treeNodes.add(new TreeNode(relationshipList.get(r).getfirstId(),-1,relationshipList.get(r).getfirstCont()));
							id=relationshipList.get(r).getfirstId();
							break;
        				}
					}
					MotionParsing.tree(relationshipList,id, treeNodes);
        			ManyNodeTree tree = new ManyNodeTree();
//        			System.out.println(tree.iteratorTree(tree.createTree(treeNodes).getRoot()));
        			
        			ArrayList<Relationship> relationADVlist=MotionParsing.ADV(relationshipList);//分析出ADV关系对组
        			/*计算出情感词关系对组的得分*/
        			double sita=1.0;
        			for(int k=0;k<motionWordslist.size();k++){
        				double wordSentimentScore;
        				double intense=1.0;
        				double ModifiedIntense=1.0;
//        				Text.writerFileIsAppend(dir, motionWordslist.get(k).getText()+"\r\n");
//        				System.out.println(motionWordslist.get(k).getText());
        				ArrayList<Relationship> relationlist=MotionParsing.isADVWord(motionWordslist.get(k).getId(), relationADVlist);
        				if(relationlist.size()!=0)
        				{
        					ADVmotion_count++;
        					for(int l=0;l<relationlist.size();l++){
        						Text.writerFileIsAppend(dir, "\tADV关系对组:\n\t<"+relationlist.get(l).getfirstId()
        								+"\t"+relationlist.get(l).getfirstCont()
        								+"\t"+relationlist.get(l).getfirstPos()
        								+"\t"+relationlist.get(l).getsecondId()
        								+"\t"+relationlist.get(l).getsecondCont()
        								+"\t"+relationlist.get(l).getsecondPos()
        								+"\t"+relationlist.get(l).getrelation()
        								+"\t"+relationlist.get(l).getrawScore()
        								+">"+"\r\n");
//        						System.out.println("\tADV关系对组:\n\t<"+relationlist.get(l).getfirstId()
//        								+"\t"+relationlist.get(l).getfirstCont()
//        								+"\t"+relationlist.get(l).getfirstPos()
//        								+"\t"+relationlist.get(l).getsecondId()
//        								+"\t"+relationlist.get(l).getsecondCont()
//        								+"\t"+relationlist.get(l).getsecondPos()
//        								+"\t"+relationlist.get(l).getrelation()
//        								+"\t"+relationlist.get(l).getrawScore()
//        								+">");
        						
        						if(MotionParsing.isAdverb(relationlist.get(l).getfirstCont())){
        							//该词有副词修饰
        							Text.writerFileIsAppend(dir, relationlist.get(l).getfirstCont()+"分值是:"+MotionParsing.getAdverbScore(relationlist.get(l).getfirstCont())+"\r\n");
        							System.out.println(relationlist.get(l).getfirstCont()+"分值是:"+MotionParsing.getAdverbScore(relationlist.get(l).getfirstCont()));
        							intense=intense*MotionParsing.getAdverbScore(relationlist.get(l).getfirstCont());
        						}else if(MotionParsing.isNot(relationlist.get(l).getfirstCont())){
        							//有否定词修饰
        							ManyNodeTree tree1 = new ManyNodeTree();
        							ManyNodeTree tree2 = new ManyNodeTree();
        							int first=tree1.getsearch(relationlist.get(l).getfirstId(),tree.createTree(treeNodes).getRoot());
        							int second=tree2.getsearch(relationlist.get(l).getsecondId(),tree.createTree(treeNodes).getRoot());
        							
        							double dist=Math.abs(second-first);
        							Text.writerFileIsAppend(dir, "\t语法近距离是："+dist+"\r\n");
        							System.out.println("\t语法近距离是："+dist);
        							ModifiedIntense=ModifiedIntense*((MotionParsing.getNotScore(relationlist.get(l).getfirstCont()))/(sita*dist));
        						}else{
        							//没有副词和否定词修饰
        							intense=1.0*intense;
        							ModifiedIntense=1.0*ModifiedIntense;
        						}
        					}
        					wordSentimentScore=MotionParsing.getscore("cidian", motionWordslist.get(k).getText())*intense*ModifiedIntense;
            				wordSentimentScorelist.add(wordSentimentScore);
        				}
        				else{
        					notADVmotion_count++;
        					wordSentimentScore=motionWordslist.get(k).getScore();
        					notSentimentScorelist.add(wordSentimentScore);
        				}
        			}	
        		}
        		Text.writerFileIsAppend(dir, "该情感句中有修饰的情感词个数:"+ADVmotion_count+"\n情感句中没有修饰的情感词个数:"+notADVmotion_count+"\r\n");
//        		System.out.println("情感句中有修饰的情感词个数:"+ADVmotion_count+"\n情感句中没有修饰的情感词个数:"+notADVmotion_count);

        		if(ADVmotion_count!=0||notADVmotion_count!=0)
        		{
        			Text.writerFileIsAppend(dir, "\tADV得分:"+"\r\n");
//        			System.out.println("\tADV得分:");
        			for(int i=0;i<wordSentimentScorelist.size();i++){
        				Text.writerFileIsAppend(dir, "\t"+wordSentimentScorelist.get(i)+"\r\n");
//            			System.out.println("\t"+wordSentimentScorelist.get(i));
            		}
        			Text.writerFileIsAppend(dir, "\t非ADV得分:"+"\r\n");
//        			System.out.println("\t非ADV得分:");
        			for(int i=0;i<notSentimentScorelist.size();i++){
        				Text.writerFileIsAppend(dir, "\t"+notSentimentScorelist.get(i)+"\r\n");
//            			System.out.println("\t"+notSentimentScorelist.get(i));
            		}
        		}else{
        			Text.writerFileIsAppend(dir, "\t无情感词！"+"\r\n");
        			System.out.println("\t无情感词！");
        		}
        		
        		SentenceMotionWordCount.add(ADVmotion_count+notADVmotion_count);
        		
        		if(ADVmotion_count==0&&notADVmotion_count==0){
        			sentenceSentimentScore=0;
        			sentenceScore.add(sentenceSentimentScore);
        			Text.writerFileIsAppend(dir, "情感句得分为:"+sentenceSentimentScore+"\r\n");
//        			System.out.println("情感句得分为:"+sentenceSentimentScore);
        		}else
        		{
        			double wordscore=0;
        			double rawScore=0;
        			for(int j=0;j<wordSentimentScorelist.size();j++){
        				wordscore+=wordSentimentScorelist.get(j);
        			}
        			for(int i=0;i<notSentimentScorelist.size();i++){
        				rawScore+=notSentimentScorelist.get(i);
        			}
        			if(ADVmotion_count==0){
        				sentenceSentimentScore=(1.0/(ADVmotion_count+notADVmotion_count))*rawScore;
        				sentenceScore.add(sentenceSentimentScore);
        				Text.writerFileIsAppend(dir, "情感句得分为:"+sentenceSentimentScore+"\r\n");
        				System.out.println("情感句得分为:"+sentenceSentimentScore);
        			}else if(notADVmotion_count==0){
        				sentenceSentimentScore=wordscore*(1.0/(ADVmotion_count+notADVmotion_count));
        				sentenceScore.add(sentenceSentimentScore);
        				Text.writerFileIsAppend(dir, "情感句得分为:"+sentenceSentimentScore+"\r\n");
        				System.out.println("情感句得分为:"+sentenceSentimentScore);
        			}
        			else{
        				sentenceSentimentScore=wordscore*(1.0/(ADVmotion_count+notADVmotion_count))*rawScore;
        				sentenceScore.add(sentenceSentimentScore);
        				Text.writerFileIsAppend(dir, "情感句得分为:"+sentenceSentimentScore+"\r\n");
        				System.out.println("情感句得分为:"+sentenceSentimentScore);
        			}
        			
        		}
        		if(sentenceSentimentScore==0){
    				sentenceZeroScoreCount++;
    			}
        	}
        	for(int i=0;i<SentenceMotionWordCount.size();i++){
        		DocuementCount+=SentenceMotionWordCount.get(i);
        	}
        	Text.writerFileIsAppend(dir, "\t情感词个数:"+DocuementCount+"\r\n");
        	System.out.println("\t情感词个数:"+DocuementCount);
        	for(int j=0;j<motionSentence_count;j++){
        		Text.writerFileIsAppend(dir, (double)(SentenceMotionWordCount.get(j)/(double)DocuementCount)+"\r\n");
        		System.out.println((double)(SentenceMotionWordCount.get(j)/(double)DocuementCount));
        		Score=Score+(sentenceScore.get(j)*((double)(SentenceMotionWordCount.get(j)/(double)DocuementCount)));
        	}
        	DocuementMotionScore=(1.0/((double)(motionSentence_count-sentenceZeroScoreCount)))*Score;
        	Text.writerFileIsAppend(dir, "\t文档情感得分:"+DocuementMotionScore+"\r\n");
        	System.out.println("\t文档情感得分:"+DocuementMotionScore);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            ls.close();
        }
	}
	
	
	
	
}
