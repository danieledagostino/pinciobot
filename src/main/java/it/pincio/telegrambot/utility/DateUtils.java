package it.pincio.telegrambot.utility;

import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {

	private static final DateTimeFormatter oldPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private static final DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
	
	public static String dateToString(Date date)
	{
		return DateFormat
				  .getDateTimeInstance(DateFormat.LONG, DateFormat.MEDIUM)
				  .format(date);
	}
}
