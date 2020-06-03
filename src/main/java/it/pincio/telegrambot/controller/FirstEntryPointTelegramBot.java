package it.pincio.telegrambot.controller;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FirstEntryPointTelegramBot extends TelegramLongPollingBot {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${PINCIO_BOT_TOKEN}")
	private String TOKEN;
	
	@Value("${PERSISTENT_SERVICE}")
	private String PERSISTENT_SERVICE;
	
	@Value("${ELASTIC_SERVICE}")
	private String ELASTIC_SERVICE;
	
	private String DB_SEARCH = "/bot/search/";
	private String EL_INSERT = "/bot/elastic/insert/";

	@Override
	public void onUpdateReceived(Update update) {
		if (update.hasMessage() && update.getMessage().hasText()) {
	        try {
	        	
	            //String result = restTemplate.getForObject(PERSISTENT_SERVICE+GET_UPDATE_METHOD, String.class, update.getMessage().getText());
	        	
	        	String result = restTemplate.getForObject(ELASTIC_SERVICE+EL_INSERT, String.class, update.getMessage().getText());
	        	
	            SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
	            		.setChatId(update.getMessage().getChatId())
	            		.setText(result);
	            
	            execute(message); // Call method to send the message
	        } catch (TelegramApiException e) {
	            log.error("", e);
	        }
	    }
		
	}
	
	@PostConstruct
    public void registerBot(){
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new FirstEntryPointTelegramBot());
        } catch (TelegramApiException e) {
            log.error("", e);
        }
    }

	@Override
	public String getBotUsername() {
		return "NewPincioBot";
	}

	@Override
	public String getBotToken() {
		return TOKEN;
	}

	@PostConstruct
	public void start() {
		log.debug("token: {}", TOKEN);
	}
}
