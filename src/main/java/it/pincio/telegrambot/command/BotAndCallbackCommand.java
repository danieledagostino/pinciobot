package it.pincio.telegrambot.command;

import org.telegram.telegrambots.extensions.bots.commandbot.commands.DefaultBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public abstract class BotAndCallbackCommand extends DefaultBotCommand {
	
	public BotAndCallbackCommand(String commandIdentifier, String description) {
		super(commandIdentifier, description);
	}
	
	public abstract SendMessage processCallback(CallbackQuery callbackQuery, String... args);

}
