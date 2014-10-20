package cn.edu.fudan.se.util;

import java.io.File;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


//import cn.edu.fudan.se.enumertion.Language;

public class CommUtil 
{
	private static final String SPLITE_STRING = "[ ]";
	

	public static boolean stringNullOrZero(String str)
	{
		return str== "" || str.trim().length() == 0;
	}
	
	public static int CompareStr(String str1,String str2)
	{
		
		if( str1==null)
			str1="";
		if(str2 ==null)
			str2="";
		
		return str1.compareTo(str2);
	}

	
	public static String getCurrentProjectPath()
	{	
		return new File("").getAbsolutePath();
	}
	
	public static String getProjectNameFromProjectPath(String projectPath)
	{
		return new File(projectPath).getName();
	}
	
	public static String ListToString(List list)
	{
		String result = "";
		for(Object object : list)
		{
			if(object!=null)
				result = result +  object.toString() + " " ;
			
		}
		return result.trim();
	}

	public static List<String> stringToList(String strContent)
	{
		List<String> result = new ArrayList<String>();
		for(String str : strContent.split(SPLITE_STRING))
		{
			if(str.trim().length()>0)
				result.add(str);			
		}
		return result;
	}
	
	public static String getDateTime()
	{
		String result;
		
		GregorianCalendar calendar = new GregorianCalendar();
		result = calendar.get(Calendar.YEAR) + "" + (calendar.get(Calendar.MONTH) + 1) + "" + calendar.get(Calendar.DATE);
		return result;
		
	}
	

	public static String trimOnlySign(String fullMethodName)
	{
	    String trimMethodName=fullMethodName;
		int indexP=fullMethodName.indexOf("(", 0);
		trimMethodName=fullMethodName.substring(0, indexP);
			
		//System.out.println(trimMethodName);
					
		return trimMethodName;
		
	}
	
	public static String trimMethodAndSign(String fullMethodName)
	{
	    String trimMethodName=fullMethodName;
		int indexP=fullMethodName.indexOf("(", 0);
		trimMethodName=fullMethodName.substring(0, indexP);
		indexP=trimMethodName.lastIndexOf(".");
		trimMethodName=trimMethodName.substring(0, indexP);
		
		//System.out.println(trimMethodName);
		
				
		return trimMethodName;
		
	}
	
}
