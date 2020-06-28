package it.pincio.telegrambot.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import it.pincio.persistence.bean.Command;
import it.pincio.persistence.bean.UserCommand;
import it.pincio.persistence.dao.CommandRepository;
import it.pincio.persistence.dao.UserComandRepository;
import it.pincio.telegrambot.command.BotAndCallbackCommand;

@Service
public class ConfigurationService {

	@Autowired
	private CommandRepository commandRepository;
	
	@Autowired
	private UserComandRepository  userComandRepository;
	
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

	public List<UserCommand> searchLastCommandCalled(String commandName) {
		
		Command command = new Command(commandName);
		
		UserCommand userCommand = new UserCommand();
		userCommand.setCommand(command);
		
		//searching for a UserComand association that has The command with the given command name and
		//the insert date lower or equal to today time minus 1 hour.
		//if results arise it means that 1 hour is passed by from the last given command call
		Specification<UserCommand> example = new Specification() {

			@Override
			public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder builder) {
				
				Date todayMinus1Hour = Date.from(LocalDateTime.now().minusHours(1).atZone(ZoneId.systemDefault()).toInstant());

				return builder.lessThanOrEqualTo(root.<String>get("insertDate"), todayMinus1Hour);
			}
			
		};
		List<UserCommand> userCommands = userComandRepository.findAll(example);
		
		return userCommands;
	}
}
