package it.pincio.telegrambot.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ResourceUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NormalizeText {
	
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
	    try {
	    	List<String> stopwords = Files.readAllLines(Paths.get("stopwords.txt")); 
	    	String[] allWords = text.toLowerCase().split(" ");
	    	
	    	StringBuilder builder = new StringBuilder();
	        for(String word : allWords) {
	            if(!stopwords.contains(word)) {
	                builder.append(word);
	                builder.append(' ');
	            }
	        }
	         
	        text = builder.toString().trim();
	        
	        /*
	        String stopwordsRegex = stopwords.stream()
    	      .collect(Collectors.joining("|", "\\b(", ")\\b\\s?"));
    	 
    	    text = original.toLowerCase().replaceAll(stopwordsRegex, "");
    	    */
	    }catch (Exception e) {
			log.error("stopwords.txt not loaded", e);
		}
	    
		log.info("Normalize text: "+text);
	    return text; 
	}
}
