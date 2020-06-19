package it.pincio.telegrambot.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;

import it.pincio.persistence.bean.Command;
import it.pincio.persistence.dao.CommandRepository;
import it.pincio.telegrambot.command.BotAndCallbackCommand;

@Service
public class ConfigurationService {

	@Autowired
	CommandRepository commandRepository;
	
	@Autowired 
	private ApplicationContext applicationContext;

	public List<BotAndCallbackCommand> getAllCommands() throws InstantiationException, IllegalAccessException, ClassNotFoundException {

		List<BotAndCallbackCommand> botCommands = new ArrayList<BotAndCallbackCommand>();

		Command command = new Command();
		command.setIsInMaintenance("N");
		Example<Command> example = Example.of(command);
		List<Command> notInMaintenanceCommand = commandRepository.findAll(example);

		Class<BotAndCallbackCommand> t = null;
		BotAndCallbackCommand botCommand = null;
		
		for (Command c : notInMaintenanceCommand) {

			t = (Class<BotAndCallbackCommand>)Class.forName("it.pincio.telegrambot.command." + c.getJavaCommandName());
			botCommand = applicationContext.getBean(t);
			botCommand.setIsPrivateAnswer(c.getPrivateAnswer().equals("Y"));
			botCommands.add(botCommand);
		}

		return botCommands;
	}

}
