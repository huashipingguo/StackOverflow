package cn.edu.fudan.se.NLP;

import java.util.ArrayList;
import java.util.List;

public class Object {
	
	private String object;
    private List<WordProperty> objectList;
    public Object()
    {
    	objectList = new ArrayList<>();
    }
    
    public void addSubject(List<WordProperty> object)
    {
    	objectList.addAll(object);
    }
    
    public void addObject(WordProperty objectItem)
    {
    	objectList.add(objectItem);
    }
    
    public List<WordProperty> getobjectList()
    {
    	return objectList;
    }
	
	public String toString()
	{
		String string = "";
		for(WordProperty wp:objectList)
		{
			string += wp.getWord() + " ";
		}
		return string;
	}

}
