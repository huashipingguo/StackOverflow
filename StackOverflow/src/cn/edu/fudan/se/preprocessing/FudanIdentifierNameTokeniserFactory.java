package cn.edu.fudan.se.preprocessing;

import uk.ac.open.crc.intt.IdentifierNameTokeniser;
import uk.ac.open.crc.intt.IdentifierNameTokeniserFactory;

public class FudanIdentifierNameTokeniserFactory 
{
	private IdentifierNameTokeniserFactory factory;
	public FudanIdentifierNameTokeniserFactory()
	{
		factory = new IdentifierNameTokeniserFactory();
		factory.setSeparatorCharacters(FudanIdentifierNameTokeniser.SPLIT_STRING);
		factory.setProjectVocabularyThreshold(2);		
	}
	public IdentifierNameTokeniser create()
	{
		return factory.create();
	}
	

}
