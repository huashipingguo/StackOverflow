package cn.edu.fudan.se.datasource;

import cn.edu.fudan.se.bean.SortDataType;

//import cn.edu.fudan.se.bean.TokenInFile;
import cn.edu.fudan.se.bean.Word;

//import cn.edu.fudan.se.bean.ast.CLass;
import cn.edu.fudan.se.bean.lda.Document;
import cn.edu.fudan.se.bean.lda.Topic;
import cn.edu.fudan.se.bean.lda.TopicToDocument;
import cn.edu.fudan.se.bean.lda.TopicToWord;
import cn.edu.fudan.se.bean.lda.WordToDocument;

//import cn.edu.fudan.se.dal.ASTDAL;
import cn.edu.fudan.se.dal.DBHelper;
import cn.edu.fudan.se.dal.LDADAL;
import cn.edu.fudan.se.lda.LDA;
import cn.edu.fudan.se.preprocessing.TokenExtractor;

//import cn.edu.fudan.se.service.LDAService;
//import cn.edu.fudan.se.service.StemService;
//import cn.edu.fudan.se.util.ASTUtil;
import cn.edu.fudan.se.util.CommUtil;
import cn.edu.fudan.se.util.FileHelper;
import cn.edu.fudan.se.util.Global;
import cn.edu.fudan.se.util.INIHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.stackoverflow.bean.Post;


//import cn.edu.fudan.se.util.StemUtil;
public class LDAOperator //implements LDAService
 {
    private static String argStr;
    private static LDAOperator instance;
    private static final String sectionName = "LDA";
    private static String tempFileName;
    private static int currentfilenumber = 0;
    List<Post> postList = null;
    String directory = CommUtil.getCurrentProjectPath();
    String projectPath = null;
    float[][] document_topic = null;
    private final int DocumentNumber = 10; //设置相关文档数
    private int ndocs = 1;
    private int ntopics = 1;
    private int ntopWordscount = -1;
    private int nwords = -1;
    private final String Topic_Keyword = "Topic ";
    float[][] topic_word = null;
    List<Topic> topicList;
    Map<Integer, String> wordMapHashMap = new HashMap<Integer, String>();
    List<WordToDocument> wordToDocument = new ArrayList<WordToDocument>();
    List<Word> words = new ArrayList<Word>();

    public int getCurrentfilenumber() {
        return currentfilenumber;
    }

    public static void setCurrentfilenumber(int currentfilenumber) {
        LDAOperator.currentfilenumber = currentfilenumber;
    }

    public static LDAOperator getInstance() {
        if (instance == null) {
            instance = new LDAOperator();
        }

        return instance;
    }
    
    public float[][] getTopicWord()
    {
    	return topic_word;
    }
    
    public float[][] getDocumentTopic()
    {
    	return document_topic;
    }

    public void analysisFiles() {
        analysisOthers();
        analysisWordMap();
        analysisTheta();
        analysisTwords();
        analysisPhi();
    }

    private void analysisOthers() {
        String[] mapArray = FileHelper.getContentArray(CommUtil.getCurrentProjectPath() +
                "\\LDAResult\\model-final.others");
        HashMap<String, String> othersHashMap = new HashMap<String, String>();

        for (int i = 0; i < mapArray.length; i++) {
            String[] tempString = mapArray[i].split("=");

            tempString[0].toString().toLowerCase().trim();
            tempString[1].toString().toLowerCase().trim();
            othersHashMap.put(tempString[0], tempString[1]);
        }

        ntopics = Integer.parseInt(othersHashMap.get("ntopics"));
        ndocs = Integer.parseInt(othersHashMap.get("ndocs"));
        nwords = Integer.parseInt(othersHashMap.get("nwords"));
    }

    private void analysisPhi() {
        String[] mapArray = FileHelper.getContentArray(CommUtil.getCurrentProjectPath() +
                "\\LDAResult\\model-final.phi");
        topic_word = new float[ntopics][nwords];

        for (int i = 0; i < ntopics; i++) {
            String[] values = mapArray[i].split("[ ]");

            for (int j = 0; j < nwords; j++) {
                topic_word[i][j] = Float.parseFloat(values[j]);
            }
        }
    }

    private void analysisTheta() {
        String[] mapArray = FileHelper.getContentArray(CommUtil.getCurrentProjectPath() +
                "\\LDAResult\\model-final.theta");
        document_topic = new float[ndocs][ntopics];

        for (int i = 0; i < ndocs; i++) {
            String[] values = mapArray[i].split("[ ]");

            for (int j = 0; j < ntopics; j++) {
                document_topic[i][j] = Float.parseFloat(values[j]);
            }
        }
    }

    public void analysisTwords() {
        topicList = new ArrayList<Topic>();

        String[] mapArray = FileHelper.getContentArray(CommUtil.getCurrentProjectPath() +
                "\\LDAResult\\model-final.twords");

        String[] tempString;
        Topic newTopic = null;

        for (int i = 0; i < mapArray.length; i++) {
            if (mapArray[i].contains(Topic_Keyword)) {
                newTopic = new Topic();
                tempString = mapArray[i].split(" ");
                tempString[1] = tempString[1].replace("th:", " ").trim();
                newTopic.setIndex(Integer.parseInt(tempString[1]));

                topicList.add(newTopic);
            } else {
                if (mapArray[i].trim().split(" ").length == 1) {
                    continue;
                }

                String word = mapArray[i].trim().split(" ")[0];

                if ((word.trim().length() > 0) && (newTopic != null)) {
                    newTopic.addTopWord(word);
                }
            }
        }
    }

    public List<Topic> getTopicList() {
        return topicList;
    }

    private void analysisWordMap() {
        String[] mapArray = FileHelper.getContentArray(CommUtil.getCurrentProjectPath() +
                "\\LDAResult\\wordmap.txt");

        for (int i = 1; i < mapArray.length; i++) {
            String[] tempString = mapArray[i].split(" ");

            tempString[0] = tempString[0].toString().toLowerCase().trim();
            tempString[1] = tempString[1].toString().toLowerCase().trim();

            if (tempString[1].length() == 0) {
                tempString[1] = tempString[0];
                tempString[0] = " ";
            }

            wordMapHashMap.put(Integer.parseInt(tempString[1]), tempString[0]);
        }
    }

    public void doLDAAnalysis(List<Post> postSet) {
        this.postList = postSet;
        //		this.projectPath=projectPath;
        this.currentfilenumber = postSet.size();
        this.setup();
        this.tokenExtract(postSet);
        LDA.grandLDAmain(argStr);
        analysisFiles();
        SaveDB();
    }

    private void generateTopicList() {
        topicList = new ArrayList<Topic>();

        for (int topicID = 0; topicID < ntopics; topicID++) {
            List<TopicToWord> topicWords = LDADAL.getTopWordsByTopic(topicID,
                    ntopWordscount);
            Topic topic = new Topic();
            topic.setIndex(topicID);

            for (int i = 0; i < topicWords.size(); i++) {
                topic.getTopWords()
                     .add(getWordNameByID(topicWords.get(i).getWordID()));
            }

            List<String> docs = getTopicRelatedDocuments(topicID);
            topic.setRelatedDocs(docs);
            topicList.add(topic);
        }
    }

    public List<Topic> getRelatedTopic(String query) {
        String[] keywords = query.split("[ ]");
        SortDataType<Topic> sortedTopic = new SortDataType<Topic>();

        for (String keyword : keywords) {
            for (Topic ldaTopicDataType : topicList) {
                if (ldaTopicDataType.getTopWords().contains(keyword)) {
                    sortedTopic.addContent(ldaTopicDataType);
                }
            }
        }

        sortedTopic.sort();

        return sortedTopic.getContentList();
    }

    public List<String> getTopicRelatedDocuments(int topicIndex) {
        float[] documentArray = new float[ndocs];
        List<String> result = new ArrayList<String>();

        for (int i = 0; i < ndocs; i++) {
            documentArray[i] = document_topic[i][topicIndex];
        }

        try {
            for (int i = 0; i < DocumentNumber; i++) {
                float maxValue = documentArray[0];
                int indexOfMaxValue = 0;

                for (int j = 1; j < ndocs; j++) {
                    if (documentArray[j] > maxValue) {
                        maxValue = documentArray[j];
                        indexOfMaxValue = j;
                    }
                }

                result.add(postList.get(indexOfMaxValue).postId + "");
                documentArray[indexOfMaxValue] = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();

            // TODO: handle exception
        }

        return result;
    }

    //	public List<String> getTopicTopAllDocuments(int topicIndex)
    public List<String> getTopicTopAllDocumentsAndSort(int topicIndex) {
        float[] documentArray = new float[ndocs];
        List<String> result = new ArrayList<String>();

        for (int i = 0; i < ndocs; i++) {
            documentArray[i] = document_topic[i][topicIndex];
        }

        for (int i = 0; i < ndocs; i++) {
            float maxValue = documentArray[0];
            int indexOfMaxValue = 0;

            for (int j = 1; j < ndocs; j++) {
                if (documentArray[j] > maxValue) {
                    maxValue = documentArray[j];
                    indexOfMaxValue = j;
                }
            }

            result.add(postList.get(indexOfMaxValue).postId + "");
            documentArray[indexOfMaxValue] = 0;
        }

        return result;
    }

    private String getWordNameByID(int wordID) {
        return wordMapHashMap.get(wordID);
    }

    private void insertDocuments() {
        List<Document> documents = new ArrayList<Document>();

        for (int i = 0; i < postList.size(); i++) {
            Document newDocument = new Document();
            newDocument.setId(i);
//            newDocument.setName(postList.get(i).getId() + "");
            newDocument.setName(postList.get(i).postId + "");
            documents.add(newDocument);
        }

        LDADAL.insertDocuments(documents);
    }

    private void insertTopicToDocuments() {
        List<TopicToDocument> topicToDocuments = new ArrayList<TopicToDocument>();

        for (int i = 0; i < document_topic.length; i++)
            for (int j = 0; j < document_topic[i].length; j++) {
                TopicToDocument newTopicToDocument = new TopicToDocument();
                newTopicToDocument.setDocumentID(i);
                newTopicToDocument.setTopicID(j);
                newTopicToDocument.setDocumentDistributionValue(document_topic[i][j]);
                topicToDocuments.add(newTopicToDocument);
            }

        LDADAL.insertTopicToDocument(topicToDocuments);
    }

    private void insertTopicToWords() {
        List<TopicToWord> topicToWords = new ArrayList<TopicToWord>();

        for (int i = 0; i < topic_word.length; i++)
            for (int j = 0; j < topic_word[i].length; j++) {
                TopicToWord newTopicToWord = new TopicToWord();
                newTopicToWord.setTopicID(i);
                newTopicToWord.setWordID(j);
                newTopicToWord.setWordDistributionValue(topic_word[i][j]);
                topicToWords.add(newTopicToWord);
            }

        LDADAL.inserTopicToWord(topicToWords);
    }

    private void insertWordsToDocument() {
//        List<WordToDocument> wordToDocument = new ArrayList<WordToDocument>();
        double value;
        for (int i = 0; i < document_topic.length; i++) {
            for (int j = 0; j < document_topic[i].length; j++) {
                for (int k = 0; k < topic_word[j].length; k++) {
                   value = document_topic[i][j] * topic_word[j][k];
                   if(value >= 0.03)
                   {
                	   WordToDocument newWordToDocument = new WordToDocument();
                       newWordToDocument.setDocumentID(i);
                       newWordToDocument.setTopicID(j);
                       newWordToDocument.setWordID(k);
                       newWordToDocument.setWordIdString(k+"");
                       newWordToDocument.setDocumentDistributionValue(document_topic[i][j] * topic_word[j][k]);
                       wordToDocument.add(newWordToDocument);
                   } 
                }
            }
        }

        LDADAL.inserWordToDocument(wordToDocument);
    }

    private void insertWords() {
//        List<Word> words = new ArrayList<Word>();

        for (int i = 0; i < wordMapHashMap.size(); i++) {
            Word newWord = new Word();
            newWord.setId(i);
            newWord.setName(wordMapHashMap.get(i));
            words.add(newWord);
        }

        LDADAL.insertWords(words);
    }
    
    public List<Word> getWordList()
    {
         return words;

    }
    
    public List<WordToDocument> getWordsToDocument() {

       return wordToDocument;
    }


    //	public void loadDataFromDB()
    //	{
    //		DBHelper.setCurrentProjectName(projectName);
    //		DBHelper.openConn();
    //		loadDocuments();
    //		loadWords();
    //		loadOther();
    //		loadTopicToDocuments();
    //		loadTopicToWords();
    //		generateTopicList();
    //		DBHelper.closeConn();
    //
    //	}

    //	private void loadDocuments()
    //	{
    //		List<Document> documents = LDADAL.getDocuments();
    //		fileList = new ArrayList<String>();
    //		for (int i = 0; i < documents.size(); i++)
    //		{
    //			fileList.set(documents.get(i).getId(), documents.get(i).getName());
    //
    //		}
    //
    //	}

    //	private void loadOther()
    //	{
    //		nwords = wordMapHashMap.size();
    //		ndocs = fileList.size();
    //		ntopics = LDADAL.getTopicCount();
    //
    //	}

    //	private void loadTopicToDocuments()
    //	{
    //		List<TopicToDocument> topicToDocuments = LDADAL.getTopicToDocument();
    //		document_topic = new float[ndocs][ntopics];
    //		for (int i = 0; i < topicToDocuments.size(); i++)
    //		{
    //			TopicToDocument newTopicToDocument = topicToDocuments.get(i);
    //			document_topic[newTopicToDocument.getDocumentID()][newTopicToDocument
    //					.getTopicID()] = newTopicToDocument
    //					.getDocumentDistributionValue();
    //
    //		}
    //
    //	}

    //	private void loadTopicToWords()
    //	{
    //		List<TopicToWord> topicToWords = LDADAL.getTopicToWord();
    //		topic_word = new float[ntopics][nwords];
    //		for (int i = 0; i < topicToWords.size(); i++)
    //		{
    //			TopicToWord newTopicToWord = topicToWords.get(i);
    //			topic_word[newTopicToWord.getTopicID()][newTopicToWord.getWordID()] = newTopicToWord
    //					.getWordDistributionValue();
    //		}
    //
    //	}

    //	private void loadWords()
    //	{
    //		List<Word> words = LDADAL.getWords();
    //		wordMapHashMap.clear();
    //		for (int i = 0; i < words.size(); i++)
    //		{
    //			wordMapHashMap.put(words.get(i).getId(), words.get(i).getName());
    //		}
    //
    //	}
    private void SaveDB() {
        DBHelper.setCurrentProjectName(Global.projectName);
        DBHelper.openConn();
        LDADAL.clearRelatedData();
        insertDocuments();
        insertWords();
        insertTopicToDocuments();
        insertTopicToWords();
//        insertWordsToDocument();
        generateTopicList();
        DBHelper.closeConn();
    }

    public void setup() {
        INIHelper iniHelper = new INIHelper(CommUtil.getCurrentProjectPath() +
                "\\conf.ini");

        //		INIHelper iniHelper = new INIHelper(CommUtil.getCurrentProjectPath());
        String alphaValue = iniHelper.getValue(sectionName, "alpha", "0.5");
        String betaValue = iniHelper.getValue(sectionName, "beta", "0.1");
        String ntopicsValue = iniHelper.getValue(sectionName, "ntopics", "100");
        String nitersValue = iniHelper.getValue(sectionName, "niters", "100");
        String savestepValue = iniHelper.getValue(sectionName, "savestep", "100");
        ntopWordscount = Integer.parseInt(iniHelper.getValue(sectionName,
                    "twords", "20"));
        argStr = "-est -alpha " + alphaValue + " -beta " + betaValue +
            " -dir " + CommUtil.getCurrentProjectPath() + "\\LDAResult" +
            " -ntopics " + ntopicsValue + " -niters " + nitersValue +
            " -savestep " + savestepValue + " -twords " + ntopWordscount;
    }

    private void tokenExtract(List<Post> postSet) {
        TokenExtractor tokenExtractor = new TokenExtractor();
        INIHelper iniHelper = new INIHelper(CommUtil.getCurrentProjectPath() +
                "\\conf.ini");

        tempFileName = iniHelper.getValue("IDENTIFIEREXTRACTOR",
                "wordsMapFileName", "temp.txt");
        tokenExtractor.analysis(postSet);
        tokenExtractor.writeToFile(CommUtil.getCurrentProjectPath() +
            "\\LDAResult\\" + tempFileName);
        argStr = argStr + " -dfile " + tempFileName;
    }
}
