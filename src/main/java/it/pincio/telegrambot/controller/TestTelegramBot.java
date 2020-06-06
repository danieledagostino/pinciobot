package it.pincio.telegrambot.controller;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.google.gson.JsonArray;

import lombok.extern.slf4j.Slf4j;

//@Component
@Slf4j
public class TestTelegramBot extends TelegramLongPollingBot {
	
	@Value("${PINCIO_BOT_TOKEN}")
	private String TOKEN;
	
	@Override
	public void onUpdateReceived(Update update) {
		if (update.hasMessage() && update.getMessage().hasText()) {
	        try {
	        	
	        	log.info("******************* TEST onUpdateReceived *******************");
	            SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
	            		.setChatId(update.getMessage().getChatId())
	            		.setText("Elaboro la richiesta per: "+update.getMessage().getText());
	            execute(message); // Call method to send the message
	            
	        } catch (TelegramApiException e) {
	            log.warn("Probably message empty");
	        } catch (Exception e) {
	        	log.error("Generic comunication error", e);
	        }
	    }
		
	}
	
	@PostConstruct
    public void registerBot(){
		log.debug("token: {}", TOKEN);
    }

	@Override
	public String getBotUsername() {
		return "NewPincioBot";
	}

	@Override
	public String getBotToken() {
		return TOKEN;
	}
}
