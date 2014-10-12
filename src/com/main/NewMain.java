package com.main;

import com.nbu.edu.parsing.NewParsing;

public class NewMain {
	public static void main(String args[]) throws Exception{
		
		
		String huati[]={/*"大老虎",*/"招远围殴凶杀"};
		String src[]={/*"安徽","澳门",*/"北京","福建","甘肃","广东","广西","贵州","海南","河北",
				"黑龙江","河南","湖北","湖南","吉林","江苏","江西","辽宁","内蒙古","宁夏",
				"青海","山东","山西","陕西","上海","四川","台湾","天津","西藏","香港",
				"新疆","云南","浙江","重庆"};
		String type[]={/*"happy","angery",*/"sad","fear","bad","shock"};
		for(int k=0;k<huati.length;k++){
			for(int i=0;i<src.length;i++){
				for(int j=0;j<type.length;j++){
					NewParsing.Parsing(src[i],huati[k],type[j],"E:/各地区情感强度");
				}
			}
		}
//		NewParsing.Parsing("上海", "乌鲁木齐爆炸", "fear", "E:/各地区情感强度");
	}
}
