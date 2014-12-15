package cn.edu.fudan.se.NLP;

import java.util.ArrayList;
import java.util.List;

public class Object {
	
	private String object;
	private String environment = "";
    private List<WordProperty> objectList;
    public Object()
    {
    	objectList = new ArrayList<>();
    }
    
    public String getObject()
    {
    	return object;
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
    
    public String getEnvironment()
    {
    	return environment;
    }
    
    public void init()
    {
    	extractObject();
    	extractEnvironment();
    }
    
    public void extractEnvironment()
    {
    	int size = objectList.size();
    	environment = "";
    	WordProperty wp;
    	for(int i = 0 ; i < size; i++)
    	{
    		wp = objectList.get(i);
    		
    		if(wp.isiS()&&(wp.getProperty().contains("NN")||wp.getProperty().equals("CD")||
    				wp.getProperty().contains("JJ")||wp.getProperty().equals("VBG")||wp.getProperty().equals("FW")||
    				wp.getProperty().equals("RB")||wp.getProperty().equals("#")||
    				wp.getProperty().equals("IN")||wp.getProperty().equals("VBN")))
    		{
    			wp.setiS(false);
    			environment += wp.getWord() + " ";
    		}
    	}
    }
    
    public void extractObject()
    {
    	int size = objectList.size();
    	object = "";
    	WordProperty wp;
    	boolean isOpen = true;
    	for(int i = 0 ; i < size; i++)
    	{
    		wp = objectList.get(i);
    		if(wp.getProperty().equals("IN")||wp.getProperty().equals("TO")||wp.getProperty().equals("``")
    				||wp.getProperty().equals("VBN"))
    		{
    			isOpen = false;
    		}
    		
    		if(isOpen&&(wp.getProperty().contains("NN")||wp.getProperty().equals("CD")||wp.getProperty().equals("FW")||wp.getProperty().equals("#")))
    		{
    			wp.setiS(false);
    			object += wp.getWord() + " ";
    		}
    	}
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
