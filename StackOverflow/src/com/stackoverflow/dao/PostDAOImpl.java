package com.stackoverflow.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import cn.edu.fudan.se.filter.FilterPost;

import com.stackoverflow.bean.*;
import com.stackoverflow.utils.DBConnection;

public class PostDAOImpl implements PostDAO {
	public List<Post> findPosts(String keywords) {
		Connection conn = null;
		try {
			conn = DBConnection.getConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PreparedStatement pstmt = null;
		List<Post> posts = new ArrayList<Post>();
		// String keywords = "java,mysql,database";
		// generate query string based on separate keywords
		// using regexp replace('keyword1,keyword2',',','|')
		System.out.println("String:"+keywords);
//		String SQLString = "SELECT * FROM stackoverflowdata.posts WHERE PostTypeId = 1 and CONCAT(title,tags,body) regexp replace('"
//				+keywords+"',',','|') limit 20";
		
		String SQLString = "SELECT * FROM stackoverflowdata.posts WHERE PostTypeId = 1 and title like '%how%' and title like '%query%' and title like '%table%' limit 20";
//		String SQLString = "SELECT * FROM stackoverflowdata.posts WHERE CONCAT(title,tags,body) like '"
//				+ "sort%" +"'" +"or CONCAT(title,tags,body) like 'list%'";

		try {
			pstmt = conn.prepareStatement(SQLString);
			ResultSet rs = pstmt.executeQuery();
			posts = constructedPostList(rs);
		} catch (Exception ex) {
			System.out.println("Error : " + ex.toString());
		} finally {
			DBConnection.close(conn);
			DBConnection.close(pstmt);
		}
		return posts;
	}
	
	public List<Comment> findRelatedComments(int postId) throws Exception {
		Connection conn = DBConnection.getConnection();
		PreparedStatement pstmt = null;
		List<Comment> comments = new ArrayList<Comment>();

		String SQLString = "SELECT * FROM stackoverflowdata.comments WHERE PostId="+postId+";";

		try {
			pstmt = conn.prepareStatement(SQLString);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next())
			{
				int commentId = rs.getInt("Id");
				String text = rs.getString("Text");
				comments.add(new Comment(commentId,postId,text));
			}
		} catch (Exception ex) {
			System.out.println("Error : " + ex.toString());
		} finally {
			DBConnection.close(conn);
			DBConnection.close(pstmt);
		}
		return comments;
	}
	
	public List<Answer> findRelatedAnswers(int postId) throws Exception {
		Connection conn = DBConnection.getConnection();
		PreparedStatement pstmt = null;
		List<Answer> answers = new ArrayList<Answer>();

		String SQLString = "SELECT * FROM stackoverflowdata.newposts WHERE ParentId="+postId+";";

		try {
			pstmt = conn.prepareStatement(SQLString);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next())
			{
				//comment Attribute;
				int post_typeId = rs.getInt("PostTypeId");
				String post_title = rs.getString("title");
				String post_body = rs.getString("body");
				String post_tag = rs.getString("tags");
				int post_comment_count = rs.getInt("CommentCount");//may be null
				//Answer Attribute;
				int parentId = rs.getInt("parentID");//may be null
				//Question Attribute;
				int post_answer_count = rs.getInt("AnswerCount");//may be null
				int accepted_answerId = rs.getInt("AcceptedAnswerId");//may be null
				
				answers.add(new Answer(postId,post_title,post_body,post_tag,post_comment_count,parentId,post_answer_count,accepted_answerId));
			}		
			} catch (Exception ex) {
			System.out.println("Error : " + ex.toString());
		} finally {
			DBConnection.close(conn);
			DBConnection.close(pstmt);
		}
		return answers;
	}
	
	public Question findRelatedQuestion(int postId) throws Exception {
		Connection conn = DBConnection.getConnection();
		PreparedStatement pstmt = null;
		Question resultQuestion = null;
		String SQLString = "SELECT * FROM stackoverflowdata.newposts WHERE id="+postId+";";

		try {
			pstmt = conn.prepareStatement(SQLString);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next())
			{
				//comment Attribute;
				int post_typeId = rs.getInt("PostTypeId");
				String post_title = rs.getString("title");
				String post_body = rs.getString("body");
				String post_tag = rs.getString("tags");
				int post_comment_count = rs.getInt("CommentCount");//may be null
				//Answer Attribute;
				int parentId = rs.getInt("parentID");//may be null
				//Question Attribute;
				int post_answer_count = rs.getInt("AnswerCount");//may be null
				int accepted_answerId = rs.getInt("AcceptedAnswerId");//may be null
				
				resultQuestion=new Question(postId,post_title, post_body,post_tag,post_comment_count,parentId,post_answer_count,accepted_answerId);
			}		} catch (Exception ex) {
			System.out.println("Error : " + ex.toString());
		} finally {
			DBConnection.close(conn);
			DBConnection.close(pstmt);
		}
		return resultQuestion;
	}

	public List<Post> constructedPostList(ResultSet rs) throws SQLException{
		List<Post> posts = new ArrayList<Post>();
		int id = 0;
		try {
		while (rs.next()) {
			//comment Attribute;
			id++;
			int postId = rs.getInt("Id");
			int post_typeId = rs.getInt("PostTypeId");
			String post_title = rs.getString("title");

			String post_body = rs.getString("body");

			String post_tag = rs.getString("tags");
			int post_comment_count = rs.getInt("CommentCount");//may be null
			//Answer Attribute;
			int parentId = rs.getInt("parentID");//may be null
			//Question Attribute;
			int post_answer_count = rs.getInt("AnswerCount");//may be null
			int accepted_answerId = rs.getInt("AcceptedAnswerId");//may be null

//			if(!FilterPost.FilterAnswerCount(post_answer_count))
//				continue;
//			FilterPost.setFilterTag("mysql");
//			if(!FilterPost.FilterTag(post_tag))
//				continue;
			switch (post_typeId) {
			case 1: {
				Question question = new Question(postId,post_title, post_body,post_tag,post_comment_count,parentId,post_answer_count,accepted_answerId);
//				//generate commentList and AnswerList 
//				if(question.getPost_comment_count()>0)
//					question.setCommentList(this.findRelatedComments(question.getPostId()));
//				if(question.getPost_answer_count()>0)
//					question.setAnswerList(this.findRelatedAnswers(question.getPostId()));
				
				posts.add(question);
				break;
			}
			case 2: {
				Answer answer = new Answer(postId,post_title,post_body,post_tag,post_comment_count,parentId,post_answer_count,accepted_answerId);
				
//				//generate commentList and parent Question 
//				if(answer.getPost_comment_count()>0)
//					answer.setCommentList(this.findRelatedComments(answer.getPostId()));
//				if(answer.getParentId()>0)
//					answer.setParent_question(this.findRelatedQuestion(answer.getParentId()));
				
				posts.add(answer);
				break;
			}
			default:
				;
			}
		}
		}catch (Exception ex) {
			System.out.println("Error : " + ex.toString());
		}
		return posts;
	}

	@Override
	public List<Post> findPosts() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
