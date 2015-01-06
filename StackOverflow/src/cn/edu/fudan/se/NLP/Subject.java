package cn.edu.fudan.se.NLP;

import java.util.ArrayList;
import java.util.List;

public class Subject {
	
	private String subject;
    private List<WordProperty> subjectList;
    
   
    private String environment ="";
    
    public Subject()
    {
    	subjectList = new ArrayList<>();
    }
    
    public String getSubject()
    {
    	return subject;
    }
    
    public void addSubject(List<WordProperty> subject)
    {
    	subjectList.addAll(subject);
    }
    
    public void addSubject(WordProperty subjectItem)
    {
    	subjectList.add(subjectItem);
    }
    
    public List<WordProperty> getSubjectList()
    {
    	return subjectList;
    }
    
    public String getEnvironment()
    {
    	return environment;
    }
    
    public String getNoun()
    {
    	String noun = "";
    	for(WordProperty wp:subjectList)
    	{
    		if(wp.getProperty().contains("NN")||wp.getProperty().equals("WRB"))
    			noun += wp.getLemmaWord() + ",";
    	}
    	
    	return noun;
    }
    
    public void init()
    {
//    	extractSubject();
//    	extractEnvironment();
    }
    
    public void extractEnvironment()
    {
    	int size = subjectList.size();
    	environment = "";
    	WordProperty wp;
    	for(int i = 0 ; i < size; i++)
    	{
    		wp = subjectList.get(i);
    		
    		if(wp.isiS()&&(wp.getProperty().contains("NN")||wp.getProperty().equals("CD")||
    				wp.getProperty().contains("JJ")||wp.getProperty().contains("RB")||
    				wp.getProperty().equals("VBG")||wp.getProperty().equals("FW")||wp.getProperty().equals("IN")
    				||wp.getProperty().equals("#")))
    		{
    			wp.setiS(false);
    			environment += wp.getWord() + " ";
    		}
    	}
    }
    
    public void extractSubject()
    {
    	int size = subjectList.size();
    	subject = "";
    	WordProperty wp;
    	boolean isOpen = true;
    	for(int i = 0 ; i < size; i++)
    	{
    		wp = subjectList.get(i);
    		
    		if(wp.getProperty().equals("PRP"))
    			subject += wp.getWord() + " ";
    		if((wp.getProperty().equals("IN")&&!wp.getWord().equalsIgnoreCase("because"))||wp.getProperty().equals("``")
    				||wp.getProperty().equals("VBN"))
    		{
    			isOpen = false;
    		}
    		
    		if(isOpen&&(wp.getProperty().contains("NN")||wp.getProperty().equals("CD")||
    				wp.getProperty().equals("EX")||wp.getProperty().equals("FW")||wp.getProperty().equals("WP")
    				||wp.getProperty().equals("#")))
    		{
    			wp.setiS(false);
    			subject += wp.getWord() + ",";
    		}
    	}
    }
	
	
	public String toString()
	{
		String string = "";
		for(WordProperty wp:subjectList)
		{
			string += wp.getWord() + " ";
		}
		return string;
	}
	
	

}
