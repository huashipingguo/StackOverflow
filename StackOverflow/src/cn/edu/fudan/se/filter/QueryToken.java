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
    
    public QueryToken()
    {
    	fsw = new FilterStopWord();
    	fsw.setStopWordLength(4);//
    	ste = new Stemmer();
    	factory = new FudanIdentifierNameTokeniserFactory();
    	identifierNametokeniser = factory.create();
    }

    public List<String> tokeniseOnly(String line) {
    	String stemm = ste.getStringAfterStemming(line);
    	String filterStopWord = fsw.getStringWithoutStopWord(stemm);
    	System.out.println("Stem:"+stemm);
        String[] tokens = identifierNametokeniser.tokenise(filterStopWord);
        ArrayList<String> result = new ArrayList<String>();

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

        return result;
    }
    
    public static void main(String args[])
    {
    	QueryToken qt = new QueryToken();
    	System.out.println(qt.tokeniseOnly("I cannot connect to mysql").toString());
    }
}
