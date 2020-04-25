
package it.pincio.telegrambot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import it.pincio.telegrambot.controller.FirstEntryPointController;

@SpringBootApplication
public class Application 
{

	private static final Logger logger = LoggerFactory.getLogger(Application.class);
	
	public static void main(String[] args) 
	{
		ApiContextInitializer.init();

//        TelegramBotsApi botsApi = new TelegramBotsApi();
//
//        try {
//            botsApi.registerBot(new FirstEntryPointController());
//        } catch (TelegramApiException e) {
//            logger.error("Registration hook error", e);
//        }
        
		SpringApplication.run(Application.class, args);
	}
}
