package it.pincio.telegrambot.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import com.vdurmont.emoji.EmojiParser;

import it.pincio.persistence.bean.ChatUser;
import it.pincio.persistence.bean.Event;
import it.pincio.persistence.dao.ChatUserRepository;
import it.pincio.persistence.dao.EventRepository;
import it.pincio.telegrambot.dto.EventDto;
import it.pincio.telegrambot.utility.EmojiiCode;
import it.pincio.telegrambot.utility.TelegramKeyboard;
import it.pincio.telegrambot.utility.Utility;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EventService {
	
	@Autowired
	MessageSource messageSource;
	
	// https://codepoints.net/
	private String patternDate = "@(2020)([0-1][0-9])([0-3][0-9])([0-2][0-9])([0-5][0-9])@";
	
	@Autowired
	EventRepository eventRepository;
	
	@Autowired
	private ChatUserRepository chatUserRepository;
	
	public String processRequest(String idUser, String[] args)
	{
		List<Event> events = eventRepository.searchByCurrentDate(idUser);
		
		Integer step = 0;
		
		String returnMessage = "";
		
		if (events.size() > 1)
		{
			step = events.get(0).getStep();
		}
		
		if ((step == 1 || step == 2 || step == 3) && args.length == 0) {
			returnMessage = messageSource.getMessage("event.service.add.error", null, Locale.ITALY);
		} else if (step == 0){
			
				if (args.length == 0) {
					returnMessage = "Non hai scritto il titolo. Invoca di nuovo il comando scrivendo /aggiungi_evento spazio titolo";
				} else {
			
					ChatUser owner = chatUserRepository.getOne(idUser);
					Event event = new Event();
					event.setTitle(args[0].trim());
					event.setOwner(owner);
					eventRepository.save(event);
	
					returnMessage = "Bene, hai inserito correttamente il titolo.\n"+
								"Scrivi ora la data dell'evento invocando di nuovo il comando /aggiungi_evento spazio data.\n"+
								"NOTA BENE: La data deve avere il seguente formato: yyyyMMddHHmm\n"+
								"Esempio 202006101930 -> anno 2020, mese 06, giorno 10, ora 19, minuti 30";
				}
		} else if (step == 1) {
			Pattern pattern = Pattern.compile(patternDate);
			String[] dateMatch = pattern.split(args[0].trim());
			
			if (dateMatch.length == 5) {
				
				ChatUser owner = chatUserRepository.getOne(idUser);
				ZoneId defaultZoneId = ZoneId.systemDefault();
				
				Event event = new Event();
				event.setStep(1);
				event.setOwner(owner);
				
				Example<Event> example = Example.of(event);
				Optional<Event> optEvent = eventRepository.findOne(example);
				event = optEvent.get();
				LocalDate localDate = LocalDate.of(Integer.valueOf(dateMatch[0]), Month.valueOf(dateMatch[1]), Integer.valueOf(dateMatch[2]));
				localDate.atTime(LocalTime.of(Integer.valueOf(dateMatch[4]), Integer.valueOf(dateMatch[5])));
				Date date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
				event.setStartDate(date);
				
				eventRepository.save(event);
				
				returnMessage = "Bene, hai inserito correttamente la data.\n"+
								"Scrivi ora la descrizione di questo evento invocando di nuovo il comando /aggiungi_evento spazio descrizione.\n";
			}else{
				returnMessage = "Non hai scritto correttamente la data. Ricorda! La data deve avere il seguente formato: yyyyMMddHHmm\n"+
								"Esempio 202006101930 -> anno 2020, mese 06, giorno 10, ora 19, minuti 30";
			
			}
		} else if (step == 2) {
			ChatUser owner = chatUserRepository.getOne(idUser);
			Event event = new Event();
			event.setStep(2);
			event.setOwner(owner);
			
			Example<Event> example = Example.of(event);
			Optional<Event> optEvent = eventRepository.findOne(example);
			event = optEvent.get();
			event.setDescription(args[0].trim());
			
			eventRepository.save(event);
			//returnMessage = "E' stato aggiunto un nuovo evento. Controlla lanciando il comando /lista_eventi";
			//sendMessage("sendMessage", $parameters);
			
			returnMessage = "Complimenti, hai creato il tuo evento per la chat 'Pattinatori del Pincio'.\n"+
							"Ora puoi controllare la presenza del tuo evento appena creato, semplicemente lanciando il comando "+
							"/lista_eventi anche da qui.";
		}
		
		return returnMessage;
	}
	
	public Event generateEmptyEvent(Integer userId) {
		
		String token = Utility.randomAlphaNumeric(20);
		
		Event event = new Event();
		event.setOwner(new ChatUser(userId));
		event.setToken(token);
		event.setCancelled("Y");
		
		try {
			event = eventRepository.save(event);
			return event;
		} catch (Exception e) {
			log.error("Error during creation of a new empy event", e);
			return null;
		}
		
	}
	
	@Transactional
	public List<EventDto> searchCurrentEvents() {
		return toDto(eventRepository.searchCurrentEvents());
	}
	
	public InlineKeyboardMarkup searchMyEvents(Integer userId){
		List<Event> oldEvents = eventRepository.searchMyOldEvents(String.valueOf(userId));
		List<Event> confirmedEvents = eventRepository.searchMyConfirmedEvents(String.valueOf(userId));
		List<Event> uncompleteEvents = eventRepository.searchMyUncompleteEvents(String.valueOf(userId));
		
		List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<List<InlineKeyboardButton>>();
		
		InlineKeyboardMarkup replyMarkup = new InlineKeyboardMarkup();
		List<InlineKeyboardButton> keyboardButtons = null;
		InlineKeyboardButton inlineKB = null;
		
		for (Event e : oldEvents) {
			inlineKB = new InlineKeyboardButton(EmojiiCode.VICTORY_ICON+e.getTitle());
			inlineKB.setCallbackData("miei_eventi,"+e.getId());
			
			keyboardButtons = new ArrayList<InlineKeyboardButton>();
			keyboardButtons.add(inlineKB);
			keyboardRows.add(keyboardButtons);
		}
		
		for (Event e : confirmedEvents) {
			inlineKB = new InlineKeyboardButton(EmojiiCode.WHITE_HEAVY_CHECK_MARK_ICON+e.getTitle());
			inlineKB.setCallbackData("miei_eventi,"+e.getId());
			
			keyboardButtons = new ArrayList<InlineKeyboardButton>();
			keyboardButtons.add(inlineKB);
			keyboardRows.add(keyboardButtons);
		}
		
		for (Event e : uncompleteEvents) {
			inlineKB = new InlineKeyboardButton(EmojiiCode.HEAVY_EXCLAMATION_MARK_ICON+e.getTitle());
			inlineKB.setCallbackData("miei_eventi,"+e.getId());
			
			keyboardButtons = new ArrayList<InlineKeyboardButton>();
			keyboardButtons.add(inlineKB);
			keyboardRows.add(keyboardButtons);
		}
		
		replyMarkup.setKeyboard(keyboardRows);
		
		return replyMarkup;
	}
	
	@Transactional
	public EventDto findById(Integer id) {
		Optional<Event> e = eventRepository.findById(id);
		return toDto(e.get());
	}
	
	@Transactional
	public EventDto searchNextEvent() throws Exception {
		Page<Event> page = eventRepository.searchNextEvent(PageRequest.of(0, 1));
		
		return toDto(page);
	}
	
	public InlineKeyboardMarkup prepareJoinMessage(List<EventDto> events)
	{
		List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<List<InlineKeyboardButton>>();
		
		InlineKeyboardMarkup replyMarkup = new InlineKeyboardMarkup();
		List<InlineKeyboardButton> keyboardButtons = null;
		InlineKeyboardButton inlineKB = null;
		
		String label = messageSource.getMessage("eventlist.service.participant.label", null, Locale.ITALY);
		
		String eventTitle = null;
		String numberOfParticipants = null;
		
		for (EventDto e : events) {
			if (e.getFacebookId() == null) {
				eventTitle = EmojiParser.parseToUnicode(":ticket:"+" "+e.getTitle());
			} else {
				eventTitle = EmojiParser.parseToUnicode(":blue_book:"+" "+e.getTitle());
			}
			numberOfParticipants = EmojiParser.parseToUnicode(":runner:"+" "+e.getNumberOfParticipants());
			inlineKB = new InlineKeyboardButton(eventTitle+" "+numberOfParticipants);
			inlineKB.setCallbackData("lista_eventi,"+e.getId());
			
			keyboardButtons = new ArrayList<InlineKeyboardButton>();
			keyboardButtons.add(inlineKB);
			keyboardRows.add(keyboardButtons);
		}
		
		replyMarkup.setKeyboard(keyboardRows);
		
		return replyMarkup;
	}
	
	private EventDto toDto(Event e) {
		EventDto dto = new EventDto();
		
		dto.setId(e.getId());
		dto.setTitle(e.getTitle());
		dto.setDescription(e.getDescription());
		dto.setStartDate(e.getStartDate());
		dto.setNumberOfParticipants(e.getParticipants().size());
		dto.setFacebookId(e.getFacebookId());
		
		return dto;
	}
	
	private EventDto toDto(Page<Event> page) {
		Event e = page.getContent().get(0);
		return toDto(e);
	}
	
	private List<EventDto> toDto(List<Event> list) {
		List<EventDto> dtoList = new ArrayList<EventDto>();
		
		for (Event e : list) {
			dtoList.add(toDto(e));
		}
		
		return dtoList;
	}
}
