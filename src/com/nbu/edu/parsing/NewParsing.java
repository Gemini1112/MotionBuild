package com.nbu.edu.parsing;

//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.InputStreamReader;
//import edu.hit.ir.ltpService.SRL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;

import pro.mysql.connection.MySqlConnection;

//import com.mysql.jdbc.ResultSet;

import nbu.deleteurl.test.HtmlRegexpUtil;
//import nbu.file.txt.write.Text;
import nbu.nlp.examples.MotionWord;
import nbu.nlp.examples.Relationship;
import nbu.test.tree.ManyNodeTree;
import nbu.test.tree.ParsingTree;
import nbu.test.tree.TreeNode;
import edu.hit.ir.ltpService.LTML;
import edu.hit.ir.ltpService.LTPOption;
import edu.hit.ir.ltpService.LTPService;
import edu.hit.ir.ltpService.Word;

public class NewParsing {
	
	
	public ResultSet result=null;
	public static Statement stmt = null ;
	public static Connection con = null ;
	public static String driver=MySqlConnection.getDriver();;
	public static String url=MySqlConnection.getUrl();;
	public static String username=MySqlConnection.getUser();;
	public static String password=MySqlConnection.getPassword();
	
	public NewParsing() throws ClassNotFoundException{
		Class.forName (driver);
	}

	
	public static void main(String args[]) throws Exception{
//		String sql="insert into intensitytb(topic,province,type,intensity) values('安徽','安徽','happy','8.0')";
//		Class.forName (driver);
//		con=DriverManager.getConnection (url, username, password);
//		NewParsing.aqlInsert(sql);
//		con.close();
//		ArrayList<String> a=new ArrayList<String>();
//		System.out.println(a.size());
//		NewParsing.Parsing("广东", "乌鲁木齐爆炸", "shock", "");
		
//		NewParsing.Insert("招远围殴凶杀");
		
		
//		NewParsing.Parsing("北京", "大老虎", "bad", "");
	}
	
	/**
	 * 依存句法分析情绪强度
	 * @param src
	 * @param huati
	 * @param motionStyle
	 * @param dec
	 * @throws Exception
	 */
	public static void Parsing(String province,String huati,String motionType,String dec) throws Exception{
		ArrayList<String> txtWeibo=NewParsing.Sentences(province,huati);
		LTPService ls = new LTPService("nbu13jsjfh@163.com:gFLSVHzo"); //语言云验证
		ArrayList<Double> sentIntensity=new ArrayList<Double>();//记录每条微博,即每条情感句的强度值
		ArrayList<Integer> SentenceMotionWordCount=new ArrayList<Integer>();
		int totalMotionSentences=txtWeibo.size();
		int totalMotionWordCount=0;
		int sentenceZeroScoreCount=0;
		double Score=0;
		double totalMotionIntensity;
		
		int totalMotion=0;
		
		ArrayList<Integer> motionCount=new ArrayList<Integer>();//每个情感句中情感词的数目
		for(int i=0;i<txtWeibo.size();i++){
			
//			int motion=0;
			double sentenceIntensity;	//情感句强度值
//			int motionCount=0;			//情感词个数
			int ADVmotion_count=0;		//有修饰的情感词个数
    		int notADVmotion_count=0;	//无修饰的情感词个数
    		
//    		ArrayList<MotionWord> motionWordList=new ArrayList<MotionWord>();//情感词
//			ArrayList<Relationship> ADVrelationship=new ArrayList<Relationship>();//ADV关系对组
			ArrayList<Double> SentimentScore=new ArrayList<Double>();//情感词修饰强度
			ArrayList<Double> notSentimentScore=new ArrayList<Double>();
			ls.setEncoding(LTPOption.UTF8);
    		LTML ltml = ls.analyze(LTPOption.PARSER,txtWeibo.get(i));
    		int sentNum = ltml.countSentence();//该条微博文本包含的句子数量
    		
    		int yiwen=0;
			int gantan=0;
			
			int motionNum=0;
			
    		for(int m=0;m<sentNum;m++){
    			//分析感叹号和疑问号（统计的方法）
    			
    			ArrayList<MotionWord> motionWordList=new ArrayList<MotionWord>();//一句话中的情感词
    			ArrayList<Word> wordList = ltml.getWords(m);//分词结果
    			ArrayList<Relationship> relationshipList=new ArrayList<Relationship>();
    			for(int j=0;j<wordList.size();j++){
    				//System.out.println(wordList.get(j));
    				Relationship relationship=new Relationship();
    				
    				/**
    				 * 是情感词，则记录到motionWordList中
    				 */
    				if(NewParsing.isMotion_Word(wordList.get(j).getWS())!=null&&NewParsing.isMotion_Word(wordList.get(j).getWS()).equals(motionType))
    				{
    					motionNum++;
    					
    					MotionWord motionword=new MotionWord();
    					motionword.setId(wordList.get(j).getID());
    					motionword.setText(wordList.get(j).getWS());
    					motionword.setType(motionType);
    					motionword.setScore(0.8);
    					motionWordList.add(motionword);
    				}
    			
    				if(m==sentNum-1&&j==wordList.size()-1&&wordList.get(j).getWS().equals("?")){
    					yiwen++;
    				}else if(m==sentNum-1&&j==wordList.size()-1&&wordList.get(j).getWS().equals("!")){
    					gantan++;
    				}
    				/**
    				 * 分析出该句的依存关系，记录在relationship中
    				 */
    				if(wordList.get(j).getParserParent()!=-1){
    					relationship.setsecondCont(wordList.get(wordList.get(j).getParserParent()).getWS());
    					relationship.setsecondPos(wordList.get(wordList.get(j).getParserParent()).getPOS());
    				}else{
    					relationship.setsecondCont("Root");
    					relationship.setsecondPos("null");
    				}
    				relationship.setfirstId(wordList.get(j).getID());
					relationship.setfirstCont(wordList.get(j).getWS());
					relationship.setfirstPos(wordList.get(j).getPOS());
					relationship.setsecondId(wordList.get(j).getParserParent());
    				relationship.setrelation(wordList.get(j).getParserRelation());
					relationshipList.add(relationship);
    			}//依存关系、分词结束
    			/**
    			 * 构建语法树
    			 */
    			int id=0;
    			List<TreeNode> treeNodes = new ArrayList<TreeNode>();
				for(int r=0;r<relationshipList.size();r++){
					if(relationshipList.get(r).getsecondId()==-1){
    					treeNodes.add(new TreeNode(relationshipList.get(r).getfirstId(),-1,relationshipList.get(r).getfirstCont()));
						id=relationshipList.get(r).getfirstId();
						break;
    				}
				}
				ParsingTree.tree(relationshipList,id, treeNodes);
    			ManyNodeTree tree = new ManyNodeTree();
//    			System.out.println(tree.iteratorTree(tree.createTree(treeNodes).getRoot()));
    			/**
    			 * ADV关系对组
    			 */
    			ArrayList<Relationship> advList=NewParsing.ADV_RelationShip(relationshipList, motionType);
    			
//    			for(int k=0;k<advList.size();k++){
//    				System.out.println("\t"+advList.get(k).getfirstId()+"\t"+advList.get(k).getsecondCont()+"\t"+advList.get(k).getrelation());
//    			}
//    			
    			
    			/**
    			 * ADV关系对组，情感词强度修饰
    			 */
    			double sita=1.0;
    			for(int j=0;j<motionWordList.size();j++){
    				double wordSentimentScore = 0;
//    				double intense=1.0;
//    				double ModifiedIntense=1.0;
    				boolean isadv=false;
    				boolean isnot=false;
    				double advdist=0;
    				double notdist=0;
    				double adv=0.0;
    				double not=0.0;
    				ArrayList<Relationship> ADV_centerWord=NewParsing.isADV_Word(motionWordList.get(j).getId(), advList);
    				if(ADV_centerWord.size()>0){
    					ADVmotion_count++;
    				}else if(ADV_centerWord.size()==0){
    					notADVmotion_count++;
//    					notSentimentScore.add(motionWordList.get(j).getScore());
    					wordSentimentScore=motionWordList.get(j).getScore();
    					SentimentScore.add(motionWordList.get(j).getScore());
    				}
    				for(int k=0;k<ADV_centerWord.size();k++){
    					
    					if(NewParsing.isAdverbWord(ADV_centerWord.get(k).getfirstCont())){
    						ManyNodeTree tree1 = new ManyNodeTree();
    						ManyNodeTree tree2 = new ManyNodeTree();
    						int first=tree1.getsearch(ADV_centerWord.get(k).getfirstId(),tree.createTree(treeNodes).getRoot());
    						int second=tree2.getsearch(ADV_centerWord.get(k).getsecondId(),tree.createTree(treeNodes).getRoot());
    						advdist=Math.abs(first-second);
    						adv=NewParsing.get_AdverbScore(ADV_centerWord.get(k).getfirstCont());
    						isadv=true;
    						
//    						if(NewParsing.get_AdverbScore(ADV_centerWord.get(k).getfirstCont())>=1){
//    							intense=intense*(NewParsing.get_AdverbScore(ADV_centerWord.get(k).getfirstCont())-1.0);
//    						}else{
//    							intense=intense*NewParsing.get_AdverbScore(ADV_centerWord.get(k).getfirstCont());
//    						}
//    						intense=intense*NewParsing.get_AdverbScore(ADV_centerWord.get(k).getfirstCont());
    					}else if(NewParsing.isNotWord(ADV_centerWord.get(k).getfirstCont())){
    						/**
    						 * 计算语法距离
    						 */
    						ManyNodeTree tree1 = new ManyNodeTree();
							ManyNodeTree tree2 = new ManyNodeTree();
							int first=tree1.getsearch(ADV_centerWord.get(k).getfirstId(),tree.createTree(treeNodes).getRoot());
							int second=tree2.getsearch(ADV_centerWord.get(k).getsecondId(),tree.createTree(treeNodes).getRoot());
							notdist=Math.abs(first-second);
							isnot=true;
//    						ModifiedIntense=ModifiedIntense*((NewParsing.get_NotScore(ADV_centerWord.get(k).getfirstCont()))/(sita*dist));
    					}else{
    						isadv=false;
    						isnot=false;
//							intense=1.0*intense;
//							ModifiedIntense=1.0*ModifiedIntense;
						}	
    				}
    				if(ADV_centerWord.size()>0){
    					if(isadv==true&&isnot==false){
    						double score=motionWordList.get(j).getScore();
    						if(adv>=1.0){
    							wordSentimentScore=score+(1.0-score)*(adv-1.0)/(sita*Math.sqrt(advdist));
    						}else{
    							wordSentimentScore=score*adv/(sita*Math.sqrt(advdist));
    						}
    						SentimentScore.add(wordSentimentScore);
    					}else if(isadv==true&&isnot==true){
    						double score=motionWordList.get(j).getScore();
    						wordSentimentScore=score;
    						if(advdist>notdist&&adv>=1&&not>=1){
    							wordSentimentScore=(score*(2-not)+(1-score))*(2-not)/(sita*Math.sqrt(advdist)*Math.sqrt(notdist));
    						}else if(advdist<notdist&&adv>=1&&not>=1){
    							wordSentimentScore=(score+(1-score)*(adv-1))*(2-not)/(sita*Math.sqrt(advdist)*Math.sqrt(notdist));	
    						}
    						SentimentScore.add(wordSentimentScore);
    					}else if(isadv==false&&isnot==true){
    						double score=motionWordList.get(j).getScore();
    						if(not>=1.0){
    							wordSentimentScore=score*(2-not)/(sita*Math.sqrt(notdist));
    						}else{
    							wordSentimentScore=score+(1-score)*(1-not)/(sita*Math.sqrt(notdist));
    						}
    						SentimentScore.add(wordSentimentScore);
    					}
    					else{
    						wordSentimentScore=motionWordList.get(j).getScore();
    						SentimentScore.add(motionWordList.get(j).getScore());
    					}
    				}
    				System.out.println(motionWordList.get(j).getText()+"\t修饰强度:\t"+wordSentimentScore);
//    				wordSentimentScore=motionWordList.get(j).getScore()*intense*ModifiedIntense;
//					SentimentScore.add(wordSentimentScore);
    			}
    		}//一条微博情感句分析完毕
    		
    		
    		if(motionNum>0){
    			totalMotion++;
    		}
    		
    		
    		
    		motionCount.add(ADVmotion_count+notADVmotion_count);
    		/**
    		 * 计算情感句的强度
    		 */
    		SentenceMotionWordCount.add(ADVmotion_count+notADVmotion_count);
    		if(ADVmotion_count==0&&notADVmotion_count==0){//10月11日晚改
    			sentenceIntensity=0;
    		}else{
    			double totalScore=0;
    			for(int j=0;j<SentimentScore.size();j++){
    				totalScore+=SentimentScore.get(j);
    			}
//    			if(notSentimentScore.size()>0){
//    				for(int j=0;j<notSentimentScore.size();j++){
//        				totalScore+=notSentimentScore.get(j);
//        			}
//    				sentenceIntensity=totalScore/notSentimentScore.size();
//    			}
    			if(yiwen>0){
    				sentenceIntensity=(totalScore/(ADVmotion_count+notADVmotion_count))*0.8;
    			} else if(gantan>0){
    				sentenceIntensity=(totalScore/(ADVmotion_count+notADVmotion_count))*1.5;
    			}else{
    				sentenceIntensity=(totalScore/(ADVmotion_count+notADVmotion_count));
    			}
    			
//    			double wordScore=0;
//    			double rawScore=0;
//    			for(int j=0;j<SentimentScore.size();j++){
//    				wordScore+=SentimentScore.get(j);
//    			}
//    			for(int j=0;j<notSentimentScore.size();j++){
//    				rawScore+=notSentimentScore.get(j);
//    			}
//    			if(ADVmotion_count==0){
//    				sentenceIntensity=(1.0/(ADVmotion_count+notADVmotion_count))*rawScore;
//    				
//    			}else if(notADVmotion_count==0){
//    				sentenceIntensity=wordScore*(1.0/(ADVmotion_count+notADVmotion_count));
//    			}else{
//    				sentenceIntensity=wordScore*(1.0/(ADVmotion_count+notADVmotion_count))*rawScore;
//    			}
    		}
    		
    		sentIntensity.add(sentenceIntensity);
    		System.out.println("情感句强度:"+sentenceIntensity);
    		if(sentenceIntensity==0){
    			sentenceZeroScoreCount++;
    		}
		}
		
//		int motion_count=0;
//		for(int k=0;k<motionCount.size();k++){
//			motion_count+=motionCount.get(k);
//		}
//		ArrayList<Double> rato=new ArrayList<Double>();
//		for(int k=0;k<motionCount.size();k++){
//			rato.add((double)motionCount.get(k)/(double)motion_count);
//		}
		/**
		 * 计算出群体情绪的强度值
		 */
		double quan=0;
//		ArrayList<Double> rato=new ArrayList<Double>();
		for(int j=0;j<sentIntensity.size();j++){
			quan+=Math.sqrt(motionCount.get(j));
		}
		double score=0;
		for(int j=0;j<sentIntensity.size();j++){
			score+=sentIntensity.get(j)*(Math.sqrt(motionCount.get(j))/quan);
//			score+=sentIntensity.get(j)*rato.get(j);
		}
		if(totalMotionSentences-sentenceZeroScoreCount>0){
//			totalMotionIntensity=score/(totalMotionSentences-sentenceZeroScoreCount);
			totalMotionIntensity=score;
		}else{
			totalMotionIntensity=-1.0;
		}
//		
//		for(int i=0;i<SentenceMotionWordCount.size();i++){
//			totalMotionWordCount+=SentenceMotionWordCount.get(i);
//		}
//		if(SentenceMotionWordCount.size()>0){
//			for(int j=0;j<totalMotionSentences;j++){
//				Score=Score+(sentIntensity.get(j)*((double)(SentenceMotionWordCount.get(j)/(double)totalMotionWordCount)));
//			}
//			totalMotionIntensity=(1.0/((double)(totalMotionSentences-sentenceZeroScoreCount)))*Score;
//		}else{
//			totalMotionIntensity=-1.0;
//		}
		String addsql="insert into intensitytb(topic,province,type,intensity) values('"+huati+"','"+province+"','"+motionType+"','"+totalMotionIntensity+"')";
		Class.forName (driver);
		con=DriverManager.getConnection (url, username, password);
		NewParsing.sqlInsert(addsql);
		con.close();
//		Text.writerFileIsAppend(dec+"/"+province+".txt", motionType+":"+totalMotionIntensity+"\r\n");
		System.out.println("群体强度值:"+totalMotionIntensity);
		
	}
	
	
	/**
	 * 如果是情感词，则返回该词所属类别，否则，返回为空
	 * @param word
	 * @return
	 * @throws Exception
	 */
	public static String isMotion_Word(String word)throws Exception{
		
		String happy="happy";
		String anger="angery";
		String sad="sad";
		String fear="fear";
		String bad="bad";
		String shock="shock";

		List<String> texthappy=new ArrayList<String>();
		List<String> textanger=new ArrayList<String>();
		List<String> textsad=new ArrayList<String>();
		List<String> textfear=new ArrayList<String>();
		List<String> textbad=new ArrayList<String>();
		List<String> textshock=new ArrayList<String>();
		ResultSet rs1 = null,rs2=null,rs3=null,rs4=null,rs5=null,rs6=null;
		String slt_happy="select text from cidian where class='PA' or class='PE' ";
		String slt_anger="select text from cidian where class='NA'";
		String slt_sad="select text from cidian where class='NB' or class='NJ' or class='NH' or class='PF'";
		String slt_fear="select text from cidian where class='NI' or class='NC' or class='NG'";
		String slt_bad="select text from cidian where class='NE' or class='ND' or class='NN' or class='NK' or class='NL'";
		String slt_shock="select text from cidian where class='PC'";
		Class.forName (driver);
		con = DriverManager.getConnection (url, username, password); 
		rs1=new NewParsing().sqlQuery(slt_happy);
		while(rs1.next()){
			texthappy.add(rs1.getString("text"));
		}
		rs2=new NewParsing().sqlQuery(slt_anger);
		while(rs2.next()){
			textanger.add(rs2.getString("text"));
		}
		rs3=new NewParsing().sqlQuery(slt_sad);
		while(rs3.next()){
			textsad.add(rs3.getString("text"));
		}
		rs4=new NewParsing().sqlQuery(slt_fear);
		while(rs4.next()){
			textfear.add(rs4.getString("text"));
		}
		rs5=new NewParsing().sqlQuery(slt_bad);
		while(rs5.next()){
			textbad.add(rs5.getString("text"));
		}
		rs6=new NewParsing().sqlQuery(slt_shock);
		while(rs6.next()){
			textshock.add(rs6.getString("text"));
		}
		con.close();
		for(int i=0;i<texthappy.size();i++){
			if(texthappy.get(i).equals(word)){
				return happy;
			}
		}
		for(int i=0;i<textanger.size();i++){
			if(textanger.get(i).equals(word)){
				return anger;
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
		return null;
	}
	
	
	/**
	 * 返回微博文本集合
	 * @param huati
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<String> Sentences(String province,String huati) throws Exception{
		ArrayList<String> list=new ArrayList<String>();
		String sql="select distinct weibo_id,text,location from weibo_info where huati='"+huati+"' and location like '%"+province+"%'";
		Class.forName (driver);
		con = DriverManager.getConnection (url, username, password);
		ResultSet result=new NewParsing().sqlQuery(sql);
		while(result.next()){
				String s=HtmlRegexpUtil.filterHtml(HtmlRegexpUtil.filterJinhao(result.getString("text").replaceAll("http:[a-zA-Z\\/\\.0-9]+", "")));
				if(!s.isEmpty()){
					list.add(s);
				}
		}
		con.close();
	//	result.close();
		return list;
	}
	
	
	/**
	 * 数据库查询操作
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public  ResultSet sqlQuery(String sql) throws Exception{
		stmt = con.createStatement();
		result=stmt.executeQuery(sql);
		//con.close();
		return result;
	}
	
	
	/**
	 * 返回一个依存关系中提取ADV关系对组
	 * @param relation
	 * @param motionType
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<Relationship> ADV_RelationShip(ArrayList<Relationship> relation,String motionType)throws Exception{
		ArrayList<Relationship> relationADVlist=new ArrayList<Relationship>();
		
		for(int i=0;i<relation.size();i++){
			Relationship relationADV=new Relationship();
			String str;
			if((str=NewParsing.isMotion_Word(relation.get(i).getsecondCont()))!=null){
				if(relation.get(i).getrelation().equals("ADV")&&str.equals(motionType)){
					relationADV.setfirstId(relation.get(i).getfirstId());
					relationADV.setfirstCont(relation.get(i).getfirstCont());
					relationADV.setfirstPos(relation.get(i).getfirstPos());
					relationADV.setsecondId(relation.get(i).getsecondId());
					relationADV.setsecondCont(relation.get(i).getsecondCont());
					relationADV.setsecondPos(relation.get(i).getsecondPos());
					relationADV.setrelation(relation.get(i).getrelation());
					relationADV.setrawScore(0.8);
					relationADVlist.add(relationADV);
				}
				else
				{
					if(relation.get(i).getrelation().equals("CMP")||relation.get(i).getrelation().equals("VOB")){
						if(NewParsing.isMotion_Word(relation.get(i).getsecondCont()).equals(motionType)){
							Relationship reship=new Relationship();
							if((reship=NewParsing.isADV_RelationShip(relation.get(i).getsecondId(),relation))!=null){
								relationADV.setfirstId(reship.getfirstId());
								relationADV.setfirstCont(reship.getfirstCont());
								relationADV.setfirstPos(reship.getfirstPos());
								relationADV.setsecondId(relation.get(i).getfirstId());
								relationADV.setsecondCont(relation.get(i).getfirstCont());
								relationADV.setsecondPos(relation.get(i).getfirstPos());
								relationADV.setrelation("ADV");
								relationADV.setrawScore(0.8);
								relationADVlist.add(relationADV);
							}
						}
					}
				}
			}
		}
		return relationADVlist;
	}
	
	
	/**
	 * 如果是ADV关系对组，则返回这个ADV关系对组
	 * @param id
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public static Relationship isADV_RelationShip(int id,ArrayList<Relationship> list)throws Exception{
		for(int i=0;i<list.size();i++){
			if(list.get(i).getsecondId()==id&&list.get(i).getrelation().equals("ADV")){
				return list.get(i);
			}
		}
		return null;
	}
	
	
	/**
	 * 在ADV关系对组中找出以该情感词为中心的关系对组
	 * @param id
	 * @param relationADV
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<Relationship> isADV_Word(int id,ArrayList<Relationship> relationADV)throws Exception{
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
	
	
	
	/**
	 * word是副词返回true，否则返回false
	 * @param word
	 * @return
	 * @throws Exception
	 */
	public static boolean isAdverbWord(String word)throws Exception{
		boolean ok=false;
		ResultSet rs = null;
		Class.forName (driver);
		con = DriverManager.getConnection (url, username, password);
		String selt="select * from fuci where text='"+word+"'";
		try{
			rs=new NewParsing().sqlQuery(selt);
			if(rs.next()){
				ok=true;
			}
			else{
				ok=false;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		con.close();
		return ok;	
	}

	
	/**
	 * 返回副词的强度值
	 * @param word
	 * @return
	 * @throws Exception
	 */
	public static double get_AdverbScore(String word)throws Exception{
		double score = 0;
		ResultSet rs = null;
		Class.forName (driver);
		con = DriverManager.getConnection (url, username, password);
		String selt="select score from fuci where text='"+word+"'";
		try{
			
			rs=new NewParsing().sqlQuery(selt);
			if(rs.next()){
				score = rs.getDouble("score");
			}else
				score=0;
		}catch(Exception e){
			e.printStackTrace();
		}
		con.close();
		return score;	
	}

	
	
	/**
	 * 是否定词返回true，否则返回false
	 * @param word
	 * @return
	 * @throws Exception
	 */
	public static boolean isNotWord(String word)throws Exception{
		boolean ok=false;
		ResultSet rs = null;
		Class.forName (driver);
		con = DriverManager.getConnection (url, username, password);
		String selt="select * from negtive where text='"+word+"'";
		try{
			rs=new NewParsing().sqlQuery(selt);
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
		con.close();
		return ok;	
	}
	
	
	
	/**
	 * 返回否定词强度
	 * @param word
	 * @return
	 * @throws Exception
	 */
	public static double get_NotScore(String word)throws Exception{
		double score=0;
		ResultSet rs = null;
		Class.forName (driver);
		con = DriverManager.getConnection (url, username, password);
		String selt="select score from negtive where text='"+word+"'";
		
		try{
			rs=new NewParsing().sqlQuery(selt);
			if(rs.next()){
				score = rs.getDouble("score");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		con.close();
		return score;	
	}

	/**
	 * 插入操作
	 * @param sql
	 * @throws SQLException
	 */
	public static void sqlInsert(String sql) throws SQLException{
		stmt = con.createStatement();
		stmt.executeUpdate(sql);
	}
	
	
	public static void Insert(String huati) throws Exception{
		String sql="select distinct weibo_id,text from weibo_info where huati='"+huati+"'and (weibo_id='404660041137883' or weibo_id='407058044562737' or weibo_id='405359023090408' or weibo_id='401265011601917' or weibo_id='404459131323169' or weibo_id='416400060961980' or weibo_id='404959033295254' or weibo_id='413204023279824' or weibo_id='398438093056377' or weibo_id='397571131199645' or weibo_id='274960097587678' or weibo_id='415001116554055')";
		Class.forName (driver);
		con = DriverManager.getConnection (url, username, password);
		ResultSet rs=new NewParsing().sqlQuery(sql);
		String str[]=new String[12];
		int i=0;
		while(rs.next()){
			
			str[i]=HtmlRegexpUtil.filterHtml(HtmlRegexpUtil.filterJinhao(rs.getString("text").replaceAll("http:[a-zA-Z\\/\\.0-9]+", "")));
			i++;
		}
		con.close();
		
		LTPService ls = new LTPService("nbu13jsjfh@163.com:gFLSVHzo");
		for(int k=0;k<str.length;k++){
			ls.setEncoding(LTPOption.UTF8);
			LTML ltml = ls.analyze(LTPOption.PARSER,str[k]);
			int sentNum = ltml.countSentence();
			for(int j=0;j<sentNum;j++){
				ArrayList<Word> wordList = ltml.getWords(j);
				for(int s=0;s<wordList.size();s++){
					String type=NewParsing.isMotion_Word(wordList.get(s).getWS());
					if(type!=null){
						Class.forName (driver);
						con = DriverManager.getConnection (url, username, password);
						String s1="insert into motionword(no,text,high,low,general,type,huati)  values("+k+"+1,'"+wordList.get(s).getWS()+"',0,0,0,'"+type+"','"+huati+"')";
						NewParsing.sqlInsert(s1);
						con.close();
					}
				}
			}
		}
		Class.forName (driver);
		con = DriverManager.getConnection (url, username, password);
		for(int j=0;j<str.length;j++){
			String add="insert into sentences(sent_no,text,huati) values("+j+"+1,'"+str[j]+"','"+huati+"')";
			NewParsing.sqlInsert(add);
		}
		con.close();
	}
	
	
}
