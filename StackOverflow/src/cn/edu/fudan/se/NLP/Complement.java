package cn.edu.fudan.se.NLP;

import java.util.ArrayList;
import java.util.List;

public class Complement {
	
	private String complement;
    private List<WordProperty> complementList;
    public Complement()
    {
    	complementList = new ArrayList<>();
    }
    
    public void addSubject(List<WordProperty> complement)
    {
    	complementList.addAll(complement);
    }
    
    public void addComplement(WordProperty complementItem)
    {
    	complementList.add(complementItem);
    }
    
    public List<WordProperty> getcomplementList()
    {
    	return complementList;
    }
	
	public String toString()
	{
		String string = "";
		for(WordProperty wp:complementList)
		{
			string += wp.getWord() + " ";
		}
		return string;
	}


}
