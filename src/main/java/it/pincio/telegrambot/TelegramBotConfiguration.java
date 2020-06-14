package it.pincio.telegrambot;

import javax.annotation.PreDestroy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;

import it.pincio.telegrambot.controller.FirstEntryPointTelegramBot;
import lombok.extern.slf4j.Slf4j;

@Configuration
@SpringBootApplication
@PropertySource("classpath:app.properties")

@Slf4j
public class TelegramBotConfiguration {
	
	private static BotSession session;

	public static void main(String[] args) 
	{
		ApiContextInitializer.init();
		
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
        	session = telegramBotsApi.registerBot(new FirstEntryPointTelegramBot());
        } catch (TelegramApiException e) {
            log.error("******************* Registration hook error *******************");
        }
		
		log.info("******************* ApiContextInitializer.init() *******************");
		SpringApplication.run(TelegramBotConfiguration.class, args);
	}
	
	@PreDestroy
	public static void destroy() {
		if (session != null && session.isRunning()) {
			session.stop();
		}
	}
}
