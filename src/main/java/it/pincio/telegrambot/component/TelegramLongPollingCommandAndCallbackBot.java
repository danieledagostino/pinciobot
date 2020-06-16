package it.pincio.telegrambot.component;

import java.util.Collection;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;

import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.CommandRegistry;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.ICommandRegistry;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import org.telegram.telegrambots.util.WebhookUtils;

import it.pincio.telegrambot.command.BotAndCallbackCommand;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class TelegramLongPollingCommandAndCallbackBot extends DefaultAbsSender implements ICommandRegistry, LongPollingBot {

	private final String CALLBACKDATA_ARGS_SEPARATOR = ",";
	
	@Override
    public final void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.isCommand() && !filter(message)) {
                if (!commandRegistry.executeCommand(this, message)) {
                    //we have received a not registered command, handle it as invalid
                    processInvalidCommandUpdate(update);
                }
                return;
            }
        } else if (update.hasCallbackQuery()) {
			CallbackQuery cb = update.getCallbackQuery();
			String[] args = cb.getData().split(CALLBACKDATA_ARGS_SEPARATOR);
			
			BotAndCallbackCommand botCommand = getRegisteredCommand(args[0]);
			SendMessage sendMessage = botCommand.processCallback(cb, args[1]);
			
			
			//TODO If message sent from group, the bot should send a message to the user to inform him to switch on the private chat
			try {
				execute(sendMessage);
			} catch (TelegramApiException e) {
				log.error("Message not sent", e);
			}
            return;
		}
        processNonCommandUpdate(update);
    }
	
	private String removeUsernameFromCommandIfNeeded(String command) {
		return command.replaceAll("(?i)@" + Pattern.quote(getBotUsername()), "").trim();
    }
	
	private final CommandRegistry commandRegistry;

    /**
     * Creates a TelegramLongPollingCommandBot using default options
     * Use ICommandRegistry's methods on this bot to register commands
     *
     */
    public TelegramLongPollingCommandAndCallbackBot() {
        this(ApiContext.getInstance(DefaultBotOptions.class));
    }

    /**
     * Creates a TelegramLongPollingCommandBot with custom options and allowing commands with
     * usernames
     * Use ICommandRegistry's methods on this bot to register commands
     *
     * @param options     Bot options
     */
    public TelegramLongPollingCommandAndCallbackBot(DefaultBotOptions options) {
        this(options, true);
    }

    /**
     * Creates a TelegramLongPollingCommandBot
     * Use ICommandRegistry's methods on this bot to register commands
     *
     * @param options                   Bot options
     * @param allowCommandsWithUsername true to allow commands with parameters (default),
     *                                  false otherwise
     */
    public TelegramLongPollingCommandAndCallbackBot(DefaultBotOptions options, boolean allowCommandsWithUsername) {
        super(options);
        this.commandRegistry = new CommandRegistry(allowCommandsWithUsername, this.getBotUsername());
    }

    /**
     * This method is called when user sends a not registered command. By default it will just call processNonCommandUpdate(),
     * override it in your implementation if you want your bot to do other things, such as sending an error message
     *
     * @param update Received update from Telegram
     */
    protected void processInvalidCommandUpdate(Update update) {
        processNonCommandUpdate(update);
    }


    /**
     * Override this function in your bot implementation to filter messages with commands
     * <p>
     * For example, if you want to prevent commands execution incoming from group chat:
     * #
     * # return !message.getChat().isGroupChat();
     * #
     *
     * @param message Received message
     * @return true if the message must be ignored by the command bot and treated as a non command message,
     * false otherwise
     * @note Default implementation doesn't filter anything
     */
    protected boolean filter(Message message) {
        return false;
    }

    @Override
    public final boolean register(IBotCommand botCommand) {
        return commandRegistry.register(botCommand);
    }

    @Override
    public final Map<IBotCommand, Boolean> registerAll(IBotCommand... botCommands) {
        return commandRegistry.registerAll(botCommands);
    }

    @Override
    public final boolean deregister(IBotCommand botCommand) {
        return commandRegistry.deregister(botCommand);
    }

    @Override
    public final Map<IBotCommand, Boolean> deregisterAll(IBotCommand... botCommands) {
        return commandRegistry.deregisterAll(botCommands);
    }

    @Override
    public final Collection<IBotCommand> getRegisteredCommands() {
        return commandRegistry.getRegisteredCommands();
    }

    @Override
    public void registerDefaultAction(BiConsumer<AbsSender, Message> defaultConsumer) {
        commandRegistry.registerDefaultAction(defaultConsumer);
    }

    @Override
    public final BotAndCallbackCommand getRegisteredCommand(String commandIdentifier) {
        return (BotAndCallbackCommand)commandRegistry.getRegisteredCommand(commandIdentifier);
    }

    /**
     * @return Bot username
     */
    @Override
    public abstract String getBotUsername();

    /**
     * Process all updates, that are not commands.
     *
     * @param update the update
     * @warning Commands that have valid syntax but are not registered on this bot,
     * won't be forwarded to this method <b>if a default action is present</b>.
     */
    public abstract void processNonCommandUpdate(Update update);
    
    @Override
	public void clearWebhook() throws TelegramApiRequestException {
		WebhookUtils.clearWebhook(this);
	}

	@Override
	public void onClosing() {
		exe.shutdown();
	}
}
