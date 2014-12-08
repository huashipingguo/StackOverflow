package cn.edu.fudan.se.test;

import cn.edu.fudan.se.filter.QueryToken;
import cn.edu.fudan.se.result.Result;

public class MainTest {
	
	public static void main(String args[])
	{
		QueryToken qt = new QueryToken("sort list");
		Result result = new Result();
		result.setQueryToken(qt.getToken());
		result.init();
//		result.outPutTopicWordsDocument();
	}

}
