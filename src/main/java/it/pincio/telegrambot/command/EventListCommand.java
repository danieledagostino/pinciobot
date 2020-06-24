package it.pincio.telegrambot.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import it.pincio.telegrambot.dto.EventDto;
import it.pincio.telegrambot.service.EventService;
import it.pincio.telegrambot.service.ParticipantService;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EventListCommand extends BotAndCallbackCommand {
	
	private static final String COMMAND_IDENTIFIER = "lista_eventi";
	private static final String COMMAND_DESCRIPTION = "Mostra tutti gli eventi da oggi. Usa /help "+COMMAND_IDENTIFIER+" per maggiori info";
	private static final String EXTENDED_DESCRIPTION = "This command displays all commands the bot has to offer.\n /help [command] can display deeper information";
	
	@Autowired
	EventService eventService;
	
	@Autowired
	ParticipantService participantService; 
	
	public EventListCommand() {
		super(COMMAND_IDENTIFIER, COMMAND_DESCRIPTION);
	}

	@Override
	public void execute(AbsSender absSender, User user, Chat chat, Integer messageId, String[] arguments) {
		List<EventDto> events = eventService.searchCurrentEvents();
		
		if (events.size() > 0) {
			InlineKeyboardMarkup replyMarkup = eventService.prepareJoinMessage(events);
			
			SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
					.setChatId(chat.getId())
					.setReplyMarkup(replyMarkup)
					//.setReplyToMessageId(messageId)
					.setText(messageSource.getMessage("eventlist.command.text", null, Locale.ITALY));
			
			try {
				absSender.execute(message); // Call method to send the message
			} catch (TelegramApiException e) {
				log.error(messageSource.getMessage("log.telegram.send.error", null, Locale.ITALY), e);
			}
		}
		
	}

	@Override
	public SendMessage processCallback(CallbackQuery callbackQuery, String... args) {
		SendMessage sendMessage = new SendMessage();
		Chat chat = callbackQuery.getMessage().getChat();
		
		EventDto e = eventService.findById(Integer.valueOf(args[0]));
		
		if ("".equals(e.getFacebookId())) {
			sendMessage.setText("Questo è un evento generato su facebook. Non è possibile registrarsi a questo evento da qui.");
		} else {
		
			boolean isParticipating = participantService.checkParticipation(callbackQuery.getFrom().getId(), e.getId());
			
			InlineKeyboardMarkup replyMarkup = new InlineKeyboardMarkup();
			List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<List<InlineKeyboardButton>>();
			
			InlineKeyboardButton btPartecipa = null;
			if (isParticipating) {
				btPartecipa = new InlineKeyboardButton("Rimuovi partecipazione");
				btPartecipa.setCallbackData("annulla_partecipazione,"+e.getId());
			} else {
				btPartecipa = new InlineKeyboardButton("Partecipa");
				btPartecipa.setCallbackData("partecipa_evento,"+e.getId());
			}
			keyboardRows.add(Arrays.asList(btPartecipa));
			
			
			replyMarkup.setKeyboard(keyboardRows);
			sendMessage.setReplyMarkup(replyMarkup);
			sendMessage.setText(e.getDescription());
		}
		
		sendMessage.setChatId(String.valueOf(callbackQuery.getFrom().getId()));
		return sendMessage;
	}

	

}
