
package it.pincio.telegrambot;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import it.pincio.telegrambot.controller.FirstEntryPointTelegramBot;

@SpringBootApplication
public class Application 
{

	private static final Logger logger = LoggerFactory.getLogger(Application.class);
	private static TelegramBotsApi botsApi;
	
	public static void main(String[] args) 
	{
		ApiContextInitializer.init();
//
//        botsApi = new TelegramBotsApi();
//
//        try {
//            botsApi.registerBot(new FirstEntryPointTelegramBot());
//        } catch (TelegramApiException e) {
//            logger.error("Registration hook error", e);
//        }
        
		SpringApplication.run(Application.class, args);
	}
	
}
