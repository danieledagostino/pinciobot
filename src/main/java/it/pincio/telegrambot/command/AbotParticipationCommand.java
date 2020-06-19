package it.pincio.telegrambot.command;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.DefaultBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import it.pincio.persistence.bean.Event;
import it.pincio.persistence.bean.Participant;
import it.pincio.telegrambot.service.EventService;
import it.pincio.telegrambot.service.ParticipantService;

@Component
public class AbotParticipationCommand extends BotAndCallbackCommand {
	
	private static final String COMMAND_IDENTIFIER = "annulla_partecipazione";
	private static final String COMMAND_DESCRIPTION = "shows all commands. Use /help [command] for more info";
	private static final String EXTENDED_DESCRIPTION = "This command displays all commands the bot has to offer.\n /help [command] can display deeper information";
	
	@Autowired
	ParticipantService participantService;
	
	public AbotParticipationCommand() {
		super(COMMAND_IDENTIFIER, COMMAND_DESCRIPTION);
	}

	@Override
	public void execute(AbsSender absSender, User user, Chat chat, Integer messageId, String[] arguments) {
		SendMessage message = process(user.getId(), arguments[0]);
		
		message.setChatId(chat.getId());
		message.setReplyToMessageId(messageId);
		
        try {
        	absSender.execute(message); // Call method to send the message
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
	}

	@Override
	public SendMessage processCallback(CallbackQuery callbackQuery, String... args) {
		Chat chat = callbackQuery.getMessage().getChat();
		SendMessage message = process(callbackQuery.getFrom().getId(), args[0]);
		message.setChatId(chat.getId());
		return message;
	}

	private SendMessage process(Integer userId, String eventId) {
		Participant participant = new Participant();

		participant.setEvent(new Event(Integer.valueOf(eventId)));
		participant.setUser(String.valueOf(userId));
		
		participantService.delete(participant);
		
		SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                .setText(messageSource.getMessage("abortparticipation.command.text.confirm", null, Locale.ITALIAN));
		
		return message;
	}

}
