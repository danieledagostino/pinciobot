package it.pincio.telegrambot.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import it.pincio.persistence.bean.Event;
import it.pincio.persistence.dao.EventRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EventService {
	
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
			returnMessage = "Si è verificato un errore. Hai invocando il comando /aggiungi_evento ma senza argomenti. Riprova!";
		} else if (step == 0){
			
				if ("".equals(args[0].trim())) {
					returnMessage = "Non hai scritto il titolo. Invoca di nuovo il comando scrivendo /aggiungi_evento spazio titolo";
				} else {
			
					Event event = new Event();
					event.setTitle(args[0].trim());
					event.setOwner(idUser);
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
				
				ZoneId defaultZoneId = ZoneId.systemDefault();
				
				Event event = new Event();
				event.setStep(1);
				event.setOwner(idUser);
				
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
			Event event = new Event();
			event.setStep(2);
			event.setOwner(idUser);
			
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
	
	public List<Event> searchCurrentEvents() {
		return eventRepository.searchCurrentEvents();
	}
	
	public Event findById(Integer id) {
		Optional<Event> e = eventRepository.findById(id);
		return e.get();
	}
}
