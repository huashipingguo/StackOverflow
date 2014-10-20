package cn.edu.fudan.se.bean.lda;



public class TopicToDocument
{
	private int topicID;
	private int documentID;
	private float documentDistributionValue;
	public int getTopicID()
	{
		return topicID;
	}
	public void setTopicID(int topicID)
	{
		this.topicID = topicID;
	}
	public int getDocumentID()
	{
		return documentID;
	}
	public void setDocumentID(int documentID)
	{
		this.documentID = documentID;
	}
	public float getDocumentDistributionValue()
	{
		return documentDistributionValue;
	}
	public void setDocumentDistributionValue(float documentDistributionValue)
	{
		this.documentDistributionValue = documentDistributionValue;
	}
	
}
