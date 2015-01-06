package cn.edu.fudan.se.result;

import cn.edu.fudan.se.bean.Word;
import cn.edu.fudan.se.bean.lda.TopicToDocument;
import cn.edu.fudan.se.bean.lda.TopicToWord;
import cn.edu.fudan.se.datasource.LDAOperator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.stackoverflow.bean.Answer;
import com.stackoverflow.bean.Post;
import com.stackoverflow.bean.Question;
import com.stackoverflow.dao.PostDAOImpl;


public class Result {
    float[][] topic_word;
    float[][] document_topic;
    float topicWordThreshold = 0, topicDocumentThreshold = 0;
    int wordNumThreshold = 4, documentNumThreshold = 2;
    HashMap<Integer, String> topicWord;
    HashMap<Integer, String> topicDocument;
    List<Word> words;
    List<Post> posts;
    List<Integer> sortTopic;
    LDAOperator LDAO;
    private String queryToken;
    
    public Result(String queryToken)
    {
    	LDAO = LDAOperator.getInstance();
        PostDAOImpl pdi = new PostDAOImpl();
        System.out.println(queryToken);
        posts = pdi.findPosts(queryToken);
//        init();
    }
    
    public List<Post> getPosts()
    {
    	
    	return posts;
    }

    public void init() {
    	
    	try {    
    		File file = new File("C:\\Users\\jqt\\Desktop\\test.txt");    
    		FileOutputStream fos = new FileOutputStream(file);    
    		OutputStreamWriter osw = new OutputStreamWriter(fos);     
    		BufferedWriter bw = new BufferedWriter(osw);    
    	    Post post;
    	    int id = 0;
    	    for(int i = 0; i < posts.size(); i++)
    	    {
    	    	id ++;
    	        post = posts.get(i);
    	        String s1  = id+" : "+ post.post_title+"\r\n" + post.post_body_text + "\r\n";
    	        bw.write(s1);    
        		bw.newLine();        
        		bw.flush();     	          
    	    }
    	        	
    		
    		bw.close();  
    		osw.close();    
    	    fos.close();  
    	}
    	catch (FileNotFoundException e1)
    	{     
    		e1.printStackTrace();   
    	} catch (IOException e2)
    	{   
    		e2.printStackTrace();   
    	} 
//        LDAO.doLDAAnalysis(posts);
//        
//        topic_word = LDAO.getTopicWord();
//        document_topic = LDAO.getDocumentTopic();
//        words = LDAO.getWordList();
//        
//        topicWord = new HashMap<Integer, String>();
//        topicDocument = new HashMap<Integer, String>();
//        sortTopic =  new ArrayList<Integer>();
//        
//        processTopicWord();
//        porcessTopicDocumentAndSortTopic();
        
        
    }
    

    public void processTopicWord() {
    	List<IdValue> list = new ArrayList<IdValue>();
    	String wordSet;
    	IdValue iv;
        for (int i = 0; i < topic_word.length; i++)
        {
        	wordSet = "";
         	list.clear();
            for (int j = 0; j < topic_word[i].length; j++) {
            	list.add(new IdValue(j,topic_word[i][j]));
            }
            ComparatorIdValue comparator=new ComparatorIdValue();
          	Collections.sort(list, comparator);
          	
          	for(int j = 0; j < list.size(); j ++)
          	{
          		iv = list.get(j);
          		if(iv.getValue() >= topicWordThreshold&&j < wordNumThreshold)
          			wordSet += words.get(iv.getId()).getName()+",";
          	}
          	topicWord.put(i, wordSet);
        }
    }
    
    public void porcessTopicDocumentAndSortTopic()//对topic和document的相关度进行排序并对topic进行排序
    {
    	int topicNum = topic_word.length;
    	List<IdValue> list = new ArrayList<IdValue>();
    	List<IdValue> sortTopicList = new ArrayList<IdValue>();
    	float tempValue;
    	String documentSet;
    	IdValue iv;
    	for(int i = 0;i < topicNum; i ++)
    	{
    		list.clear();
    		documentSet = "";
    		for(int j = 0; j < document_topic.length; j ++)
    		{
    			list.add(new IdValue(j,document_topic[j][i]));
    		}
    		ComparatorIdValue comparator=new ComparatorIdValue();
           	Collections.sort(list, comparator);
           	tempValue = 0;
           	for(int j = 0; j < list.size();j ++)
           	{
           		iv = list.get(j);
           		if(iv.getValue() >= topicDocumentThreshold&&j < documentNumThreshold)
           		{
           			documentSet += iv.getId() + ",";
           			tempValue += iv.getValue();//topic对应的所有document的相关度进行求和用于对topic进行排序
           		}
           	}
           	sortTopicList.add(new IdValue(i,tempValue));
           	topicDocument.put(i, documentSet);
    	}
    	ComparatorIdValue comparator=new ComparatorIdValue();//对topic进行排序
    	Collections.sort(sortTopicList, comparator);
    	for(int i = 0; i < sortTopicList.size(); i++)
    	{
    		sortTopic.add(sortTopicList.get(i).getId());
    	}
    	
    }
    
	
	public List<Post> getSortTopicDocument(int topicId)//得到topic对应的已经排好序的document
	{
		List<Post> topicPost = new ArrayList<Post>();

		String topicDocumentSet = topicDocument.get(topicId);
		String[] documentSet = topicDocumentSet.split(",");
		for(int j = 0; j < documentSet.length; j ++)
		{
			topicPost.add(posts.get(Integer.parseInt(documentSet[j])));
		}

		return topicPost;
	}
	
	public void outPutTopicWordsDocument()
	{
		List<Post> postList;
		for(int i = 0 ;i < sortTopic.size(); i++)
		{
			int topicId = sortTopic.get(i);
			System.out.println("Toppic"+topicId+":"+topicWord.get(topicId));
			postList = this.getSortTopicDocument(i);
			for(int j = 0;j < postList.size(); j++)
			{
				Post p = postList.get(j);
				System.out.println("Title:"+p.getPost_title());
				System.out.println("Text:"+p.getPost_body_text());
				System.out.println("Code:"+p.getPost_body_code());
				System.out.println("Body:"+p.getPost_body());
				System.out.println("Tags:"+p.getPost_tag());
			}
			System.out.println("------------------");
		}
	}
}
