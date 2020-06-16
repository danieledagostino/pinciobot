package it.pincio.telegrambot.command;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.DefaultBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import it.pincio.persistence.bean.Event;
import it.pincio.persistence.bean.Faq;
import it.pincio.persistence.bean.Partecipant;
import it.pincio.persistence.bean.PartecipantId;
import it.pincio.telegrambot.service.AddParticipationService;
import it.pincio.telegrambot.service.EventService;
import it.pincio.telegrambot.service.FaqService;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FaqDettaglioCommand extends BotAndCallbackCommand {
	
	private static final String COMMAND_IDENTIFIER = "faq_dettaglio";
	private static final String COMMAND_DESCRIPTION = "shows all commands. Use /help [command] for more info";
	private static final String EXTENDED_DESCRIPTION = "This command displays all commands the bot has to offer.\n /help [command] can display deeper information";
	
	@Autowired
	FaqService faqService;
	
	public FaqDettaglioCommand() {
		super(COMMAND_IDENTIFIER, COMMAND_DESCRIPTION);
	}

	@Override
	public void execute(AbsSender absSender, User user, Chat chat, Integer messageId, String[] arguments) {
		
		//Faq faq = faqService.getFaqById(Integer.valueOf(arguments[0]));
	}

	@Override
	public SendMessage processCallback(CallbackQuery callbackQuery, String... args) {
		SendMessage sendMessage = null;
		try {
			if ("0".equals(args[0])) {
				
			} else {
				sendMessage = faqService.getFaqById(Integer.valueOf(args[0]));
				
				sendMessage.setChatId(String.valueOf(callbackQuery.getFrom().getId()));					
			}
		} catch (Exception e) {
			log.error("Partecipazione non confermata", e);
			sendMessage.setText("Partecipazione non confermata.\n"+
				"Puoi gestire tutto comodamente in chat privata col bot. Prova a premere qui @PincioBot");
		}
		
		return sendMessage;
	}

	

}
