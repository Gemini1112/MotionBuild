package nbu.deleteurl.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import nbu.file.txt.write.Text;

public class DeleteUrl {

	public static void main(String[] args)throws Exception {
		// TODO Auto-generated method stub
		String f="E:\\美国全球监听.txt";
		File file=new File(f);
		FileInputStream fInputStream = new FileInputStream(file);
		List<String> Sentences=new ArrayList<String>();
		BufferedReader br = new BufferedReader(new InputStreamReader(fInputStream, "UTF-8"));
		String str=null;
		while((str=br.readLine())!=null){
			Sentences.add(str);
			String s=HtmlRegexpUtil.filterHtml(HtmlRegexpUtil.filterJinhao(str.replaceAll("http:[a-zA-Z\\/\\.0-9]+", "")));
			Text.writerFileIsAppend("E:\\test3.txt", s+"\r\n");
			System.out.println(s);
		}
		br.close();

	}

}
