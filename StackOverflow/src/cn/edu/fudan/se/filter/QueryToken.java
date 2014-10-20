package cn.edu.fudan.se.filter;

import uk.ac.open.crc.intt.IdentifierNameTokeniser;

import java.util.ArrayList;
import java.util.List;

import cn.edu.fudan.se.preprocessing.FudanIdentifierNameTokeniserFactory;


public class QueryToken {
    private IdentifierNameTokeniser identifierNametokeniser;
    private FilterStopWord fsw;
    private Stemmer ste;
    private FudanIdentifierNameTokeniserFactory factory;
    private int minLength = 2;
    private ArrayList<String> result;
    
    public QueryToken(String query)
    {
    	fsw = new FilterStopWord();
    	fsw.setStopWordLength(4);//
    	ste = new Stemmer();
    	factory = new FudanIdentifierNameTokeniserFactory();
    	identifierNametokeniser = factory.create();
    	result = new ArrayList<String>();
    	tokeniseOnly(query);
    }

    public void tokeniseOnly(String line) {
    	String stemm = ste.getStringAfterStemming(line);
    	String filterStopWord = fsw.getStringWithoutStopWord(stemm);
        String[] tokens = identifierNametokeniser.tokenise(filterStopWord);

        for (String token : tokens) {
            token = token.trim();

            // add lihongwei 2011-10-28
            if (token.equals("")) {
                continue;
            }

            //   add end 2011-10-28
            if ((token.length() >= minLength) && ((int) token.charAt(0) < 255)) {
                token = token.toLowerCase();
                result.add(token);
            }
        }
    }
    
    public String getToken()
    {
    	String token = "";
    	for(int i = 0; i < result.size(); i++)
    	{
    		if(i == result.size()-1)
    			token += result.get(i)+"%";
    		else
    			token += result.get(i) + "%";
    	}
    	return token;
    	
    	
    }
    
    public static void main(String args[])
    {
//    	QueryToken qt = new QueryToken();
//    	System.out.println(qt.tokeniseOnly("I cannot connect to mysql").toString());
    }
}
