package cn.edu.fudan.se.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.imageio.ImageIO;
import javax.sound.midi.MidiDevice.Info;
import javax.swing.ImageIcon;

import cn.edu.fudan.se.bean.TokenInFile;
import cn.edu.fudan.se.preprocessing.TokenExtractor;

public class FileHelper
{

	private  List<String> getSubFile(String path, FileFilter filter)
	{
		List<String> result = new ArrayList<String>();
		File file = new File(path);
		if( file.isDirectory())
		{			
			for(File subFile :file.listFiles(filter))
			{
				result.addAll(getSubFile(subFile.getAbsolutePath(), filter));
			}
		}
		else 
		{
			result.add(file.getAbsolutePath());
		}
		return result;
	
	}

	public static String[] getContentArray(String path)
	{
		List<String> list = new ArrayList<String>();				
		File file = new File(path);
		try
		{
			BufferedReader in = new BufferedReader(new FileReader(file));
			String line = null;			
			while(null != (line=in.readLine()))
			{
				list.add(line);
			}
			in.close();
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] result ;
		result = list.toArray(new String[0]);
		return result;
		
	}
	
	
	
	public static String getContent(String path) {
		String content = "";
		try {
			File file = new File(path);
			BufferedReader in = new BufferedReader(new FileReader(file));			
			StringBuilder buffer = new StringBuilder();
			String line = null;
	
			while (null != (line = in.readLine())) {
				buffer.append("\t" + line);
				buffer.append("\n");
				
			}
	
			content = buffer.toString();
			in.close();
			
	
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}

	
	/**
	 * 鍦ㄦ枃浠跺す涓紝鑾峰彇鎵�湁鎸囧畾鍚庣紑鍚庣殑鏂囦欢
	 * @param path
	 * @param extension
	 * @return
	 */
	public static List<String> getSubFile(String path, String extension)
	{		 
		FileFilter fileFilter = new ExtensionFileFilter(extension);
		FileHelper fileHelper = new FileHelper();
		return fileHelper.getSubFile(path, fileFilter);

	}


	public static void createFile(String fileName) {
		File file = new File(fileName);
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			MicroLog.log(Level.INFO, "create file error");
		}
		
	}
	

	public static void deleteFile(String filename)
	{
		File f = new File(filename);
		if(f.exists())
			f.delete();
	}
	
	public static void createFileWithTokenize(String fileName, String content)
	{
		TokenExtractor extractor = new TokenExtractor();
		extractor.isAcceptDigit(false);
		extractor.isAcceptReduplication(true);
		content = CommUtil.ListToString(extractor.getIdentifierOccurenceOfString(content));
		createFile(fileName, content);
		
	}
	
	public static void createFile(String fileName, String content)
	{
		
		PrintWriter pw;
		try
		{
			pw = new PrintWriter(fileName);
			pw.write(content);
			pw.close();
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

	
	public static byte[] transforPNGFiletoByteStream(String pngFileName) 
	{

		File pngfile=new File(pngFileName);
		
		
		int len=(int) pngfile.length();
		byte[] pngfileBytes=new byte[len];
		
		FileInputStream filestream;
		try
		{
			filestream = new FileInputStream(pngfile);
			filestream.read(pngfileBytes);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
        return pngfileBytes;
	
		//TODO  LIHONGWEI
	}
	
	public static void readByteStreamtoPNGFile(String pngFileName, byte[] imageByteStream)
	{
		
		DataOutputStream out;
		try
		{
			out = new DataOutputStream(new FileOutputStream(pngFileName));
			out.write(imageByteStream);
			out.close();
			
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

		
		//TODO LIHONGWEI
	}

	
	public static List<String> getContentAsList(String instanceFile)
	{
		List<String> result = new ArrayList<String>();
		try
		{
			File file = new File(instanceFile);
			BufferedReader in = new BufferedReader(new FileReader(file));
			String line = null;
			while(null != (line = in.readLine()))
			{
				result.add(line);
			}
		}
		catch (IOException e)
		{
			// TODO: handle exception
		}
		return result;
	}
	
}

class ExtensionFileFilter implements FileFilter
{
	String acceptedExtension;
	public ExtensionFileFilter(String acceptedExtension)
	{
		this.acceptedExtension = acceptedExtension;
	}
	
	public boolean accept(File pathname)
	{
		return pathname.getName().endsWith(acceptedExtension) || pathname.isDirectory();			
	}
	
	
	
	
}

