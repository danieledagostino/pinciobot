package it.pincio.telegrambot.command;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import it.pincio.persistence.bean.Event;
import it.pincio.telegrambot.service.EventService;
import it.pincio.telegrambot.utility.TelegramKeyboard;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EventAddCommand extends BotAndCallbackCommand {
	
	private static final String COMMAND_IDENTIFIER = "aggiungi_evento";
	private static final String COMMAND_DESCRIPTION = "Crea il tuo personale evento. Usa /help "+COMMAND_IDENTIFIER+" per maggiori info";
	private static final String EXTENDED_DESCRIPTION = "This command displays all commands the bot has to offer.\n /help [command] can display deeper information";
	
	@Autowired
	private EventService eventService;
	
	public EventAddCommand() {
		super(COMMAND_IDENTIFIER, COMMAND_DESCRIPTION);
	}

	@Override
	public void execute(AbsSender absSender, User user, Chat chat, Integer messageId, String[] arguments) {
		
		
		Event event = eventService.generateEmptyEvent(user.getId());
		
		String text = "Premi sul pulsante";
		
		SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                .setChatId(chat.getId())
                .setReplyMarkup(TelegramKeyboard.makeOneRowWithLink("Crea evento", "http://pinciogames.altervista.org/evento.php?token="+event.getToken()))
                .setText(text);
        try {
        	absSender.execute(message); // Call method to send the message
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
