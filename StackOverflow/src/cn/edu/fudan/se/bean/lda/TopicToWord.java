package cn.edu.fudan.se.bean.lda;

/*
create table if not exists topicToWords
(
	topicID smallint not null,
	wordID int,
	wordDistributionValue double,
	projectPath varchar(1000)
);*/
public class TopicToWord
{
	private int topicID;
	private int wordID;
	private float wordDistributionValue;
	public int getTopicID()
	{
		return topicID;
	}
	public void setTopicID(int topicID)
	{
		this.topicID = topicID;
	}
	public int getWordID()
	{
		return wordID;
	}
	public void setWordID(int wordID)
	{
		this.wordID = wordID;
	}
	public float getWordDistributionValue()
	{
		return wordDistributionValue;
	}
	public void setWordDistributionValue(float wordDistributionValue)
	{
		this.wordDistributionValue = wordDistributionValue;
	}
	
}
