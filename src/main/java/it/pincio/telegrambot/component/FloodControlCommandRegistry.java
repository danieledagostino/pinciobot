package it.pincio.telegrambot.component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;

import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.ICommandRegistry;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import it.pincio.telegrambot.command.BotAndCallbackCommand;

public class FloodControlCommandRegistry implements ICommandRegistry {
	private final Map<String, IBotCommand> commandRegistryMap = new HashMap<>();
	private final boolean allowCommandsWithUsername;
	private final String botUsername;
	private BiConsumer<AbsSender, Message> defaultConsumer;
	private final long TIME_TO_WAIT_FOR_PREVENTING_FLOOD = 180; //Represented with seconds
	private Map<String, LocalDateTime> commandFoodControl;

	/**
     * Creates a Command registry
     * @param allowCommandsWithUsername True to allow commands with username, false otherwise
     * @param botUsername Bot username
     */
    public FloodControlCommandRegistry(boolean allowCommandsWithUsername, String botUsername) {
        this.allowCommandsWithUsername = allowCommandsWithUsername;
        this.botUsername = botUsername;
        commandFoodControl = new HashMap<String, LocalDateTime>();
    }

	@Override
	public void registerDefaultAction(BiConsumer<AbsSender, Message> defaultConsumer) {
		this.defaultConsumer = defaultConsumer;
	}

	@Override
	public final boolean register(IBotCommand botCommand) {
		if (commandRegistryMap.containsKey(botCommand.getCommandIdentifier())) {
			return false;
		}
		commandRegistryMap.put(botCommand.getCommandIdentifier(), botCommand);
		return true;
	}

	@Override
	public final Map<IBotCommand, Boolean> registerAll(IBotCommand... botCommands) {
		Map<IBotCommand, Boolean> resultMap = new HashMap<>(botCommands.length);
		for (IBotCommand botCommand : botCommands) {
			resultMap.put(botCommand, register(botCommand));
		}
		return resultMap;
	}

	@Override
	public final boolean deregister(IBotCommand botCommand) {
		if (commandRegistryMap.containsKey(botCommand.getCommandIdentifier())) {
			commandRegistryMap.remove(botCommand.getCommandIdentifier());
			return true;
		}
		return false;
	}

	@Override
	public final Map<IBotCommand, Boolean> deregisterAll(IBotCommand... botCommands) {
		Map<IBotCommand, Boolean> resultMap = new HashMap<>(botCommands.length);
		for (IBotCommand botCommand : botCommands) {
			resultMap.put(botCommand, deregister(botCommand));
		}
		return resultMap;
	}

	@Override
	public final Collection<IBotCommand> getRegisteredCommands() {
		return commandRegistryMap.values();
	}

	@Override
	public final IBotCommand getRegisteredCommand(String commandIdentifier) {
		return commandRegistryMap.get(commandIdentifier);
	}

	/**
	 * Executes a command action if the command is registered.
	 *
	 * @note If the command is not registered and there is a default consumer, that
	 *       action will be performed
	 *
	 * @param absSender absSender
	 * @param message   input message
	 * @return True if a command or default action is executed, false otherwise
	 */
	public final boolean executeCommand(AbsSender absSender, Message message) {
		BotAndCallbackCommand botCommand = null;
		Chat chat = null;
		if (message.hasText()) {
			chat = message.getChat();
			String text = message.getText();
			if (text.startsWith(BotCommand.COMMAND_INIT_CHARACTER)) {
				String commandMessage = text.substring(1);
				String[] commandSplit = commandMessage.split(BotCommand.COMMAND_PARAMETER_SEPARATOR_REGEXP);

				String command = removeUsernameFromCommandIfNeeded(commandSplit[0]);

				if (commandRegistryMap.containsKey(command)) {
					if (chat.isUserChat() || floodManagerPermitThisCommand(command)) {
					
						botCommand = (BotAndCallbackCommand)getRegisteredCommand(command);
						if ((botCommand.isPrivateAnswer() && chat.isUserChat()) || !botCommand.isPrivateAnswer()) {
							String[] parameters = Arrays.copyOfRange(commandSplit, 1, commandSplit.length);
							commandRegistryMap.get(command).processMessage(absSender, message, parameters);
							return true;
						} else {
							return false;
						}
					} else {
						return false;
					}
				} else if (defaultConsumer != null) {
					defaultConsumer.accept(absSender, message);
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean floodManagerPermitThisCommand(String command) {
		LocalDateTime dateTime = null;
		if (commandFoodControl.containsKey(command)) {
			dateTime = commandFoodControl.get(command);
			boolean permitted = dateTime.until(LocalDateTime.now(), ChronoUnit.SECONDS) > TIME_TO_WAIT_FOR_PREVENTING_FLOOD;
			
			if (permitted) {
				commandFoodControl.put(command, LocalDateTime.now());
			}
			
			return permitted;
		} else {
			commandFoodControl.put(command, LocalDateTime.now());
			return true;
		}
		
	}

	/**
	 * if {@link #allowCommandsWithUsername} is enabled, the username of the bot is
	 * removed from the command
	 * 
	 * @param command Command to simplify
	 * @return Simplified command
	 */
	private String removeUsernameFromCommandIfNeeded(String command) {
		if (allowCommandsWithUsername) {
			return command.replaceAll("(?i)@" + Pattern.quote(botUsername), "").trim();
		}
		return command;
	}
}
