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
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.CommandRegistry;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import it.pincio.persistence.bean.Faq;
import it.pincio.telegrambot.service.ConfigurationService;
import it.pincio.telegrambot.service.PrivateChatService;
import it.pincio.telegrambot.service.PublicChatService;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FirstEntryPointTelegramBot extends TelegramLongPollingBot {

	@Value("${PINCIO_BOT_TOKEN}")
	private String TOKEN;
	
	@Value("${USER_BOT}")
	private String userBot;
	
	@Autowired
	private PublicChatService publicChatService;
	
	@Autowired
	private ConfigurationService configurationService;

	@Override
	public void onUpdateReceived(Update update) {
		if (update.hasMessage() && update.getMessage().hasText()) {
			
			String textMessage = update.getMessage().getText();
			Chat chat = update.getMessage().getForwardFromChat();
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
					List<InlineKeyboardButton> keyboardRows = new ArrayList<InlineKeyboardButton>();
					InlineKeyboardButton inlineKB = null;
					
					for (Faq faq : listFaq) {
						inlineKB = new InlineKeyboardButton(faq.getKeywords());
						inlineKB.setCallbackData(String.valueOf(faq.getId()));
						
						keyboardRows.add(inlineKB);
					}
					
					replyMarkup.setKeyboard(Arrays.asList(keyboardRows));
					
					SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
			                .setChatId(chat.getId())
			                .setReplyToMessageId(messageId)
			                .setReplyMarkup(replyMarkup)
			                .setText("");
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
		CommandRegistry commandRegistry = new CommandRegistry(false, userBot);
		
		for (BotCommand botCommand : commands) {
			commandRegistry.register(botCommand);
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
