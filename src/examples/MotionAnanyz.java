package examples;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import nbu.deleteurl.test.HtmlRegexpUtil;
import nbu.nlp.examples.GroupWord;
import edu.hit.ir.ltpService.LTML;
import edu.hit.ir.ltpService.LTPOption;
import edu.hit.ir.ltpService.LTPService;
import edu.hit.ir.ltpService.Word;
import pro.mysql.connection.MySqlConnection;

public class MotionAnanyz {
	/*
	public static void main(String args[]) throws Exception{
		String huati="乌鲁木齐爆炸";
		String sql1="select * from weibo_info where huati='"+huati+"'";
		ArrayList<String> sentences=new MotionAnanyz().getSentences(sql1);
		int sent_no=1;
		for(int i=0;i<sentences.size();i++){
			MotionAnanyz motion=new MotionAnanyz();
			String sent=HtmlRegexpUtil.filterHtml(HtmlRegexpUtil.filterJinhao(sentences.get(i).replaceAll("http:[a-zA-Z\\/\\.0-9]+", "")));
			if(!sent.isEmpty()){
				ArrayList<GroupWord> group=motion.Fenci(sent);
				if(group.size()>0){
					String sql="insert into sentences(sent_no,text,huati) values('"+sent_no+"','"+sent+"','"+huati+"')";
//					MotionAnanyz motion=new MotionAnanyz();
					MotionAnanyz.insertWord(sql);
					for(int j=0;j<group.size();j++){
//						MotionAnanyz motion=new MotionAnanyz();
						String word=group.get(j).getWord();
						String type=group.get(j).getType();
						String sqltxt="insert into motionword(no,text,high,low,general,type,huati) values('"+sent_no+"','"+word+"',0,0,0,'"+type+"','"+huati+"')";
						MotionAnanyz.insertWord(sqltxt);
					}
					sent_no++;
				}
			}
		}	
	}
	*/
	
	public ArrayList<String> getSentences(String txtsql) throws Exception{
		ArrayList<String> list=new ArrayList<String>();
		
		Statement stmt = null ;
		ResultSet rs =null;
		Connection con = null ;		
		String driver=MySqlConnection.getDriver();
		String url=MySqlConnection.getUrl();
		String username=MySqlConnection.getUser();
		String password=MySqlConnection.getPassword();
		try{
			Class.forName (driver);
			con = DriverManager.getConnection (url, username, password); 
			stmt = con.createStatement();
			rs=stmt.executeQuery(txtsql);
			while(rs.next()){
				list.add(rs.getString("text"));
			}
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally{
			con.close();
		}
		return list;
	}
	
	
	public static void insertWord(String txtsql) throws SQLException{
		Statement stmt = null ;
		Connection con = null ;		
		String driver=MySqlConnection.getDriver();
		String url=MySqlConnection.getUrl();
		String username=MySqlConnection.getUser();
		String password=MySqlConnection.getPassword();
		try{
			Class.forName (driver);
			con = DriverManager.getConnection (url, username, password); 
			stmt = con.createStatement();
			stmt.executeUpdate(txtsql);	
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally{
			con.close();
			
		}
		
	}
	
	public ArrayList<GroupWord> Fenci(String sent){
		ArrayList<GroupWord> words=new ArrayList<GroupWord>();
		LTPService ls = new LTPService("nbu13jsjfh@163.com:gFLSVHzo");
		try{
			ls.setEncoding(LTPOption.UTF8);
			LTML ltml=ls.analyze(LTPOption.PARSER, sent);
			int sentNum = ltml.countSentence();
			for(int i=0;i<sentNum;i++){
				ArrayList<Word> list=new ArrayList<Word>();
				list=ltml.getWords(i);
				for(int j=0;j<list.size();j++){
					String str;
					if((str=MotionAnanyz.isMotionWord(list.get(j).getWS()))!=null)
					{
						GroupWord word=new GroupWord();
						word.setWord(list.get(j).getWS());
						word.setType(str);
						word.setIntensity(0);
						words.add(word);
					}
						
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return words;
	}
	
	public static String isMotionWord(String word)throws Exception{
		
		String happy="happy";
//		String goood="goood";
		String anger="angery";
		String sad="sad";
		String fear="fear";
		String bad="bad";
		String shock="shock";
		
//		String positive="positive";
//		String negative="negative";
		
		List<String> texthappy=new ArrayList<String>();
//		List<String> textgood=new ArrayList<String>();
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
		
		String slt_happy="select text from cidian where class='PA' or class='PE' ";
//		String slt_good="select text from cidian where class='PD' or class='PH' or class='PG' or class='PB' or class='Pk'";
		String slt_anger="select text from cidian where class='NA'";
		String slt_sad="select text from cidian where class='NB' or class='NJ' or class='NH' or class='PF'";
		String slt_fear="select text from cidian where class='NI' or class='NC' or class='NG'";
		String slt_bad="select text from cidian where class='NE' or class='ND' or class='NN' or class='NK' or class='NL'";
		String slt_shock="select text from cidian where class='PC'";
		
		try{
			Class.forName (driver);
			con = DriverManager.getConnection (url, username, password); 
			stmt = con.createStatement();
			
			rs1=stmt.executeQuery(slt_happy);
			while(rs1.next()){
				texthappy.add(rs1.getString("text"));
			}
			rs2=stmt.executeQuery(slt_anger);
			while(rs2.next()){
				textanger.add(rs2.getString("text"));
			}
			rs3=stmt.executeQuery(slt_sad);
			while(rs3.next()){
				textsad.add(rs3.getString("text"));
			}
			rs4=stmt.executeQuery(slt_fear);
			while(rs4.next()){
				textfear.add(rs4.getString("text"));
			}
			rs5=stmt.executeQuery(slt_bad);
			while(rs5.next()){
				textbad.add(rs5.getString("text"));
			}
			rs6=stmt.executeQuery(slt_shock);
			while(rs6.next()){
				textshock.add(rs6.getString("text"));
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally{
			con.close();
		}
		
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
	
	public void dosomething(String huati) throws Exception{
		String sql1="select * from weibo_info where huati='"+huati+"'";
		ArrayList<String> sentences=new MotionAnanyz().getSentences(sql1);
		int sent_no=1;
		for(int i=0;i<sentences.size();i++){
			String sent=HtmlRegexpUtil.filterHtml(HtmlRegexpUtil.filterJinhao(sentences.get(i).replaceAll("http:[a-zA-Z\\/\\.0-9]+", "")));
			if(!sent.isEmpty()){
				ArrayList<GroupWord> group=new MotionAnanyz().Fenci(sent);
				if(group.size()>0){
					String sql="insert into sentences(sent_no,text,huati) values('"+sent_no+"','"+sent+"','"+huati+"')";
					MotionAnanyz motion=new MotionAnanyz();
					motion.insertWord(sql);
					for(int j=0;j<group.size();j++){
						MotionAnanyz motion1=new MotionAnanyz();
						String word=group.get(j).getWord();
						String type=group.get(j).getType();
						String sqltxt="insert into motionword(no,text,high,low,general,type,huati) values('"+sent_no+"','"+word+"',0,0,0,'"+type+"','"+huati+"')";
						motion1.insertWord(sqltxt);
					}
					sent_no++;
				}
			}
		}	
	}
	
	public static void main(String args[]){
			
	}
}

