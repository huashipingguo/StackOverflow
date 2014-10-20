package cn.edu.fudan.se.bean.lda;

public class WordToDocument {
	
	private int topicID;
	private int wordID;
	private int documentID;
	private float documentDistributionValue;
	
	private String wordIdString = "";
	
	public String getWordIdString()
	{
		return wordIdString;
	}
	
	public void setWordIdString(String id)
	{
		wordIdString += id+",";
	}
	
	public int getTopicID() {
		return topicID;
	}
	public void setTopicID(int topicID) {
		this.topicID = topicID;
	}
	public int getWordID() {
		return wordID;
	}
	public void setWordID(int wordID) {
		this.wordID = wordID;
	}
	public int getDocumentID() {
		return documentID;
	}
	public void setDocumentID(int documentID) {
		this.documentID = documentID;
	}
	public float getDocumentDistributionValue() {
		return documentDistributionValue;
	}
	public void setDocumentDistributionValue(float documentDistributionValue) {
		this.documentDistributionValue = documentDistributionValue;
	}

}
