package pro.mysql.connection;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;



public class MySqlConnection {
	public static Properties getProperties() {

	      Properties props = new Properties();
	      InputStream is = null ;
	      try {
	         is = MySqlConnection . class .getResourceAsStream( "./jdbc_mysql.properties" ); // 按照参数路劲获得属性文件构造文件输入流
	         props.load(is); // 从输入流中读取属性表
	      } catch (Exception e1) {
	         e1.printStackTrace();
	         return null ;
	      }
	      finally {
	         if (is != null ) {
	            try {
	                is.close();
	            } catch (IOException e2) {
	            	e2.printStackTrace();
	            }

	         }

	      }

	 

	      return props;

	   }
	
	public static String getDriver(){
		Properties props = getProperties ();
		return props.getProperty( "driver" );
	}
	
	public static String getUrl(){
		Properties props = getProperties ();
		return props.getProperty( "url" );
	}
	
	public static String getUser(){
		Properties props = getProperties ();
		return props.getProperty( "user" );
	}
	
	public static String getPassword(){
		Properties props = getProperties ();
		return props.getProperty( "password" );
	}
}
