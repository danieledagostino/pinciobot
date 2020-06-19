package it.pincio.telegrambot.command;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import it.pincio.persistence.bean.Event;
import it.pincio.persistence.bean.Participant;
import it.pincio.telegrambot.dto.EventDto;
import it.pincio.telegrambot.service.EventService;
import it.pincio.telegrambot.service.ParticipantService;
import it.pincio.telegrambot.utility.TelegramKeyboard;
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
	ParticipantService participantService;

	public AddParticipationCommand() {
		super(COMMAND_IDENTIFIER, COMMAND_DESCRIPTION);
	}

	@Override
	public void execute(AbsSender absSender, User user, Chat chat, Integer messageId, String[] arguments) {

		List<EventDto> events = eventService.searchCurrentEvents();

		if (events.size() > 0) {
			InlineKeyboardMarkup replyMarkup = participantService.prepareJoinMessage(events);

			SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
					.setChatId(chat.getId()).setReplyMarkup(replyMarkup).setReplyToMessageId(messageId)
					.setText("Scegli l'evento al quale partecipare");

			try {
				absSender.execute(message); // Call method to send the message
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public SendMessage processCallback(CallbackQuery callbackQuery, String... args) {
		SendMessage sendMessage = new SendMessage();
		Chat chat = callbackQuery.getMessage().getChat();

		try {
			Participant participant = new Participant();

			participant.setEvent(new Event(Integer.valueOf(args[0])));
			participant.setUser(String.valueOf(callbackQuery.getFrom().getId()));
			
			boolean isPresent = participantService.checkParticipation(participant);

			if (isPresent) {
				sendMessage.setChatId(chat.getId()).setReplyToMessageId(callbackQuery.getMessage().getMessageId());
				sendMessage.setText(messageSource.getMessage("addparticipation.command.text.alreadyparticipating", null, Locale.ITALIAN));
				
				InlineKeyboardMarkup replyMarkup = TelegramKeyboard.makeOneRow("Annulla la partecipazione", 
						"annulla_partecipazione,"+args[0]);
				
				sendMessage.setReplyMarkup(replyMarkup);
			} else {

				participantService.insert(participant);

				sendMessage.setChatId(chat.getId()).setReplyToMessageId(callbackQuery.getMessage().getMessageId());
				sendMessage.setText(messageSource.getMessage("addparticipation.command.text.confirm", null, Locale.ITALIAN));
			}
		} catch (Exception e) {
			log.error("Partecipazione non confermata", e);
			sendMessage.setText("Partecipazione non confermata.\n"
					+ "Puoi gestire tutto comodamente in chat privata col bot. Prova a premere qui @PincioBot");
		}

		return sendMessage;
	}

}
