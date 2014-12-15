package cn.edu.fudan.se.NLP;

public class WordProperty {
	
	private String word;
	private String property;
	private String lemma;
	private boolean iS;
	
	public WordProperty(String word,String property,boolean iS)
	{
		this.word = word;
		this.property = property;
		this.iS = iS;
	}
	
	public WordProperty(String word,String lemma,String property,boolean iS)
	{
		this.word = word;
		this.property = property;
		this.lemma = lemma;
		this.iS = iS;
	}
	
	
	public String getLemmaWord()
	{
		return lemma;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public boolean isiS() {
		return iS;
	}
	public void setiS(boolean iS) {
		this.iS = iS;
	}
	

}
