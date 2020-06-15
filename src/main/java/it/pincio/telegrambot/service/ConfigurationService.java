package it.pincio.telegrambot.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;

import it.pincio.persistence.bean.Command;
import it.pincio.persistence.dao.CommandRepository;

@Service
public class ConfigurationService {

	@Autowired
	CommandRepository commandRepository;
	
	@Autowired 
	private ApplicationContext applicationContext;

	public List<BotCommand> getAllCommands() throws InstantiationException, IllegalAccessException, ClassNotFoundException {

		List<BotCommand> botCommands = new ArrayList<BotCommand>();

		List<Command> notInMaintenanceCommand = commandRepository.getAllActiveCommand();

		Class<BotCommand> t = null;
		BotCommand botCommand = null;
		
		for (Command command : notInMaintenanceCommand) {

			t = (Class<BotCommand>)Class.forName("it.pincio.telegrambot.command." + command.getJavaCommandName());
			botCommand = applicationContext.getBean(t);
			botCommands.add(botCommand);
		}

		return botCommands;
	}

}
