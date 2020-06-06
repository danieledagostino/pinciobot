package it.pincio.telegrambot.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FirstEntryPointTelegramBot extends TelegramLongPollingBot {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private HttpHeaders httpHeaders;

	@Value("${PINCIO_BOT_TOKEN}")
	private String TOKEN;

//	@Value("${PERSISTENT_SERVICE}")
//	private String PERSISTENT_SERVICE;
//
//	@Value("${ELASTIC_SERVICE}")
//	private String ELASTIC_SERVICE;

	@Value("${CLOUDAMQP_APIKEY}")
	private String CLOUDAMQP_APIKEY;

	@Value("${CLOUDAMQP_URL}")
	private String CLOUDAMQP_URL;

	private String DB_SEARCH = "/bot/search/";
	private String EL_INSERT = "/bot/elastic/insert/";

	private final static String QUEUE_NAME = "bot_update";

	@Override
	public void onUpdateReceived(Update update) {
		if (update.hasMessage() && update.getMessage().hasText()) {
			
			if (update.getMessage().getText().contains("?")) {
				ConnectionFactory factory = new ConnectionFactory();
				Connection connection = null;
				try {
					factory.setUri(CLOUDAMQP_URL);
					factory.setRequestedHeartbeat(30);
					factory.setConnectionTimeout(120);
					
					connection = factory.newConnection();
					Channel channel = connection.createChannel();
					channel.queueDeclare(QUEUE_NAME, false, false, false, null);
					
					ObjectMapper parser = new ObjectMapper();
					String jsonUpdate = parser.writeValueAsString(update);
					
					channel.basicPublish("", QUEUE_NAME, null, jsonUpdate.getBytes());
				} catch (IOException e) {
					log.error("Probably QUEUE_NAME wrong", e);
				} catch (KeyManagementException | NoSuchAlgorithmException | URISyntaxException e1) {
					log.error("", e1);
				} finally {
					if (connection.isOpen()) {
						try {
							connection.close();
						} catch (IOException e) {
							log.error("Connection error during closing", e);
						}
					}
				}
			}
//			if (update.getMessage().getText().contains("?")) {
//				JsonArray jsonArray = new JsonArray(1);
//				jsonArray.add(update.getMessage().getText());
//				HttpEntity<String> request = new HttpEntity<>(jsonArray.toString(), httpHeaders);
//				ResponseEntity<String> response = restTemplate.exchange(ELASTIC_SERVICE + EL_INSERT, HttpMethod.PUT,
//						request, String.class);
//			}
		}

	}

	@PostConstruct
	public void registerBot() {
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
