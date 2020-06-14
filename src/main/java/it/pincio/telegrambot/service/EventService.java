package it.pincio.telegrambot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.pincio.persistence.bean.Event;
import it.pincio.persistence.bean.Faq;
import it.pincio.persistence.dao.EventRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EventService {
	
	private String patternTitle = "@[a-zA-Z]+(?:_[a-zA-Z]+)+@";
	private String patternDate = "@(2020)([0-1][0-9])([0-3][0-9])([0-2][0-9])([0-5][0-9])@";
	
	@Autowired
	EventRepository eventRepository;
	
	public String processRequest(String idUser, String[] args)
	{
		List<Event> events = eventRepository.searchByCurrentDate(idUser);
		
		Integer step = 0;
		
		String returnMessage = "";
		
		if (events.size() > 1)
		{
			step = events.get(0).getStep();
		}
		
		if ((step == 1 || step == 2 || step == 3) && "".equals(args[0].trim())) {
			returnMessage = "Si è verificato un errore. Hai risposto ad un messaggio del bot ma senza argomenti. Riprova!";
		} else if (step == 0){
			
		}
		
		return returnMessage;
	}
	
	
}
