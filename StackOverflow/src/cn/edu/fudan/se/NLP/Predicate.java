package cn.edu.fudan.se.NLP;

import java.util.ArrayList;
import java.util.List;

import cn.edu.fudan.se.domain.dictionary.InitDomainDic;

public class Predicate {
	private String predicate;
	private String environment;
	private String synonyms = "";
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
    public String getPredicate()
    {
    	return predicate;
    }
    public String getEnvironment()
    {
    	return environment;
    }
    
    public String getVerb()
    {
    	String verb = "";
    	for(WordProperty wp:predicateList)
    	{
    		if(wp.getProperty().contains("V"))
    			verb += wp.getLemmaWord() + ",";
    	}
    	
    	return verb;
    }
    public void init()
    {
//    	extractPredicate();
//    	extractEnvironment();
    	
    	for(WordProperty wp:predicateList)
    	{
    		if(wp.getProperty().contains("V"))
    		{
    			synonyms +=InitDomainDic.getInstance().getSynonyms(wp.getLemmaWord())+ ",";
    		}
    	}
    	
    	
    }
    public void extractEnvironment()
    {
    	int size = predicateList.size();
    	WordProperty wp;
    	environment = "";
    	for(int i = 0 ; i < size ; i ++)
    	{
    		wp = predicateList.get(i);
    		if(wp.isiS()&&(wp.getProperty().equals("MD")||wp.getProperty().equals("RB")||
    				wp.getProperty().contains("NN")))
    		{
    			environment += wp.getWord() + " ";
    			wp.setiS(false);
    		}
    	}
    }
    public void extractPredicate()
    {
    	int size = predicateList.size();
    	WordProperty wp;
    	predicate = "";
    	for(int i = 0 ; i < size ; i ++)
    	{
    		wp = predicateList.get(i);
    		if(wp.getProperty().equals("VB")||wp.getProperty().equals("VBZ")||wp.getProperty().equals("VBP")
    				||wp.getProperty().equals("VBD")||wp.getProperty().equals("VBN"))
    		{
    			predicate += wp.getWord() + ",";
    			wp.setiS(false);
    		}
    	}
    }
    
    public String getSynonyms()
    {
    	return synonyms;
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
