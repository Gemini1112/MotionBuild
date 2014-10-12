package com.textprproccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.File;
import java.util.Date; 
import java.text.*;
import nbu.deleteurl.test.HtmlRegexpUtil;
import nbu.file.txt.write.Text;
import pro.mysql.connection.MySqlConnection;

public class Preproccess {

	public boolean feilei(String huati)throws Exception{
		String src[]={"安徽","澳门","北京","福建","甘肃","广东","广西","贵州","海南","河北",
					"黑龙江","河南","湖北","湖南","吉林","江苏","江西","辽宁","内蒙古","宁夏",
					"青海","山东","山西","陕西","上海","四川","台湾","天津","西藏","香港",
					"新疆","云南","浙江","重庆","其他"};
		try
		{
			if(!(new File("E:/数据地区分类/"+huati).isDirectory()))
			{
				new File("E:/数据地区分类/"+huati).mkdir();
			}
		
		}
		catch(SecurityException e)
		{
		         System.out.println("can not make directory");
		}
		
		Statement stmt = null ;
		ResultSet rs = null;
		Connection con = null ;
		
		String sql="select * from weibo_info where huati='"+huati+"'";
		
		String driver=MySqlConnection.getDriver();
		String url=MySqlConnection.getUrl();
		String username=MySqlConnection.getUser();
		String password=MySqlConnection.getPassword();
		
		try{
			Class.forName (driver);
			con = DriverManager.getConnection (url, username, password); 
			stmt = con.createStatement();
			rs=stmt.executeQuery(sql);
			
			Date now = new Date(); 
			DateFormat d1 = DateFormat.getDateInstance();//默认语言（汉语）下的默认风格（MEDIUM风格，比如：2008-6-16 20:54:53）
		    String dir =huati+"_"+d1.format(now);
		  
		    while(rs.next()){
		    	String s1=HtmlRegexpUtil.filterHtml(HtmlRegexpUtil.filterJinhao(rs.getString("text").replaceAll("http:[a-zA-Z\\/\\.0-9]+", "")));
		    	int i=0;
		    	for(;i<34;i++){
		    		if(rs.getString("location").contains(src[i])){
		    			if(!(new File("E:/数据地区分类/"+huati+"/"+src[i]).isDirectory())){
							new File("E:/数据地区分类/"+huati+"/"+src[i]).mkdir();
						}
		    			if(!s1.isEmpty()){
		    				Text.writerFileIsAppend("E:/数据地区分类/"+huati+"/"+src[i]+"/"+src[i]+"_"+dir+".txt", s1+"\r\n");
		    			}
		    			
		    		}
		    	}
		    	if(i>=34){
		    		if(!(new File("E:/数据地区分类/"+huati+"/"+"其他").isDirectory())){
						new File("E:/数据地区分类/"+huati+"/"+"其他").mkdir();
					}
		    		if(!s1.isEmpty()){
		    			Text.writerFileIsAppend("E:/数据地区分类/"+huati+"/"+"其他"+"/"+"其他"+"_"+dir+".txt", s1+"\r\n");
					}
		    	}
		    }
		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}
	
	public static void Text(){
		
	}
	
	public static void main(String args[])throws Exception
	{
		new Preproccess().feilei("招远围殴凶杀");
	}
}
