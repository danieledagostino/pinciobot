package it.pincio.telegrambot.command;

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

import com.vdurmont.emoji.EmojiParser;

import it.pincio.persistence.bean.Event;
import it.pincio.telegrambot.dto.EventDto;
import it.pincio.telegrambot.service.EventService;
import it.pincio.telegrambot.utility.EmojiiCode;
import it.pincio.telegrambot.utility.TelegramKeyboard;
import lombok.extern.slf4j.Slf4j;

import static it.pincio.telegrambot.utility.DateUtils.dateToString;

@Component
@Slf4j
public class EventNextCommand extends BotAndCallbackCommand {
	
	private static final String COMMAND_IDENTIFIER = "prossimo_evento";
	private static final String COMMAND_DESCRIPTION = "Mostra il prossimo imminente evento. Usa /help "+COMMAND_IDENTIFIER+" per maggiori info";
	private static final String EXTENDED_DESCRIPTION = "This command displays all commands the bot has to offer.\n /help [command] can display deeper information";
	
	@Value("${USER_BOT}")
	private String USER_BOT;
	
	@Autowired
	EventService eventService;
	
	@Autowired
	MessageSource messageSource;
	
	public EventNextCommand() {
		super(COMMAND_IDENTIFIER, COMMAND_DESCRIPTION);
	}

	@Override
	public void execute(AbsSender absSender, User user, Chat chat, Integer messageId, String[] arguments) {
		// TODO Auto-generated method stub
		
		EventDto e = eventService.searchNextEvent();
		
		InlineKeyboardMarkup replyMarkup = TelegramKeyboard.makeOneRow("Partecipa!", "partecipa_evento,"+e.getId());
		
		String label = messageSource.getMessage("eventlist.service.participant.label", null, Locale.ITALY);
		
		String eventTitle = EmojiParser.parseToUnicode(":ticket:"+" "+e.getTitle());
		String numberOfParticipants = EmojiParser.parseToUnicode(":runner:"+" "+e.getNumberOfParticipants());
		
		SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                .setChatId(chat.getId())
                .setReplyToMessageId(messageId)
                .setReplyMarkup(replyMarkup)
                .setText(eventTitle+" "+dateToString(e.getStartDate())+"\n"+numberOfParticipants+"\n"+
                		e.getDescription());
        try {
        	absSender.execute(message); // Call method to send the message
        } catch (TelegramApiException ex) {
        	log.error(messageSource.getMessage("log.telegram.send.error", null, Locale.ITALY), ex);
        }
	}

	@Override
	public SendMessage processCallback(CallbackQuery callbackQuery, String... args) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
