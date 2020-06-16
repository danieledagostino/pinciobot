package it.pincio.telegrambot.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import it.pincio.persistence.bean.Faq;
import it.pincio.persistence.dao.FaqRepository;
import it.pincio.telegrambot.utility.Costants;
import it.pincio.telegrambot.utility.NormalizeText;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

@Service
@Slf4j
public class PublicChatService {
	
	@Autowired
	FaqRepository faqRepository;
	
	@Value("${DB_REQ_SCORE}")
	private BigDecimal DB_REQ_SCORE;
	
	@Value("${DB_REQ_HINT}")
	private BigDecimal DB_REQ_HINT;
	
	@Value("${CLOUDAMQP_URL}")
	private String CLOUDAMQP_URL;
	
	public List<Faq> checkQuestion(String textMessage)
	{
		try {
			textMessage = NormalizeText.execute(textMessage);
		} catch (IOException e) {
			log.error("Generic error: stopwords.txt not found");
		}
		return faqRepository.searchReabilityAnswer(textMessage, DB_REQ_SCORE, DB_REQ_HINT);
	}
	
	public InlineKeyboardMarkup prepareFaqResponse(List<Faq> listFaq) {
		InlineKeyboardMarkup replyMarkup = new InlineKeyboardMarkup();
		List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<List<InlineKeyboardButton>>();
		List<InlineKeyboardButton> keyboardButtons = null;
		InlineKeyboardButton inlineKB = null;
		
		for (Faq faq : listFaq) {
			inlineKB = new InlineKeyboardButton(faq.getHint());
			inlineKB.setCallbackData("faq_dettaglio,"+faq.getId());
			
			keyboardButtons = new ArrayList<InlineKeyboardButton>();
			keyboardButtons.add(inlineKB);
			keyboardRows.add(keyboardButtons);
		}
		
		inlineKB = new InlineKeyboardButton("Nessuna di queste");
		inlineKB.setCallbackData("faq_dettaglio,0");
		
		keyboardButtons = new ArrayList<InlineKeyboardButton>();
		keyboardButtons.add(inlineKB);
		keyboardRows.add(keyboardButtons);
		
		replyMarkup.setKeyboard(keyboardRows);
		
		return replyMarkup;
	}
	
	public void sendToMessageQueue(Update update) {
		ConnectionFactory factory = new ConnectionFactory();
		Connection connection = null;
		try {
			factory.setUri(CLOUDAMQP_URL);
			factory.setRequestedHeartbeat(30);
			factory.setConnectionTimeout(120);
			
			connection = factory.newConnection();
			Channel channel = connection.createChannel();
			channel.queueDeclare(Costants.TG_UPDATE_QUEUE, false, false, false, null);
			
			ObjectMapper parser = new ObjectMapper();
			String jsonUpdate = parser.writeValueAsString(update);
			
			channel.basicPublish("", Costants.TG_UPDATE_QUEUE, null, jsonUpdate.getBytes());
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
}
