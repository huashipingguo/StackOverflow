package cn.edu.fudan.se.NLP;

import java.util.ArrayList;
import java.util.List;

public class Predicate {
	private String Predicate;
    private List<WordProperty> predicateList;
    public Predicate()
    {
    	predicateList = new ArrayList<>();
    }
    
    public void addPredicateList(List<WordProperty> predicate)
    {
    	predicateList.addAll(predicate);
    }
    
    public void addPredicate(WordProperty predicateItem)
    {
    	predicateList.add(predicateItem);
    }
    
    public List<WordProperty> getPredicateList()
    {
    	return predicateList;
    }
	
	public String toString()
	{
		String string = "";
		for(WordProperty wp:predicateList)
		{
			string += wp.getWord() + " ";
		}
		return string;
	}

}
