package it.pincio.telegrambot.command;

import org.telegram.telegrambots.extensions.bots.commandbot.commands.DefaultBotCommand;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.objects.Message;

public abstract class BotAndCallbackCommand extends DefaultBotCommand {
	
	public BotAndCallbackCommand(String commandIdentifier, String description) {
		super(commandIdentifier, description);
	}
	
	public abstract String processCallback(Message repliedMessage, String... args);

}
