package cn.edu.fudan.se.filter;

import java.util.ArrayList;
import java.util.List;

public class FilterPost {
	
	private static List<String> tagList = new ArrayList<String>();
	
	public static void setFilterTag(String tag)
	{
		String[] tagS = tag.split(",");
		for(int i = 0; i < tagS.length; i++)
		{
			tagList.add(tagS[i]);
		}
	}
	
	public static boolean FilterTag(String tag)
	{
		for(int i = 0; i < tagList.size(); i ++)
		{
			if(!tag.contains(tagList.get(i)))
			{
				return false;
			}
		}
		return true;
	}
	public static boolean FilterAnswerCount(int count)
	{
		if(count == 0)
			return false;
		return true;
	}
	


}
