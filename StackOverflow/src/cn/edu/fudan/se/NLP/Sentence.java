package cn.edu.fudan.se.NLP;

import java.util.ArrayList;
import java.util.List;

public class Sentence {
	
	private String sentence;
	
	private List<Clause> clauseList;
	
	public Sentence()
	{
		clauseList = new ArrayList<>();
	}
	
	public void addClause(Clause clause)
	{
		clauseList.add(clause);
	}
	
	public List<Clause> getClauseList()
	{
		return clauseList;
	}

	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}
	
	public void setClause(List<List<WordProperty>> splitList)
	{
		for(List<WordProperty> list:splitList)
		{
			String word = "";
			for(WordProperty wp:list)
			{
				word += wp.getWord() +" ";
			}
			Clause clause = new Clause();
			clause.setClause(word);
			clauseList.add(clause);
		}
	}
	
	

}
