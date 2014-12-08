package cn.edu.fudan.se.NLP;

import java.util.ArrayList;
import java.util.List;

public class Adverbial {
	
	private String Adverbial;
    private List<WordProperty> adverbialList;
    public Adverbial()
    {
    	adverbialList = new ArrayList<>();
    }
    
    public void addAdverbial(List<WordProperty> adverbial)
    {
    	adverbialList.addAll(adverbial);
    }
    
    public void addSubject(WordProperty adverbialItem)
    {
    	adverbialList.add(adverbialItem);
    }
    
    public List<WordProperty> getadverbialList()
    {
    	return adverbialList;
    }
	
	public String toString()
	{
		String string = "";
		for(WordProperty wp:adverbialList)
		{
			string += wp.getWord() + " ";
		}
		return string;
	}


}
