package it.pincio.telegrambot.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ResourceUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NormalizeText {
	
	@Value("${WORD_MIN_LENGHT}")
	private static Boolean WORD_MIN_LENGHT;

	public static String execute(String text) throws IOException
	{
	    Map<String, String> charConverter = new HashMap<String, String>();
	    charConverter.put("Å ","S");
	    charConverter.put("Å¡","s");charConverter.put("Å½","Z");charConverter.put("Å¾","z");charConverter.put("Ã€","A");charConverter.put("Ã�","A");charConverter.put("Ã‚","A");charConverter.put("Ãƒ","A");
	    charConverter.put("Ã„","A");charConverter.put("Ã…","A");charConverter.put("Ã†","A");charConverter.put("Ã‡","C");charConverter.put("Ãˆ","E");charConverter.put("Ã‰","E");charConverter.put("ÃŠ","E");charConverter.put("Ã‹","E");
	    charConverter.put("ÃŒ","I");charConverter.put("Ã�","I");charConverter.put("ÃŽ","I");charConverter.put("Ã�","I");charConverter.put("Ã‘","N");charConverter.put("Ã’","O");charConverter.put("Ã“","O");charConverter.put("Ã”","O");
	    charConverter.put("Ã•","O");charConverter.put("Ã–","O");charConverter.put("Ã˜","O");charConverter.put("Ã™","U");charConverter.put("Ãš","U");charConverter.put("Ã›","U");charConverter.put("Ãœ","U");charConverter.put("Ã�","Y");
	    charConverter.put("Ãž","B");charConverter.put("ÃŸ","Ss");charConverter.put("Ã ","a");charConverter.put("Ã¡","a");charConverter.put("Ã¢","a");charConverter.put("Ã£","a");charConverter.put("Ã¤","a");charConverter.put("Ã¥","a");
	    charConverter.put("Ã¦","a");charConverter.put("Ã§","c");charConverter.put("Ã¨","e");charConverter.put("Ã©","e");charConverter.put("Ãª","e");charConverter.put("Ã«","e");charConverter.put("Ã¬","i");charConverter.put("Ã­","i");
	    charConverter.put("Ã®","i");charConverter.put("Ã¯","i");charConverter.put("Ã°","o");charConverter.put("Ã±","n");charConverter.put("Ã²","o");charConverter.put("Ã³","o");charConverter.put("Ã´","o");charConverter.put("Ãµ","o");
	    charConverter.put("Ã¶","o");charConverter.put("Ã¸","o");charConverter.put("Ã¹","u");charConverter.put("Ãº","u");charConverter.put("Ã»","u");charConverter.put("Ã½","y");charConverter.put("Ã¾","b");charConverter.put("Ã¿","y");

	    // normalize string
	    for (String c : charConverter.keySet()) {
	    	text = text.replaceAll(c, charConverter.get(c));
		}
	    
	 // load stopwords from file
	    File stopwords = ResourceUtils.getFile(
			      "stopwords.txt");
	    
	    FileReader fr=new FileReader(stopwords);   //reads the file  
	    BufferedReader br=new BufferedReader(fr);  //creates a buffering character input stream  
	    String line = null;  
	    while((line=br.readLine())!=null)  
	    {  
	    	text = text.replaceAll(line, "");
	    }
	    
	    fr.close(); 
	    
		log.info("Normalize text: "+text);
	    return text; 
	}
}
