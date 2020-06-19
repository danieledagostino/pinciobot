package it.pincio.telegrambot.command;

import java.util.Arrays;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.DefaultBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import it.pincio.telegrambot.service.EventService;
import it.pincio.telegrambot.utility.EmojiiCode;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MyEventsCommand extends BotAndCallbackCommand {
	
	private static final String COMMAND_IDENTIFIER = "miei_eventi";
	private static final String COMMAND_DESCRIPTION = "Mostra gli eventi personali. Usa /help "+COMMAND_IDENTIFIER+" per maggiori info";
	private static final String EXTENDED_DESCRIPTION = "This command displays all commands the bot has to offer.\n /help [command] can display deeper information";
	
	@Autowired
	EventService eventService;
	
	@Autowired
	MessageSource messageSource;
	
	public MyEventsCommand() {
		super(COMMAND_IDENTIFIER, COMMAND_DESCRIPTION);
	}

	@Override
	public void execute(AbsSender absSender, User user, Chat chat, Integer messageId, String[] arguments) {
		// TODO Auto-generated method stub
		
		InlineKeyboardMarkup replyMarkup = eventService.searchMyEvents(user.getId());
		
		String[] emojii = new String[3];
		emojii[0] = EmojiiCode.WHITE_HEAVY_CHECK_MARK_ICON;
		emojii[1] = EmojiiCode.HEAVY_EXCLAMATION_MARK_ICON;
		emojii[2] = EmojiiCode.VICTORY_ICON;
		
		SendMessage messageToPrivateChat = new SendMessage() // Create a SendMessage object with mandatory fields
                .setChatId(String.valueOf(user.getId()))
                .setReplyMarkup(replyMarkup)
                .setText(messageSource.getMessage("myevent.command.list", emojii, Locale.ITALY));
		
		
        try {
        	absSender.execute(messageToPrivateChat);
        } catch (TelegramApiException e) {
        	log.error(messageSource.getMessage("log.telegram.send.error", null, Locale.ITALY), e);
        }
	}

	@Override
	public SendMessage processCallback(CallbackQuery callbackQuery, String... args) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
