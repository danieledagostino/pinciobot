package it.pincio.telegrambot.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.DefaultBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public abstract class BotAndCallbackCommand extends DefaultBotCommand {
	
	@Autowired
	protected MessageSource messageSource;
	
	@Value("${USER_BOT}")
	protected String USER_BOT;
	
	public BotAndCallbackCommand(String commandIdentifier, String description) {
		super(commandIdentifier, description);
	}
	
	public abstract SendMessage processCallback(CallbackQuery callbackQuery, String... args);

}
