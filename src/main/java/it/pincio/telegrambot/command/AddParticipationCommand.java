package it.pincio.telegrambot.command;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.DefaultBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import it.pincio.persistence.bean.Event;
import it.pincio.persistence.bean.Partecipant;
import it.pincio.persistence.bean.PartecipantId;
import it.pincio.telegrambot.service.AddParticipationService;
import it.pincio.telegrambot.service.EventService;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AddParticipationCommand extends BotAndCallbackCommand {
	
	private static final String COMMAND_IDENTIFIER = "partecipa_evento";
	private static final String COMMAND_DESCRIPTION = "shows all commands. Use /help [command] for more info";
	private static final String EXTENDED_DESCRIPTION = "This command displays all commands the bot has to offer.\n /help [command] can display deeper information";
	
	@Autowired
	EventService eventService;
	
	@Autowired
	AddParticipationService addParticipationService;
	
	
	public AddParticipationCommand() {
		super(COMMAND_IDENTIFIER, COMMAND_DESCRIPTION);
	}

	@Override
	public void execute(AbsSender absSender, User user, Chat chat, Integer messageId, String[] arguments) {

		List<Event> events = eventService.searchCurrentEvents();
		
		if (events.size() > 0) {
			InlineKeyboardMarkup replyMarkup = addParticipationService.prepareJoinMessage(events);
			
			SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
					.setChatId(chat.getId())
					.setReplyMarkup(replyMarkup)
					.setReplyToMessageId(messageId)
					.setText("Scegli l'evento al quale partecipare");
			
			try {
				absSender.execute(message); // Call method to send the message
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		}
		
		
	}

	@Override
	public String processCallback(Message repliedMessage, String... args) {
		
		try {
			Event e = eventService.findById(Integer.valueOf(args[0]));
			
			Partecipant partecipant = new Partecipant();
			PartecipantId id = new PartecipantId();
			id.setUser(String.valueOf(repliedMessage.getFrom().getId()));
			id.setEvent(e);
			
			partecipant.setPartecipantId(id);
			addParticipationService.insert(partecipant);
			return "Partecipazione aggiunta. Grazie!";
			
		} catch (Exception e) {
			log.error("Partecipazione non confermata", e);
			return "Partecipazione non confermata.\n"+
				"Puoi gestire tutto comodamente in chat privata col bot. Prova a premere qui @PincioBot";
		}
	}

	

}
