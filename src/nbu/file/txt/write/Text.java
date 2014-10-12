package nbu.file.txt.write;

import java.io.*;

public class Text {

	public static boolean writerFileIsAppend(String filePathName,String content){
		boolean flag=false;
		OutputStreamWriter osw=null;
		try {
			if (filePathName!=null&&!"".equals(filePathName)) {
				osw = new OutputStreamWriter(new FileOutputStream(filePathName,true));
			}
		} catch (Exception e1) {
			flag=false;
			e1.printStackTrace();
		} 
		if(osw!=null){
		BufferedWriter bw=new BufferedWriter(osw); 
		try {
			if(content!=null&&!"".equals(content)){
				bw.write(content);
				flag= true;
			}
		} catch (IOException e) {
			flag=false;
			e.printStackTrace();
		}finally{
			try {
				bw.close();
				osw.close();
			} catch (IOException e) {
				flag=false;
				e.printStackTrace();
			} 			
		}
		}
		return flag;
	}
	
	
	public static void main(String args[])throws Exception{
		File f=new File("E:\\1.txt");
		String s="世界杯";
		FileInputStream fInputStream = new FileInputStream(f);
		BufferedReader br = new BufferedReader(new InputStreamReader(fInputStream, "UTF-8"));
		String str=null;
		while((str=br.readLine())!=null){
			String ss=str.replaceAll("#"+s+"#", "");
			Text.writerFileIsAppend("E:\\2.txt",ss+"\r\n");
		}
	}
	
	
}

