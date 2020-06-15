package it.pincio.telegrambot.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import it.pincio.persistence.bean.Event;
import it.pincio.persistence.bean.Faq;
import it.pincio.persistence.bean.Partecipant;
import it.pincio.persistence.dao.PartecipantRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AddParticipationService {
	
	@Autowired
	PartecipantRepository partecipantRepository;
	
	public InlineKeyboardMarkup prepareJoinMessage(List<Event> events)
	{
		List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<List<InlineKeyboardButton>>();
		
		InlineKeyboardMarkup replyMarkup = new InlineKeyboardMarkup();
		List<InlineKeyboardButton> keyboardButtons = null;
		InlineKeyboardButton inlineKB = null;
		
		for (Event e : events) {
			inlineKB = new InlineKeyboardButton(e.getTitle());
			inlineKB.setCallbackData(String.valueOf(e.getId()));
			
			keyboardButtons = new ArrayList<InlineKeyboardButton>();
			keyboardButtons.add(inlineKB);
			keyboardRows.add(keyboardButtons);
		}
		
		replyMarkup.setKeyboard(keyboardRows);
		
		return replyMarkup;
	}
	
	public boolean insert(Partecipant partecipant) {
		Partecipant p = partecipantRepository.save(partecipant);
		
		if (p == null) {
			return false;
		}
		
		return true;
	}
	
}
