package cn.edu.fudan.se.NLP;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CorefCoreAnnotations.CorefChainAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.semgraph.SemanticGraph;
import edu.stanford.nlp.trees.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.util.CoreMap;




public class TestCoreNLP {
	
	private List<WordProperty> wordProperty = new ArrayList<WordProperty>();
	
	private List<Sentence> sentenceList = new ArrayList<Sentence>();
	
	private StanfordCoreNLP pipeline;
	
	private Properties props;
	public TestCoreNLP()
	{
		
	    props = new Properties();
	    props.put("annotators", "tokenize,ssplit,pos,lemma,ner,parse,dcoref");
	    pipeline = new StanfordCoreNLP(props);
		
	}
	
	
	
	public Clause QuerySentence(String text)
	{
		Clause clause = new Clause();
		wordProperty.clear();
		WordProperty wp;
		Annotation document = new Annotation(text);
		
		pipeline.annotate(document);
		
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		
		for(CoreMap sentence:sentences)
		{
			
		  Sentence sen = new Sentence();
		  sen.setSentence(sentence.toString());
		  sentenceList.add(sen);
		   wordProperty.clear();
			for(CoreLabel token:sentence.get(TokensAnnotation.class))
			{
				String word = token.get(TextAnnotation.class);
				String pos = token.get(PartOfSpeechAnnotation.class);
				String lemma = token.get(LemmaAnnotation.class); 
				
				System.out.println(word +"  " + pos);
				if(word.equals("-LRB-"))
					word = "(";
				if(word.equals("-RRB-"))
					word = ")";
				
				wp = new WordProperty(word,lemma,pos,true);
				wordProperty.add(wp);
			}
			clause.setClause(text);
			extractSentenceComponents(wordProperty,clause);
	
		}
		return clause;
		
		
	}
	public void CoreNLP(String text)
	{
		sentenceList.clear();
		wordProperty.clear();
		WordProperty wp;
		Annotation document = new Annotation(text);
		
		pipeline.annotate(document);
		
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		
		for(CoreMap sentence:sentences)
		{
			
		  Sentence sen = new Sentence();
		  sen.setSentence(sentence.toString());
		  sentenceList.add(sen);
		   wordProperty.clear();
			for(CoreLabel token:sentence.get(TokensAnnotation.class))
			{
				String word = token.get(TextAnnotation.class);
				String pos = token.get(PartOfSpeechAnnotation.class);
				String lemma = token.get(LemmaAnnotation.class); 
				
//				System.out.println(word +"  " + pos);
				if(word.equals("-LRB-"))
					word = "(";
				if(word.equals("-RRB-"))
					word = ")";
				
				wp = new WordProperty(word,lemma,pos,true);
				wordProperty.add(wp);
			}
			
//			System.out.println("Sentence:"+sentence);
			List<List<WordProperty>> splitList = splitClause(wordProperty);
			sen.setClause(splitList);
			List<List<WordProperty>> translateList = translateClause(splitList);
			List<List<WordProperty>> replacePronounList = replaceClausePronoun(translateList);
			extractSentenceComponents(replacePronounList,sen);
//			System.out.println("subject:" + getSubject(wordProperty) 
//					+ " verbGroup:" + getVerbGroup(wordProperty) + " object:" + getObject(wordProperty));
			
//			Tree tree = sentence.get(TreeAnnotation.class);
			
//			SemanticGraph dependencies = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
			
			
		}
		
//		Map<Integer,CorefChain> graph = document.get(CorefChainAnnotation.class);
		
	}
	
	public List<List<WordProperty>> splitClause(List<WordProperty> wpList)
	{
		List<List<WordProperty>> splitList = new ArrayList<>();
		List<WordProperty> tempList;
		int num = wpList.size();
		WordProperty wp,ccWp = null;
		tempList = new ArrayList<>();
		for(int i = 0; i < num; i++)
		{
			
			wp = wpList.get(i);
			if(!wp.getProperty().equals("CC")&&!wp.getWord().equalsIgnoreCase("because")&&
					!wp.getProperty().equals(",")&&!wp.getProperty().equals(":")&&!wp.getWord().equalsIgnoreCase("When")
					&&!wp.getWord().equalsIgnoreCase("that")&&!wp.getWord().equalsIgnoreCase("which")&&
					!wp.getWord().equalsIgnoreCase("where"))
			{
				tempList.add(wp);
			}else
			{
//				ccWp =wp;
//				System.out.println("judge:"+judgeClause(tempList));
//				System.out.println("子句：");
//				for(int k = 0 ; k < tempList.size(); k++)
//				{
//					System.out.print(tempList.get(k).getWord() +" ");
//				}
//				System.out.println(judgeClause(tempList));
//				if(tempList.size()==1)//(, and)导致tempList只用一个and
//					continue;
				
				if(judgeClause(tempList))//判断是否是字句
				{
					splitList.add(coypList(tempList));
				}else
				{
					int tempSize = splitList.size();
					if(tempList.size() != 0)
					{
						if(tempSize == 0)
						{
							splitList.add(coypList(tempList));
						}else
						{
							List<WordProperty> previousList = splitList.get(tempSize-1);
							previousList.addAll(tempList);
						}
					}
				}
				
				tempList.clear();
				tempList.add(wp);
			}	
		
			
		}
		if(tempList.size()!=0)
		{
//			System.out.println("子句：");
//			for(int k = 0 ; k < tempList.size(); k++)
//			{
//				System.out.print(tempList.get(k).getWord() +" ");
//			}
//			System.out.println(judgeClause(tempList));
			if(judgeClause(tempList))//判断是否是字句
			{
				splitList.add(coypList(tempList));
			}else
			{
				int tempSize = splitList.size();
				if(tempSize == 0)
				{
					splitList.add(coypList(tempList));
				}else
				{
					List<WordProperty> previousList = splitList.get(tempSize-1);
//					previousList.add(ccWp);
					previousList.addAll(tempList);
				}
			}
			
		}
		
	
		return splitList;
	}
	
	public List<List<WordProperty>> translateClause(List<List<WordProperty>> splitList)//将疑问句转成陈述句
	{
//		for(int i = 0; i < splitList.size(); i++)
//		{
//			List<WordProperty> wpList = splitList.get(i);
//			for(WordProperty wp: wpList)
//			{
//				System.out.println("split:"+wp.getWord());
//			}
//			System.out.println("----------------------");
//		}
		List<List<WordProperty>> translateList = new ArrayList<>();
		for(int k = 0; k < splitList.size(); k++)
		{
			List<WordProperty> wpList = splitList.get(k);
			int num = wpList.size();
			WordProperty wp1,wp2;
			boolean iS =false;
			if(num >=3)
			{
				wp1 = wpList.get(0);
				if(wp1.getProperty().equals(",")||wp1.getProperty().equals("CC")||wp1.getProperty().equals(":"))//子句开头有逗号等
				{
					wp1 = wpList.get(1);
					wp2 = wpList.get(2);
					iS = true;
				}else
				{
					wp2 = wpList.get(1);
				}
				if(wp1.getProperty().equals("WRB")&&(wp2.getProperty().equals("VBP")||wp2.getProperty().equals("VBZ")))//What do,What does,How to
				{
					if(iS)
					{
						wpList.remove(1);
						wpList.remove(1);
					}else
					{
						wpList.remove(0);
						wpList.remove(0);
					}
					translateList.add(wpList);
			
				}
//				else if((wp1.getProperty().equals("VBP")||wp2.getProperty().equals("VBZ"))&&
//						(wp2.getProperty().contains("NN")||wp2.getProperty().equals("PRP")))//when doing
//				{
//					wpList.remove(0);
//					translateList.add(wpList);
//				}
				else if(wp1.getProperty().equals("MD")&&wp2.getProperty().equals("PRP"))//Can you
				{
					if(iS)
					{
						wpList.remove(1);
						wpList.add(2, wp1);
					}else
					{
						wpList.remove(0);
						wpList.add(1, wp1);
					}
					
					translateList.add(wpList);
				}else if((wp1.getProperty().equals("VBZ")||wp1.getProperty().equals("VBP"))&&(wp2.getProperty().contains("NN")
						||wp2.getProperty().equals("DT")||wp2.getProperty().equals("PRP")))//Do you,Do the
				{
					if(iS)
					{
						wpList.remove(1);
					}else
						wpList.remove(0);
					translateList.add(wpList);
				}
				else
					translateList.add(wpList);
			}else
				translateList.add(wpList);
		}
		
		
		return translateList;
		
	}
	
	public List<List<WordProperty>> replaceClausePronoun(List<List<WordProperty>> translateList)//将疑问句转成陈述句
	{
		List<List<WordProperty>> replaceList = new ArrayList<>();
		int size = translateList.size();
		if(size == 1)
		{
			replaceList.addAll(translateList);
			
			return replaceList;
		}
		List<WordProperty> nounList = new ArrayList<>();
		List<WordProperty> wpList = translateList.get(0);
		replaceList.add( wpList);
		for(WordProperty wp:wpList)
		{
			if(wp.getProperty().contains("NN")&&!nounList.contains(wp))
				nounList.add(wp);
		}
		
//		int sizeee = translateList.size();
//		System.out.println("sizeee:"+sizeee);
//		for(int i = 0; i < sizeee; i++)
//		{
//			List<WordProperty> l = translateList.get(i);
//			for(WordProperty p: l)
//			{
//				System.out.println("trancejqt:"+p.getWord());
//			}
//			System.out.println("----------------------");
//		}
//		
		for(int k = 1; k < size; k++)
		{
			wpList = translateList.get(k);
			int num = wpList.size();
			WordProperty wp,wp1;
			for(int i = 0; i < num; i++)
			{
				wp = wpList.get(i);
				if((wp.getWord().equalsIgnoreCase("it")||wp.getWord().equalsIgnoreCase("which")||
						wp.getWord().equalsIgnoreCase("both")||wp.getWord().equalsIgnoreCase("that"))&&
						(nounList.size()!=0&&!wp.getProperty().equals("IN")))
				{
					wpList.remove(i);
					int nounSize = nounList.size();
					for(int n = nounSize - 1 ; n >= 0 ;n--)
					{
						wp1 = nounList.get(n);
						wpList.add(i,wp1);
					}
				}
			}
			replaceList.add(wpList);
			for(WordProperty tempWp:wpList)
			{
				if(tempWp.getProperty().contains("NN")&&!nounList.contains(tempWp))
					nounList.add(tempWp);
			}
			
		}

	
		
		return replaceList;
		
	}
	
	public void extractSentenceComponents(List<WordProperty> wpList,Clause clause)
	{
			Subject subject = new Subject();
			Predicate predicate = new Predicate();
			Object object = new Object();
			WordProperty wp;
			int num = wpList.size();
			boolean isSubject = true,isVerb = false,isObject = false,iS = true;;
			int index = 1000;
			
			for(int i = 0; i < num ; i++)
			{
				 wp = wpList.get(i);
		         if(iS&&(wp.getProperty().equals("VV")||wp.getProperty().equals("VBD")
		            		||wp.getProperty().equals("VBN")||wp.getProperty().equals("VBP")
		            		||wp.getProperty().equals("VBZ")||wp.getProperty().equals("VB"))
		            		||wp.getProperty().equals("VE")||wp.getProperty().equals("VC"))
		         {
		        	 int  tempIndex  = verbPhrase(i,wpList);
		        	
		        	 if(wp.getWord().equals("'ve")||wp.getWord().equalsIgnoreCase("has")
			        		 ||wp.getWord().equalsIgnoreCase("have"))
			         {
			            index = haExpress(i,wpList);
			         }else
			       	 {
			        	 index = toInfinitive(i,wpList);
			       	 } 
		        	 if(tempIndex > index)
		        		 index = tempIndex;
		        	 isVerb = true;
		        	 isSubject = false;
		        	 isObject = false;
		         }
		         
		         if(iS&&wp.getProperty().equals("MD"))
		         {
		        	 index = modal(i,wpList); 
//		        	 System.out.println("index Modal:"+index);
		        	 isVerb = true;
		        	 isSubject = false;
		        	 isObject = false;
		         }
		    
		         if(i > index)
		         {
		        	 isObject = true;
		        	 isSubject = false;
		        	 isVerb = false;
		         }
		         if(isSubject)//get subject
		         {
		        	 subject.addSubject(wp);
		         }
		         if(i <= index&&isVerb)//get verb
		         {
//		        	 System.out.println(i+"|"+index);
		        	 for(int j = i; j <= index ; j++)
		        	 {
		        		 wp = wpList.get(j);
		        		 predicate.addPredicate(wp);	
		        	 }
		        	 i = index;
		         
		         }
		         if(isObject)//get object
		         {
		        	object.addObject(wp);
		        	 iS = false;
		        	 
		         }
		        
			}
			predicate.init();
			clause.setPredicate(predicate);
			subject.init();
			clause.setSubject(subject);
			object.init();
			clause.setObject(object);
			clause.init();
//			System.out.println(subject.toString() +"|"+predicate.toString()+"|"+object.toString());

		
	}
	
	public void extractSentenceComponents(List<List<WordProperty>> listList,Sentence sen)
	{
		
		
		for(int k = 0; k < listList.size(); k++)
		{
			Clause clause = sen.getClauseList().get(k);
			Subject subject = new Subject();
			Predicate predicate = new Predicate();
			Object object = new Object();
			List<WordProperty> wpList = listList.get(k);
			WordProperty wp;
			int num = wpList.size();
			boolean isSubject = true,isVerb = false,isObject = false,iS = true;;
			int index = 1000;
			
			for(int i = 0; i < num ; i++)
			{
				 wp = wpList.get(i);
		         if(iS&&(wp.getProperty().equals("VV")||wp.getProperty().equals("VBD")
		            		||wp.getProperty().equals("VBN")||wp.getProperty().equals("VBP")
		            		||wp.getProperty().equals("VBZ")||wp.getProperty().equals("VB"))
		            		||wp.getProperty().equals("VE")||wp.getProperty().equals("VC"))
		         {
		        	 int  tempIndex  = verbPhrase(i,wpList);
		        	
		        	 if(wp.getWord().equals("'ve")||wp.getWord().equalsIgnoreCase("has")
			        		 ||wp.getWord().equalsIgnoreCase("have"))
			         {
			            index = haExpress(i,wpList);
			         }else
			       	 {
			        	 index = toInfinitive(i,wpList);
			       	 } 
		        	 if(tempIndex > index)
		        		 index = tempIndex;
		        	 isVerb = true;
		        	 isSubject = false;
		        	 isObject = false;
		         }
		         
		         if(iS&&wp.getProperty().equals("MD"))
		         {
		        	 index = modal(i,wpList); 
//		        	 System.out.println("index Modal:"+index);
		        	 isVerb = true;
		        	 isSubject = false;
		        	 isObject = false;
		         }
		    
		         if(i > index)
		         {
		        	 isObject = true;
		        	 isSubject = false;
		        	 isVerb = false;
		         }
		         if(isSubject)//get subject
		         {
		        	 subject.addSubject(wp);
		         }
		         if(i <= index&&isVerb)//get verb
		         {
//		        	 System.out.println(i+"|"+index);
		        	 for(int j = i; j <= index ; j++)
		        	 {
		        		 wp = wpList.get(j);
		        		 predicate.addPredicate(wp);	
		        	 }
		        	 i = index;
		         
		         }
		         if(isObject)//get object
		         {
		        	object.addObject(wp);
		        	 iS = false;
		        	 
		         }
		        
			}
			predicate.init();
			clause.setPredicate(predicate);
			subject.init();
			clause.setSubject(subject);
			object.init();
			clause.setObject(object);
			clause.init();
//			System.out.println(subject.toString() +"|"+predicate.toString()+"|"+object.toString());
		}
		
	}
	


	
	private int modal(int index,List<WordProperty> wpList)
	{
		int num = wpList.size();
		int pointer = 0;
		int num1 = index;
		WordProperty wp;
		for(int i = index+1; i < num ; i++ )
		{
			wp = wpList.get(i);
			pointer ++;
			if(wp.getProperty().equals("VB"))
				num1 = i;
			if(pointer >= 4&&num1 == index)
				return index;
		}
		return num1;
	}
	
	private int haExpress(int index,List<WordProperty> wpList)
	{
		int num = wpList.size();
		int pointer = 0;
		WordProperty wp;
		for(int i = index+1; i < num ; i++ )
		{
			wp = wpList.get(i);
			pointer ++;
			if(wp.getProperty().equals("VBN"))
				return i;
			if(wp.getProperty().equals("TO"))
				return i+1;
			if(pointer >= 3)
				return index;
		}
		return index;
	}
	
	private int verbPhrase(int index, List<WordProperty> wpList)
	{
		int num = wpList.size();
		int pointer = 0;
		WordProperty wp;
		for(int i = index+1; i < num ; i++)
		{
			wp = wpList.get(i);
			pointer ++;
			if(wp.getProperty().equals("IN"))
				return i;
			if(wp.getProperty().equals("RB"))
				return i;
			if(wp.getProperty().equals("CC"))
				return i;
			if(pointer >= 1)
				return index;
				
		}
		return index;
	}
	private int toInfinitive(int index, List<WordProperty> wpList)
	{
		int num = wpList.size();
		int pointer = 0;
		WordProperty wp;
		for(int i = index+1; i < num ; i++)
		{
			wp = wpList.get(i);
			pointer ++;
			if(wp.getProperty().equals("TO"))
				return i;
			if(pointer >= 5)
				return index;
				
		}
		return index;
	}
	
	public List<WordProperty> coypList(List<WordProperty> wpList)
	{
		List<WordProperty> copyList = new ArrayList<>();
		for(WordProperty wp:wpList)
		{
			copyList.add(wp);
		}
		return copyList;
	}
	

	
	public boolean judgeClause(List<WordProperty> wpList)
	{
		List<WordProperty> subjectList = new ArrayList<>();
		boolean iS = true;
		for(WordProperty wp:wpList)
		{
			 subjectList.add(wp);
			if(iS&&(wp.getProperty().equals("VBZ")||wp.getProperty().equals("VBP")||wp.getProperty().equals("CC")))//子句是疑问句
			{
				iS = false;
				continue;
			}
			iS = false;
			  if(wp.getProperty().equals("VV")||wp.getProperty().equals("VC")||wp.getProperty().equals("VE")||
					  wp.getProperty().equals("VBD")||wp.getProperty().equals("VBN")||wp.getProperty().equals("VBP")
	            		||wp.getProperty().equals("VBZ")||wp.getProperty().equals("VB"))
			  {
				  for(WordProperty tempWp:subjectList)
				  {
					  if(tempWp.getProperty().contains("NN")||tempWp.getProperty().equals("PRP")||
								tempWp.getProperty().equals("VBG")||tempWp.getProperty().equals("WDT")
								||tempWp.getProperty().equals("PRP")||tempWp.getProperty().equals("EX")
								||tempWp.getProperty().equals("FW")||tempWp.getProperty().equals("DT"))
					  {
						  return true;
					  }
				  }
				 
			  }
		}
		return false;
	
	}
	
	public List<Sentence> getSentence()
	{
		return sentenceList;
	}
	
	public void showSentenceComponent()
	{
		for(Sentence sen:sentenceList)
		{
			System.out.println("Sentence:"+sen.getSentence());
			List<Clause> clauseList = sen.getClauseList();
			for(Clause clause:clauseList)
			{
				System.out.println("Clause:"+clause.getClause());
				Subject subject = clause.getSubject();
				Predicate predicate = clause.getPredicate();
				Object object = clause.getObject();
				
				System.out.println("Subject["+subject.toString()+"]" +" Predicate["+predicate.toString()+"]"+
						" Object["+object.toString()+"]");
				System.out.println("-------");
				System.out.println("Subject["+subject.getSubject()+"]" +"S-E["+subject.getEnvironment()+"]"+" Predicate["+predicate.getPredicate()+"-"+predicate.getSynonyms()+"]"+ " P-E["+predicate.getEnvironment()+"]"
				+" Object["+object.getObject()+"]" + "O-E["+object.getEnvironment()+"]"+" Authority["+clause.getAuthority()+"]");
			}
		}
	}
	
	public static void main(String args[])
	{
//		String text = "How to configure postgresql for the first time?";	
//		String text = "Which key value store is the most promising/stable?";		
//		String text = "I have Qt5.4mingwRC1 on windows and it works without any problem but there is an issue with QML files and the designer.";	
//		String text = "How do you add a column, with a default value, to an existing table in SQL Server 2000/SQL Server 2005?";	
//      String text = "I like this answer a little better than dbugger's because I make the default constraint";
//		String text = "On all qml file (even with example projects), "
//				+ "i have an error message “Using Qt Quick code model instead of Qt Quick2 (M324) (4:1)” and i can’t use the design editor.";
//		String text = "I  can't make and write a decision that is important.";
//		String text = "Before Qt5.4 I was using Qt5.3 and there was no such that error.";
//		String text = "You will need to set the QML_IMPORT_PATH to the proper installation.";
//		String text = "Issue with qml files and designer tab of QtCreator of Qt5.4mingwRC1";
//		String text = "When performing an FFT on a signal, does information about the relevant pass-band allow the algorithm to be more efficient?";
		
//		String text = "When data is stored on disk based storage devices, "
//				+ "it is stored as blocks of data. "
//				+ "These blocks are accessed in their entirety,"
//				+ " making them the atomic disk access operation. "
//				+ "Disk blocks are structured in much the same way as linked lists; "
//				+ "both contain a section for data, "
//				+ "a pointer to the location of the next node (or block),"
//				+ " and both need not be stored contiguously.";
		
//		String text = "Deprecation isn't the magic bullet everyone seems to think it is. "
//				+ "PHP itself will not be there one day, yet we rely on the tools we have at our disposal today."
//				+ " When we have to change tools, we will."
//				+ " –  Lightness Races in Orbit Dec 24 '12 at 14:29 ";
//		String text = "And the later PHP developer team has taken the decision to generate E_DEPRECATED errors when users connect to MySQL, "
//				+ "whether through mysql_connect(), mysql_pconnect() or the implicit connection functionality built into ext/mysql.";
//		String text = "I have a room where is tool.";
//		String text = "Moving away from ext/mysql is not only about security,"
//				+ " but also about having access to all the features of the MySQL database.";
//		String text = "You can also pass in several driver options as an array to the fourth parameter. "
//				+ "I recommend passing the parameter which puts PDO into exception mode. "
//				+ "Because some PDO drivers don't support native prepared statements, so PDO performs emulation of the prepare. "
//				+ "It also lets you manually enable this emulation. "
//				+ "To use the native server-side prepared statements, you should explicitly set it ";
		
//		String text = "What is the simplest way to present a database for a set of records in C#?";
        String text = " How to setup a connection timeout depending of the user login in MySQL";


		TestCoreNLP tcNLP = new TestCoreNLP();
		tcNLP.CoreNLP(text);
		System.out.println("------------------------");
		tcNLP.showSentenceComponent();
		
//		System.out.println("Sentence:"+sentence);
//		System.out.println("subject:" + tcNLP.getSubject(sentence) 
//				+ " verbGroup:" + tcNLP.getVerbGroup(sentence) + " object:" + tcNLP.getObject(sentence)
//				+ " environment:" + tcNLP.getEnvironment(sentence));
			
	}
}
