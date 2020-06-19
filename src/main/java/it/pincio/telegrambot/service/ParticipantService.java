package it.pincio.telegrambot.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import it.pincio.persistence.bean.ChatUser;
import it.pincio.persistence.bean.Event;
import it.pincio.persistence.dao.ChatUserRepository;
import it.pincio.telegrambot.dto.EventDto;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ParticipantService {

	@Autowired
	ChatUserRepository chatUserRepository;
	
	public boolean checkParticipation(Integer userId, Integer eventId) {
		ChatUser user = getUser(userId, eventId);
		Example<ChatUser> example = Example.of(user);
		
		Optional<ChatUser> chatUser = chatUserRepository.findOne(example);
		
		return chatUser.isPresent();
		
	}
	
	public void delete(Integer userId, Integer eventId) {
		ChatUser user = getUser(userId, eventId);
		
		chatUserRepository.delete(user);
	}
	
	public InlineKeyboardMarkup prepareJoinMessage(List<EventDto> events, String command)
	{
		List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<List<InlineKeyboardButton>>();
		
		InlineKeyboardMarkup replyMarkup = new InlineKeyboardMarkup();
		List<InlineKeyboardButton> keyboardButtons = null;
		InlineKeyboardButton inlineKB = null;
		
		for (EventDto e : events) {
			inlineKB = new InlineKeyboardButton(e.getTitle());
			inlineKB.setCallbackData(command+","+String.valueOf(e.getId()));
			
			keyboardButtons = new ArrayList<InlineKeyboardButton>();
			keyboardButtons.add(inlineKB);
			keyboardRows.add(keyboardButtons);
		}
		
		replyMarkup.setKeyboard(keyboardRows);
		
		return replyMarkup;
	}
	
	public boolean insert(Integer userId, Integer eventId) {
		ChatUser user = getUser(userId, eventId);
		
		user = chatUserRepository.save(user);
		
		if (user == null) {
			return false;
		}
		
		return true;
	}
	
	private ChatUser getUser(Integer userId, Integer eventId)
	{
		ChatUser user = new ChatUser();
		Set<Event> events = new HashSet<Event>();
		events.add(new Event(eventId));
		user.setEvents(events);
		user.setId(userId);
		
		return user;
	}
}
