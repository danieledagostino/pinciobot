package it.pincio.telegrambot.component;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.CommandRegistry;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.ICommandRegistry;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import org.telegram.telegrambots.util.WebhookUtils;

import it.pincio.telegrambot.command.BotAndCallbackCommand;
import it.pincio.telegrambot.utility.TelegramKeyboard;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class TelegramLongPollingCommandAndCallbackBot extends DefaultAbsSender
		implements ICommandRegistry, LongPollingBot {

	private final String CALLBACKDATA_ARGS_SEPARATOR = ",";

	@Autowired
	protected MessageSource messageSource;

	@Value("${USER_BOT}")
	protected String USER_BOT;

	@Override
	public final void onUpdateReceived(Update update) {
		Integer userId = null;
		Chat chat = null;
		Message message = null;
		BotAndCallbackCommand botCommand = null;
		Integer messageId = null;
		boolean isValidCommand = false;
		if (update.hasMessage()) {
			userId = update.getMessage().getFrom().getId();
			chat = update.getMessage().getChat();
			message = update.getMessage();
			if (message.isCommand() && !filter(message)) {
				if (!commandRegistry.executeCommand(this, message)) {
					// we have received a not registered command, handle it as invalid
					processInvalidCommandUpdate(update);
				} else {
					isValidCommand = true;
					String text = message.getText();
					String commandMessage = text.substring(1);
					String[] commandSplit = commandMessage.split(BotCommand.COMMAND_PARAMETER_SEPARATOR_REGEXP);

					String command = removeUsernameFromCommandIfNeeded(commandSplit[0]);
					botCommand = getRegisteredCommand(command);

					log.info("Invoking command {}", command);
				}
				messageId = message.getMessageId();
			}
		} else if (update.hasCallbackQuery()) {
			CallbackQuery cb = update.getCallbackQuery();
			String[] args = cb.getData().split(CALLBACKDATA_ARGS_SEPARATOR);
			userId = cb.getFrom().getId();
			chat = cb.getMessage().getChat();
			SendMessage sendMessage = null;
			messageId = cb.getMessage().getMessageId();
			try {
				botCommand = getRegisteredCommand(args[0]);
				sendMessage = botCommand.processCallback(cb, args[1]);
				log.info("Callback - Invoking command {}", args[0]);

				execute(sendMessage);
			} catch (NullPointerException e) {
				log.error("Command {} not implemented yet", args[0]);
			} catch (TelegramApiException e) {
				log.error("<sendMessage>");
				log.error("username: {}", cb.getFrom().getUserName());
				log.error(sendMessage.toString());
				log.error("</sendMessage>");
				log.error("TelegramApiException: Message not sent for the callback", e);
			} catch (Exception e) {
				log.error("Generic error during send the message for the callback", e);
			}

		}

		if (isValidCommand || update.hasCallbackQuery()) {
			if (botCommand.isPrivateAnswer() && !chat.getId().equals(new Long(userId))) {
				InlineKeyboardMarkup replyMarkup = TelegramKeyboard.makeOneRowWithLink("Premi qui",
						"https://t.me/" + USER_BOT);
				SendMessage privateMessage = new SendMessage().setChatId(chat.getId()).setReplyMarkup(replyMarkup)
						.setReplyToMessageId(messageId).setText(messageSource.getMessage("command.msg.touser",
								Arrays.asList(botCommand.getCommandIdentifier()).toArray(), Locale.ITALY));
				try {
					execute(privateMessage);
				} catch (TelegramApiException e) {
					log.error("Private message to inform the user not sent", e);
				} catch (Exception e) {
					log.error("Generic error during send the message if the chat comes from public group", e);
				}
			}
			return;
		}

		processNonCommandUpdate(update);
	}

	private String removeUsernameFromCommandIfNeeded(String command) {
		return command.replaceAll("(?i)@" + Pattern.quote(getBotUsername()), "").trim();
	}

	private final FloodControlCommandRegistry commandRegistry;

	/**
	 * Creates a TelegramLongPollingCommandBot using default options Use
	 * ICommandRegistry's methods on this bot to register commands
	 *
	 */
	public TelegramLongPollingCommandAndCallbackBot() {
		this(ApiContext.getInstance(DefaultBotOptions.class));
	}

	/**
	 * Creates a TelegramLongPollingCommandBot with custom options and allowing
	 * commands with usernames Use ICommandRegistry's methods on this bot to
	 * register commands
	 *
	 * @param options Bot options
	 */
	public TelegramLongPollingCommandAndCallbackBot(DefaultBotOptions options) {
		this(options, true);
	}

	/**
	 * Creates a TelegramLongPollingCommandBot Use ICommandRegistry's methods on
	 * this bot to register commands
	 *
	 * @param options                   Bot options
	 * @param allowCommandsWithUsername true to allow commands with parameters
	 *                                  (default), false otherwise
	 */
	public TelegramLongPollingCommandAndCallbackBot(DefaultBotOptions options, boolean allowCommandsWithUsername) {
		super(options);
		this.commandRegistry = new FloodControlCommandRegistry(allowCommandsWithUsername, this.getBotUsername());
	}

	/**
	 * This method is called when user sends a not registered command. By default it
	 * will just call processNonCommandUpdate(), override it in your implementation
	 * if you want your bot to do other things, such as sending an error message
	 *
	 * @param update Received update from Telegram
	 */
	protected void processInvalidCommandUpdate(Update update) {
		processNonCommandUpdate(update);
	}

	/**
	 * Override this function in your bot implementation to filter messages with
	 * commands
	 * <p>
	 * For example, if you want to prevent commands execution incoming from group
	 * chat: # # return !message.getChat().isGroupChat(); #
	 *
	 * @param message Received message
	 * @return true if the message must be ignored by the command bot and treated as
	 *         a non command message, false otherwise
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
		return (BotAndCallbackCommand) commandRegistry.getRegisteredCommand(commandIdentifier);
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
	 *          won't be forwarded to this method <b>if a default action is
	 *          present</b>.
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
