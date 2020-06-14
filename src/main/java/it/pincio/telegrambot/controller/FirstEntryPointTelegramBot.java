package it.pincio.telegrambot.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.CommandRegistry;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import it.pincio.persistence.bean.Faq;
import it.pincio.telegrambot.service.ConfigurationService;
import it.pincio.telegrambot.service.PrivateChatService;
import it.pincio.telegrambot.service.PublicChatService;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FirstEntryPointTelegramBot extends TelegramLongPollingCommandBot {

	@Value("${PINCIO_BOT_TOKEN}")
	private String TOKEN;
	
	@Value("${USER_BOT}")
	private String userBot;
	
	@Autowired
	private PublicChatService publicChatService;
	
	@Autowired
	private ConfigurationService configurationService;

	@Override
	public void processNonCommandUpdate(Update update) {
		if (update.hasMessage() && update.getMessage().hasText()) {
			
			String textMessage = update.getMessage().getText();
			Chat chat = update.getMessage().getChat();
			Integer messageId = update.getMessage().getMessageId();
			if (textMessage.substring(textMessage.length() - 1, textMessage.length()).equals("?")) {
				List<Faq> listFaq = publicChatService.checkQuestion(textMessage);
				
				if (listFaq.size() == 1)
				{
					Faq faq = listFaq.get(0);
					SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
			                .setChatId(chat.getId())
			                .setReplyToMessageId(messageId)
			                .setText(faq.getAnswer());
				}
				else if (listFaq.size() > 1)
				{
					
					InlineKeyboardMarkup replyMarkup = new InlineKeyboardMarkup();
					List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<List<InlineKeyboardButton>>();
					List<InlineKeyboardButton> keyboardButtons = null;
					InlineKeyboardButton inlineKB = null;
					
					for (Faq faq : listFaq) {
						inlineKB = new InlineKeyboardButton(faq.getHint());
						inlineKB.setCallbackData(String.valueOf(faq.getId()));
						
						keyboardButtons = new ArrayList<InlineKeyboardButton>();
						keyboardButtons.add(inlineKB);
						keyboardRows.add(keyboardButtons);
					}
					
					replyMarkup.setKeyboard(keyboardRows);
					
					SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
					message.setChatId(chat.getId())
			                .setReplyToMessageId(messageId)
			                .setReplyMarkup(replyMarkup)
			                .setText("Ciao, sono il tuo assistente. Non ho ben capito cosa stai cercando. Scegli qui sotto la domanda");
					
					try {
						execute(message);
					} catch (TelegramApiException e) {
						log.error("Message not sent", e);
					}
				}
			}
		}

	}

	@PostConstruct
	public void postConstruct() {
		
		List<BotCommand> commands = new ArrayList<BotCommand>();
		try {
			commands = configurationService.getAllCommands();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			log.error("Java reflectiong for instancing commands didn't work");
		}
		
		for (BotCommand botCommand : commands) {
			register(botCommand);
		}
		
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
