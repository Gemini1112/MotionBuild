package com.main;

import java.io.File;

import nbu.nlp.examples.Parsing;

import com.textprproccess.Preproccess;

public class Main {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		long startTime=System.currentTimeMillis();
		String huati="招远围殴凶杀";
	    new Preproccess().feilei(huati);
	    
	    String dir="E:/数据地区分类/"+huati;
		File f=new File(dir);
		String test[];
		test=f.list();
		for(int i=0;i<test.length;i++){
			File tempfile=new File(dir+"/"+test[i]);
			String name[];
			name=tempfile.list();
			for(int j=0;j<name.length;j++){
				Parsing par=new Parsing();
				par.MotionComput(dir+"/"+test[i]+"/"+name[j],dir+"/"+test[i]+"/"+test[i]+"_"+huati+"_motionScore.txt");
			}
		}
		long endTime=System.currentTimeMillis(); 
		System.out.println("程序运行时间： "+(endTime-startTime)+"ms");  			
	}

}
