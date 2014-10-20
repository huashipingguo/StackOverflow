package cn.edu.fudan.se.dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.edu.fudan.se.bean.Word;
//import cn.edu.fudan.se.bean.ast.Class;
import cn.edu.fudan.se.bean.lda.Document;
import cn.edu.fudan.se.bean.lda.TopicToDocument;
import cn.edu.fudan.se.bean.lda.TopicToWord;
import cn.edu.fudan.se.bean.lda.WordToDocument;



public class LDADAL extends ProjectDAL
{
	
	
	public static List<String> getTokesOfFiles( List<String> contentList , String projectName)
	
	{
		List<String> result=null;
        
		
		String sql= " SELECT fileName, tokens FROM " + DBHelper.TOKEN_TABLE_NAME +
				" WHERE projectName = " +projectName;
		ResultSet rs=DBHelper.executeQuery(sql);
		try
		{
			while(rs.next())
			{
				result.add(rs.getString(2));
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			// TODO: handle exception
		}
		
		
		return result;
	}
	public static List<String> generateFileNameList( List<String> classNameList , String projectName)
	{
		List<String> result=new ArrayList<String>();
        List<String> copyOfClassNameList=new ArrayList<String>();
        
        for(String temp: classNameList)
        	{
        	copyOfClassNameList.add(temp);
        	}
		
		String sql= " SELECT fileName, tokens FROM " + DBHelper.TOKEN_TABLE_NAME +
				" WHERE projectName = '" +projectName +"' ";
		ResultSet rs=DBHelper.executeQuery(sql);
		try
		{
			while(rs.next())
			{	
				String temp=rs.getString(1);
				for(String className : copyOfClassNameList )
				{
					if (temp.contains(className)==true)
				         {
						 result.add(temp);
						 break;
	//					 copyOfClassNameList.remove(copyOfClassNameList.indexOf(className));
				         }
				}	
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			// TODO: handle exception
		}
		
		
		return result;
	
	}
	
	public static List<TopicToWord> getTopWordsByTopic(int topicID,
			int count)
	{
		List<TopicToWord> result = new ArrayList<TopicToWord>();
		String sql = " SELECT topicID, wordID,wordDistributionValue from " +
				DBHelper.TOPIC_WORDS_TABLE_NAME +
				" where topicid = "
				+ topicID + " order by wordDistributionValue desc";

		ResultSet rs = DBHelper.executeQuery(sql);
		try
		{
			while (rs.next() && count > 0)
			{
				TopicToWord ttw = new TopicToWord();
				ttw.setTopicID(rs.getInt(1));
				ttw.setWordID(rs.getInt(2));
				ttw.setWordDistributionValue(rs.getFloat(3));
				result.add(ttw);
				count --;
			}
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public static int getTopicCount()
	{
		int result = -1;
		String sql =" SELECT max(topicid) from topictodocument ";
		ResultSet rs = DBHelper.executeQuery(sql);
		try
		{
			while(rs.next())
			{
				result = rs.getInt(1) + 1;
			}
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public static void inserTopicToWord(List<TopicToWord> topicToWords)
	{
		
		PreparedStatement pst = null;
		String sql = "insert into "
				+ DBHelper.TOPIC_WORDS_TABLE_NAME
				+ " (projectname,topicID,wordID, wordDistributionValue) values ('"
				+ DBHelper.getCurrentProjectName() + "',?,?,?)";
		try
		{
		
			pst = DBHelper.getConn().prepareStatement(sql);
			
			for (TopicToWord topictoWord : topicToWords)
			{
				pst.setInt(1, topictoWord.getTopicID());
				pst.setInt(2, topictoWord.getWordID());
				pst.setDouble(3, topictoWord.getWordDistributionValue());
				pst.addBatch();
			}
			pst.executeBatch();

		}
		
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void inserWordToDocument(List<WordToDocument> wordToDocument)
	{
		
		PreparedStatement pst = null;
		String sql = "insert into "
				+ DBHelper.WORD_DOCUMENT_TABLE_NAME
				+ " (projectname,topicID,wordID,documentID, documentDistributionValue) values ('"
				+ DBHelper.getCurrentProjectName() + "',?,?,?,?)";
		try
		{
		
			pst = DBHelper.getConn().prepareStatement(sql);
			
			for (WordToDocument newWordToDocument : wordToDocument)
			{
				pst.setInt(1, newWordToDocument.getTopicID());
				pst.setInt(2, newWordToDocument.getWordID());
				pst.setInt(3, newWordToDocument.getDocumentID());
				pst.setDouble(4, newWordToDocument.getDocumentDistributionValue());
				pst.addBatch();
			}
			pst.executeBatch();

		}
		
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}


	public static void insertTopicToDocument(
			List<TopicToDocument> topicToDocuments)
	{
		PreparedStatement pst = null;
		String sql = "insert into "
				+ DBHelper.TOPIC_DOCUMENT_TABLE_NAME
				+ " (projectname,topicID,documentID, documentDistributionValue) values ('"
				+ DBHelper.getCurrentProjectName() + "',?,?,?)";
		try
		{
			pst = DBHelper.getConn().prepareStatement(sql);
			for (TopicToDocument topictoDocument : topicToDocuments)
			{
				pst.setInt(1, topictoDocument.getTopicID());
				pst.setInt(2, topictoDocument.getDocumentID());
				pst.setDouble(3, topictoDocument.getDocumentDistributionValue());
				pst.addBatch();
			}
			pst.executeBatch();

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public static void insertWords(List<Word> words)
	{
		PreparedStatement pst = null;
		String sql = "insert into " + DBHelper.WORD_TABLE_NAME
				+ " (projectname,id,name) values ('"
				+ DBHelper.getCurrentProjectName() + "',?,?)";
		try
		{
			pst = DBHelper.getConn().prepareStatement(sql);
			for (Word word : words)
			{
				pst.setInt(1, word.getId());
				pst.setString(2, word.getName());
				pst.addBatch();
			}
			pst.executeBatch();

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

public static void insertDocuments(List<Document> documents)
	{
		PreparedStatement pst = null;
		String sql = "insert into " + DBHelper.DOCUMENT_TABLE_NAME
				+ " (projectname,id,name) values ('"
				+ DBHelper.getCurrentProjectName() + "',?,?)";
		try
		{
			pst = DBHelper.getConn().prepareStatement(sql);
			for (Document document : documents)
			{
				pst.setInt(1, document.getId());
				pst.setString(2, document.getName());
				pst.addBatch();
			}
			pst.executeBatch();			

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

	}

	public static List<Document> getDocuments()
	{
		List<Document> result = new ArrayList<Document>();
		String sql = "SELECT id,name  FROM " + DBHelper.DOCUMENT_TABLE_NAME
				+ " where projectName ='" + DBHelper.getCurrentProjectName()
				+ "' ";
		ResultSet rs = DBHelper.executeQuery(sql);
		try
		{
			while (rs.next())
			{
				Document document = new Document();
				document.setId(rs.getInt(1));
				document.setName(rs.getString(2));
				result.add(document);
			}

		}
		catch (Exception exp)
		{
			exp.printStackTrace();
		}
		return result;
	}

	public static List<Word> getWords()
	{
		List<Word> result = new ArrayList<Word>();
		String sql = "SELECT id,name  FROM " + DBHelper.WORD_TABLE_NAME
				+ " where projectName ='" + DBHelper.getCurrentProjectName()
				+ "' ";
		ResultSet rs = DBHelper.executeQuery(sql);
		try
		{
			while (rs.next())
			{
				Word word = new Word();
				word.setId(rs.getInt(1));
				word.setName(rs.getString(2));
				result.add(word);
			}

		}
		catch (Exception exp)
		{
			exp.printStackTrace();
		}
		return result;
	}

public static List<TopicToDocument> getTopicToDocument()
	{
		List<TopicToDocument> result = new ArrayList<TopicToDocument>();
		String sql = "SELECT topicID,documentID, documentDistributionValue FROM "
				+ DBHelper.TOPIC_DOCUMENT_TABLE_NAME
				+ " where projectName ='"
				+ DBHelper.getCurrentProjectName() + "' ";
		ResultSet rs = DBHelper.executeQuery(sql);
		try
		{
			while (rs.next())
			{
				TopicToDocument topicToDocument = new TopicToDocument();
				topicToDocument.setTopicID(rs.getInt(1));
				topicToDocument.setDocumentID(rs.getInt(2));
				topicToDocument.setDocumentDistributionValue(rs.getFloat(3));
				result.add(topicToDocument);
			}

		}
		catch (Exception exp)
		{
			exp.printStackTrace();
		}
		return result;
	}
	
	public static void clearLDADataBase(String projectName)
	{
		ProjectDAL.clearProjectData(projectName, DBHelper.TOPIC_WORDS_TABLE_NAME);
		ProjectDAL.clearProjectData(projectName, DBHelper.TOPIC_DOCUMENT_TABLE_NAME);
		ProjectDAL.clearProjectData(projectName, DBHelper.DOCUMENT_TABLE_NAME);
		ProjectDAL.clearProjectData(projectName, DBHelper.WORD_TABLE_NAME);
		
	}
	/*
	 * create table if not exists topicToWord ( topicID int not null, wordID
	 * int, wordDistributionValue double, projectPath varchar(1000) );
	 */
	public static List<TopicToWord> getTopicToWord()
	{
		
		List<TopicToWord> result = new ArrayList<TopicToWord>();
		String sql = "SELECT topicID,wordID, wordDistributionValue FROM "
				+ DBHelper.TOPIC_WORDS_TABLE_NAME				
				+ " where projectName ='"
				+ DBHelper.getCurrentProjectName() + "' ";
		ResultSet rs = DBHelper.executeQuery(sql);
		try
		{
			while (rs.next())
			{
				TopicToWord topicToWord = new TopicToWord();
				topicToWord.setTopicID(rs.getInt(1));
				topicToWord.setWordID(rs.getInt(2));
				topicToWord.setWordDistributionValue(rs.getFloat(3));
				result.add(topicToWord);
			}

		}
		catch (Exception exp)
		{
			exp.printStackTrace();
		}
		return result;
	}
	
	public static void clearRelatedData()
	{
		clearProjectData(DBHelper.getCurrentProjectName(),
				DBHelper.DOCUMENT_TABLE_NAME);
		clearProjectData(DBHelper.getCurrentProjectName(),
				DBHelper.TOPIC_WORDS_TABLE_NAME);
		clearProjectData(DBHelper.getCurrentProjectName(),
				DBHelper.TOPIC_DOCUMENT_TABLE_NAME);
		clearProjectData(DBHelper.getCurrentProjectName(),
				DBHelper.WORD_TABLE_NAME);
		clearProjectData(DBHelper.getCurrentProjectName(),
				DBHelper.WORD_DOCUMENT_TABLE_NAME);
	}
}
