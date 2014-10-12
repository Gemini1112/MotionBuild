package nbu.json.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

import pro.mysql.connection.MySqlConnection;

public class Test {
	public static void main(String[] args){
		
		Statement stmt = null ;
		ResultSet rs = null;
		Connection con = null ;
	
		String driver=MySqlConnection.getDriver();
		String url=MySqlConnection.getUrl();
		String username=MySqlConnection.getUser();
		String password=MySqlConnection.getPassword();
	
		String slt="select * from tongji";
	
		try{
			Class.forName (driver);
			con = DriverManager.getConnection (url, username, password); 
			stmt = con.createStatement();
			rs=stmt.executeQuery(slt);
			JSONStringer js = new JSONStringer();
			JSONObject obj2 = new JSONObject();
			JSONObject obj3 = new JSONObject();
			JSONObject obj4 = new JSONObject();
			JSONArray list= new JSONArray(); 
			while(rs.next()){
				
				JSONObject obj1=new JSONObject();
				obj1.put("label", rs.getString("text")).put("anger", rs.getString("anger")).put("fear", rs.getString("fear")).put("latLng", rs.getString("latLng")).put("type", "province");
				list.put(obj1);
				
			}
//			obj2.put("pluralLabel", "provinces");
//			obj4.put("items", list);
//			obj4.put("types", obj2);
			js.object().key("items").value(list).endObject();
			System.out.println(js.toString());
		
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
