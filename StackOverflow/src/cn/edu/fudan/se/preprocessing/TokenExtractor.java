package cn.edu.fudan.se.preprocessing;


//import org.eclipse.jdt.internal.core.dom.rewrite.TokenScanner;
import cn.edu.fudan.se.bean.TokenInFile;

//import cn.edu.fudan.se.bean.ast.Class;
import cn.edu.fudan.se.bean.ast.Method;
import cn.edu.fudan.se.dal.DBHelper;
import cn.edu.fudan.se.filter.FilterStopWord;
import cn.edu.fudan.se.filter.Stemmer;
//import cn.edu.fudan.se.dal.TokenDAL;
//import cn.edu.fudan.se.util.ASTUtil;
import cn.edu.fudan.se.util.CommUtil;
import cn.edu.fudan.se.util.FileHelper;
import cn.edu.fudan.se.util.Global;
import cn.edu.fudan.se.util.INIHelper;
import cn.edu.fudan.se.util.MicroLog;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.stackoverflow.bean.Post;


//import cn.edu.fudan.se.util.WordNetDict;
public class TokenExtractor {
    List<TokenInFile> tokenList;
    String projectName;
    private boolean isAcceptReduplication = false;
    boolean isAcceptAlphabet = true;
    boolean isAcceptDigit = false;

    public void analysis(List<Post> postSet, String projectName) {
    	Stemmer stem = new Stemmer();
    	FilterStopWord fsw = new FilterStopWord();
        tokenList = new ArrayList<TokenInFile>();

        List<String> fileMapList = new ArrayList<String>();
        INIHelper iniHelper = new INIHelper(CommUtil.getCurrentProjectPath() +
                "\\conf.ini");
        String fileMapName = iniHelper.getValue("IDENTIFIEREXTRACTOR",
                "fileMapName", "");
        fileMapName = CommUtil.getCurrentProjectPath() + "\\" + fileMapName;

        TokenExtractor tokenExtractor = new TokenExtractor();
        tokenExtractor.isAcceptAlphabet(true);
        tokenExtractor.isAcceptDigit(false);
        String stringAfterStemmStopWord = "";

        for (Post post : postSet) //从文件中读取数据，
         {
            TokenInFile newTokens = new TokenInFile();
//            newTokens.setFileName(post.getId() + ""); //类的编号
            newTokens.setFileName(post.postId + ""); //类的编号
            newTokens.setProjectName(projectName); //项目名
//            System.out.println("ok:;;;"+stem.getStringAfterStemming(post.getPost_body()));
            newTokens.setTokens(tokenExtractor.getIdentifierOccurenceOfDocument(
                    fsw.getStringWithoutStopWord(stem.getStringAfterStemming(post.getPost_body()))));//提取word
            newTokens.setContent(fsw.getStringWithoutStopWord(stem.getStringAfterStemming("")));//对body提取topic
            tokenList.add(newTokens);
//            fileMapList.add(post.getId() + "");
            fileMapList.add(post.postId + "");
        }

        writeToFileMap(fileMapName, fileMapList);
    }

    public void analysis(List<Post> postSet) {
        //		List<String> files = FileHelper.getSubFile(projectPath, "java");
        //		projectName = CommUtil.getProjectNameFromProjectPath(projectPath);
        analysis(postSet, Global.projectName);
    }

    // add function for only keep noun and verb words 2011-11-28
    //
    //	public void processTokenOnlyNounsVerbs()
    //	{
    //
    //		for (TokenInFile currentTokenInFile : tokenList)
    //		{
    //			List<String> tokens = currentTokenInFile.getTokens();
    //			List<String> newTokens = new ArrayList<>();
    //			String word;
    //			for (int i = 0; i < tokens.size(); i++)
    //			{
    //				word = tokens.get(i);
    //				String tempString = WordNetDict.getInstance().getNounsOrVerbs(
    //						word);
    //				if (tempString != null)
    //				{
    //					newTokens.add(tempString);
    //				}
    //			}
    //			currentTokenInFile.setTokens(newTokens);
    //		}
    //
    //	}

    //	public void addClassAndMethodIdentification(List<Class> classList)
    //	{
    //		for (TokenInFile curTokenInFile : tokenList)
    //		{
    //			Class oClass = ASTUtil.getClassByFileName(curTokenInFile.getFileName(), classList);
    //			if(oClass == null)
    //			{
    //				MicroLog.log(Level.INFO, "can't find the class by className " + curTokenInFile.getFileName());
    //				continue;
    //			}
    //			List <String> tokens= curTokenInFile.getTokens();
    //			List<Method> methodsList=oClass.getMethods();
    //			int countOfMethod=methodsList.size();
    //			
    //			for (int i=0; i< countOfMethod;i++)
    //			{
    //				tokens.add(methodsList.get(i).getName().toString());
    //				tokens.add(oClass.getName().toString());		
    //			}		
    //			
    //		}
    //	}

    //	
    //	public List<String> getTokensOfString(String tokenString)
    //	{
    //		List<String> content = new ArrayList<String>();
    //		FudanIdentifierNameTokeniserFactory factory = new FudanIdentifierNameTokeniserFactory();
    //		FudanIdentifierNameTokeniser tokeniser = new FudanIdentifierNameTokeniser(
    //				factory.create());
    //		tokeniser.setMinTokenLength(2);
    //		List<String> tokens;
    //		tokens = tokeniser.tokeniseOnly(tokenString);
    //		
    //		
    //			
    //		for (String token : tokens)
    //		{
    //			
    //			if (isStringAccepted(content, token))
    //				content.add(token);
    //			
    //		}
    //
    //		return content;
    //	}
    //		tokens = tokeniser.tokeniseOnly(tokenString);
    //		for (String token : tokens)
    //		{
    //			if (isStringAccepted(content, token))
    //				content.add(token);
    //		}
    //
    //		return content;
    //	}
    public List<String> getIdentifierOccurenceOfString(String tokenString) {
        List<String> content = new ArrayList<String>();
        FudanIdentifierNameTokeniserFactory factory = new FudanIdentifierNameTokeniserFactory();
        FudanIdentifierNameTokeniser tokeniser = new FudanIdentifierNameTokeniser(factory.create());
        tokeniser.setMinTokenLength(2);

        List<String> tokens;

        tokens = tokeniser.tokenise(tokenString);

        for (String token : tokens) {
            if (isStringAccepted(content, token)) {
                content.add(token);
            }
        }

        return content;
    }

    public List<String> getIdentifierOccurenceOfDocument(String tokenString) {
        List<String> content = new ArrayList<String>();
        FudanIdentifierNameTokeniserFactory factory = new FudanIdentifierNameTokeniserFactory();
        FudanIdentifierNameTokeniser tokeniser = new FudanIdentifierNameTokeniser(factory.create());
        tokeniser.setMinTokenLength(2);

        List<String> tokens;
        tokens = tokeniser.tokenise(tokenString);

        for (String token : tokens) {
            if (isStringAccepted(content, token)) {
                content.add(token);
            }
        }

        return content;
    }

    //	public List<String> getIdentifierOccurenceOfDocument(String fileName)
    //	{
    //		List<String> content = new ArrayList<String>();
    //		FudanIdentifierNameTokeniserFactory factory = new FudanIdentifierNameTokeniserFactory();
    //		FudanIdentifierNameTokeniser tokeniser = new FudanIdentifierNameTokeniser(
    //				factory.create());
    //		tokeniser.setMinTokenLength(2);
    //		try
    //		{
    //			BufferedReader inputFile = new BufferedReader(new FileReader(
    //					fileName));
    //			String line;
    //			List<String> tokens;
    //			while (true)
    //			{
    //				line = inputFile.readLine();
    //				if (line == null)
    //				{
    //					break;
    //				}
    //				tokens = tokeniser.tokenise(line);
    //				for (String token : tokens)
    //				{
    //					if (isStringAccepted(content, token)) content.add(token);
    //				}
    //			}
    //			inputFile.close();
    //		}
    //		catch (IOException ioEx)
    //		{
    //			System.err.println(ioEx.getMessage());
    //		}
    //		return content;
    //	}
    public List<TokenInFile> getTokens() {
        return tokenList;
    }

    public void isAcceptAlphabet(boolean isAcceptAlphabet) {
        this.isAcceptAlphabet = isAcceptAlphabet;
    }

    public void isAcceptDigit(boolean isAcceptDigit) {
        this.isAcceptDigit = isAcceptDigit;
    }

    public void isAcceptReduplication(boolean isAcceptReduplication) {
        this.isAcceptReduplication = isAcceptReduplication;
    }

    private boolean isStringAccepted(List<String> content, String str) {
        str = str.trim();

        boolean accpeted = false;

        if (isAcceptAlphabet) {
            return ((str.charAt(0) >= 'a') && (str.charAt(0) <= 'z')) ||
            ((str.charAt(0) >= 'A') && (str.charAt(0) <= 'Z'));
        }

        if (isAcceptDigit) {
            return (str.charAt(0) >= '0') && (str.charAt(0) <= '9');
        }

        if (isAcceptReduplication == false) {
            return content.indexOf(str) == -1;
        }

        return accpeted;
    }

    //	public void saveToDB()
    //	{
    //		DBHelper.setCurrentProjectName(projectName);
    //		DBHelper.openConn();
    //		TokenDAL.clearRelatedData();
    //		TokenDAL.insertTokens(tokenList);
    //		DBHelper.closeConn();
    //	}
    public void setTokens(List<TokenInFile> tokenList) {
        this.tokenList = tokenList;
    }

    public void writeToFile(List<TokenInFile> list, String fileName) {
        FileHelper.createFile(fileName);

        PrintWriter outputStream = null;

        try {
            outputStream = new PrintWriter(fileName);
            outputStream.println(tokenList.size());

            for (TokenInFile tokens : tokenList) {
                outputStream.println(CommUtil.ListToString(tokens.getTokens()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            outputStream.close();
        }
    }

    public void writeToFile(String fileName) {
        writeToFile(tokenList, fileName);
    }

    // TODO lihongwei add
    private void writeToFileMap(String fileMapName, List<String> fileMapList) {
        FileHelper.createFile(fileMapName);

        PrintWriter outputStream = null;

        try {
            outputStream = new PrintWriter(fileMapName);

            for (String tempString : fileMapList) {
                outputStream.println(tempString);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            outputStream.close();
        }
    }
}
