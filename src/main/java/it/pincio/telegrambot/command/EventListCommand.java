package it.pincio.telegrambot.command;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.DefaultBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class EventListCommand extends DefaultBotCommand {
	
	private static final String COMMAND_IDENTIFIER = "lista_eventi";
	private static final String COMMAND_DESCRIPTION = "shows all commands. Use /help [command] for more info";
	private static final String EXTENDED_DESCRIPTION = "This command displays all commands the bot has to offer.\n /help [command] can display deeper information";
	
	public EventListCommand() {
		super(COMMAND_IDENTIFIER, COMMAND_DESCRIPTION);
	}

	@Override
	public void execute(AbsSender absSender, User user, Chat chat, Integer messageId, String[] arguments) {
		// TODO Auto-generated method stub
		
		SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                .setChatId(chat.getId())
                .setReplyToMessageId(messageId)
                .setText("");
        try {
        	absSender.execute(message); // Call method to send the message
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
	}

	

}
