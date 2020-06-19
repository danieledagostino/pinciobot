package it.pincio.telegrambot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import it.pincio.persistence.bean.Event;
import it.pincio.persistence.bean.Participant;
import it.pincio.persistence.bean.ParticipantId;
import it.pincio.persistence.dao.ParticipantRepository;
import it.pincio.telegrambot.dto.EventDto;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ParticipantService {

	@Autowired
	ParticipantRepository participantRepository;
	
	public boolean checkParticipation(String user, EventDto e) {
		ParticipantId id = new ParticipantId(user, new Event(e.getId()));
		Optional<Participant> p = participantRepository.findById(id);
		
		return p.isPresent();
		
	}
	
	public boolean checkParticipation(Participant participant) {
		Example<Participant> example = Example.of(participant);
		Optional<Participant> p = participantRepository.findOne(example);
		
		return p.isPresent();
		
	}
	
	public void delete(Participant participant) {
		participantRepository.delete(participant);
	}
	
	public InlineKeyboardMarkup prepareJoinMessage(List<EventDto> events)
	{
		List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<List<InlineKeyboardButton>>();
		
		InlineKeyboardMarkup replyMarkup = new InlineKeyboardMarkup();
		List<InlineKeyboardButton> keyboardButtons = null;
		InlineKeyboardButton inlineKB = null;
		
		for (EventDto e : events) {
			inlineKB = new InlineKeyboardButton(e.getTitle());
			inlineKB.setCallbackData(String.valueOf(e.getId()));
			
			keyboardButtons = new ArrayList<InlineKeyboardButton>();
			keyboardButtons.add(inlineKB);
			keyboardRows.add(keyboardButtons);
		}
		
		replyMarkup.setKeyboard(keyboardRows);
		
		return replyMarkup;
	}
	
	public boolean insert(Participant participant) {
		Participant p = participantRepository.save(participant);
		
		if (p == null) {
			return false;
		}
		
		return true;
	}
}
