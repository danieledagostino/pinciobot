package it.pincio.telegrambot.command;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
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

@Component
public class MyEventsCommand extends BotAndCallbackCommand {
	
	private static final String COMMAND_IDENTIFIER = "miei_eventi";
	private static final String COMMAND_DESCRIPTION = "shows all commands. Use /help [command] for more info";
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
		emojii[0] = eventService.CONFIRMED_ICON;
		emojii[1] = eventService.UNCOMPLETE_ICON;
		emojii[2] = eventService.OLD_ICON;
		
		SendMessage messageToPrivateChat = new SendMessage() // Create a SendMessage object with mandatory fields
                .setChatId(String.valueOf(user.getId()))
                .setReplyMarkup(replyMarkup)
                .setText(messageSource.getMessage("myevent.command.list", emojii, Locale.ITALY));
		
		
		//send message to the user to inform to switch to the private chat
		SendMessage messageToInformUser = new SendMessage() // Create a SendMessage object with mandatory fields
                .setChatId(chat.getId())
                .setReplyToMessageId(messageId)
                .setText("Apri la conversazione privata col bot @NewPincioBot (<- premi sul nome del bot), "+
            			"premi 'Avvia' e lancia di nuovo il comando /miei_eventi");
        try {
        	absSender.execute(messageToPrivateChat);
        	if (chat.isGroupChat()) {
        		absSender.execute(messageToInformUser); // Call method to send the message
        	}
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
	}

	@Override
	public SendMessage processCallback(CallbackQuery callbackQuery, String... args) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
