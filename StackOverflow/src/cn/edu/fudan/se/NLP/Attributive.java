package cn.edu.fudan.se.NLP;

import java.util.ArrayList;
import java.util.List;

public class Attributive {
	
	private String attributive;
    private List<WordProperty> attributiveList;
    public Attributive()
    {
    	attributiveList = new ArrayList<>();
    }
    
    public void addattributive(List<WordProperty> attributive)
    {
    	attributiveList.addAll(attributive);
    }
    
    public void addattributive(WordProperty attributiveItem)
    {
    	attributiveList.add(attributiveItem);
    }
    
    public List<WordProperty> getattributiveList()
    {
    	return attributiveList;
    }
	
	public String toString()
	{
		String string = "";
		for(WordProperty wp:attributiveList)
		{
			string += wp.getWord() + " ";
		}
		return string;
	}


}
