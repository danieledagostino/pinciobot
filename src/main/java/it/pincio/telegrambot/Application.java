
package it.pincio.telegrambot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;

@SpringBootApplication
public class Application 
{

	private static final Logger logger = LoggerFactory.getLogger(Application.class);
	private static TelegramBotsApi botsApi;
	
	public static void main(String[] args) 
	{
		ApiContextInitializer.init();
		
		SpringApplication.run(Application.class, args);
	}
	
}
