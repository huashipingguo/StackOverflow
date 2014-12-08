package cn.edu.fudan.se.NLP;

import java.util.ArrayList;
import java.util.List;

public class Subject {
	
	private String subject;
    private List<WordProperty> subjectList;
    public Subject()
    {
    	subjectList = new ArrayList<>();
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
