package nbu.nlp.examples;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import nbu.file.txt.write.Text;

import org.jdom.JDOMException;

import edu.hit.ir.ltpService.LTML;
import edu.hit.ir.ltpService.LTPOption;
import edu.hit.ir.ltpService.LTPService;
import edu.hit.ir.ltpService.Word;
import pro.mysql.connection.MySqlConnection;


public class Statics {
	public static void main(String args[]) throws IOException, JDOMException{
		new Statics().Count("E:/数据地区分类/招远围殴凶杀/安徽/安徽_招远围殴凶杀_2014-9-28.txt", "E:/情感词", "");
		
	}
	
	public void Count(String in,String out,String type) throws IOException, JDOMException{
		
		Statement stmt = null ;
		ResultSet rs1 = null,rs2=null,rs3=null,rs4=null,rs5=null,rs6=null;
		Connection con = null ;
		
		String driver=MySqlConnection.getDriver();
		String url=MySqlConnection.getUrl();
		String username=MySqlConnection.getUser();
		String password=MySqlConnection.getPassword();
		
		/*快乐、愤怒、悲伤、恐惧、厌恶、惊讶*/
		String slt_happy="select text from cidian where (class='PA' or class='PE') and polarity!=0";
//		String slt_good="select text from cidian where (class='PD' or class='PH' or class='PG' or class='PB' or class='Pk') and and polarity!=0 ";
		String slt_anger="select text from cidian where class='NA' and polarity!=0";
		String slt_sad="select text from cidian where (class='NB' or class='NJ' or class='NH' or class='PF') and polarity!=0";
		String slt_fear="select text from cidian where (class='NI' or class='NC' or class='NG') and polarity!=0";
		String slt_bad="select text from cidian where (class='NE' or class='ND' or class='NN' or class='NK' or class='NL') and polarity!=0";
		String slt_shock="select text from cidian where class='PC' and polarity!=0";
		
		List<String> txthappy=new ArrayList<String>();
		List<String> txtanger=new ArrayList<String>();
		List<String> txtsad=new ArrayList<String>();
		List<String> txtfear=new ArrayList<String>();
		List<String> txtbad=new ArrayList<String>();
		List<String> txtshock=new ArrayList<String>();
		/*读取文本*/
		List<String> textWords=new ArrayList<String>();
		File file=new File(in);
		FileInputStream fInputStream = new FileInputStream(file);
		BufferedReader br = new BufferedReader(new InputStreamReader(fInputStream, "UTF-8"));
		String str=null;
		while((str=br.readLine())!=null){
			textWords.add(str);
		}
		br.close();
		
		//将各类型情感词读出来
		try{
			Class.forName (driver);
			con = DriverManager.getConnection (url, username, password); 
			stmt = con.createStatement();
			
			rs1=stmt.executeQuery(slt_happy);
			while(rs1.next()){
				txthappy.add(rs1.getString("text"));
			}
			rs2=stmt.executeQuery(slt_anger);
			while(rs2.next()){
				txtanger.add(rs2.getString("text"));
			}
			rs3=stmt.executeQuery(slt_sad);
			while(rs3.next()){
				txtsad.add(rs3.getString("text"));
			}
			rs4=stmt.executeQuery(slt_fear);
			while(rs4.next()){
				txtfear.add(rs4.getString("text"));
			}
			rs5=stmt.executeQuery(slt_bad);
			while(rs5.next()){
				txtbad.add(rs5.getString("text"));
			}
			rs6=stmt.executeQuery(slt_shock);
			while(rs6.next()){
				txtshock.add(rs6.getString("text"));
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		LTPService ls = new LTPService("nbu13jsjfh@163.com:gFLSVHzo"); 
		ls.setEncoding(LTPOption.UTF8);
		
		
		List<String> happyWords=new ArrayList<String>();
		List<String> angerWords=new ArrayList<String>();
		List<String> sadWords=new ArrayList<String>();
		List<String> fearWords=new ArrayList<String>();
		List<String> badWords=new ArrayList<String>();
		List<String> shockWords=new ArrayList<String>();
		
		
		
		for(int m=0;m<textWords.size();m++){
			LTML ltml = ls.analyze(LTPOption.PARSER,textWords.get(m));
			int sentNum = ltml.countSentence();
			for(int i=0;i<sentNum;i++){
				ArrayList<Word> wordList = ltml.getWords(i);
				for(int j=0;j<wordList.size();j++){
					String word=wordList.get(j).getWS();
					for(int k=0;k<txthappy.size();k++){
						if(txthappy.get(k).equals(word)){
							happyWords.add(word);
							break;
						}
					}
					for(int k=0;k<txtanger.size();k++){
						if(txtanger.get(k).equals(word)){
							angerWords.add(word);
							break;
						}
					}
					for(int k=0;k<txtsad.size();k++){
						if(txtsad.get(k).equals(word)){
							sadWords.add(word);
							break;
						}
					}
					for(int k=0;k<txtfear.size();k++){
						if(txtfear.get(k).equals(word)){
							fearWords.add(word);
							break;
						}
					}
					for(int k=0;k<txtbad.size();k++){
						if(txtbad.get(k).equals(word)){
							badWords.add(word);
							break;
						}
					}
					for(int k=0;k<txtshock.size();k++){
						if(txtshock.get(k).equals(word)){
							shockWords.add(word);
							break;
						}
					}
				}
			}
		}
		
		
		for(int i=0;i<happyWords.size();i++){
			Text.writerFileIsAppend(out+"/happy.txt", happyWords.get(i)+"\r\n");
			
		}
		for(int i=0;i<angerWords.size();i++){
			Text.writerFileIsAppend(out+"/anger.txt", angerWords.get(i)+"\r\n");
			
		}
		for(int i=0;i<sadWords.size();i++){
			Text.writerFileIsAppend(out+"/sad.txt", sadWords.get(i)+"\r\n");
			
		}
		for(int i=0;i<fearWords.size();i++){
			Text.writerFileIsAppend(out+"/fear.txt", fearWords.get(i)+"\r\n");
			
		}
		for(int i=0;i<badWords.size();i++){
			Text.writerFileIsAppend(out+"/bad.txt", badWords.get(i)+"\r\n");
			
		}
		for(int i=0;i<shockWords.size();i++){
			Text.writerFileIsAppend(out+"/shock.txt", shockWords.get(i)+"\r\n");
			
		}
		/*
		for(int i=0;i<happyWords.size();i++){
			if(i==0){
				Text.writerFileIsAppend(out, happyWords.get(i));
				continue;
			}
			ArrayList<String> wordlist=textWords(out);
			int k=0;
			for(;k<wordlist.size();k++)
			{
				if(wordlist.get(k).equals(happyWords.get(i))){
					break;
				}
			
			}
		}
		*/
		
	}
	
	public ArrayList<String> textWords(String infile) throws IOException{
		ArrayList<String> list=new ArrayList<String>();
		File file=new File(infile);
		FileInputStream fInputStream = new FileInputStream(file);
		BufferedReader br = new BufferedReader(new InputStreamReader(fInputStream, "UTF-8"));
		String str=null;
		while((str=br.readLine())!=null){
			list.add(str);
		}
		br.close();
		return list;
	}
}
