package it.pincio.telegrambot.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.pincio.persistence.bean.Event;
import it.pincio.persistence.bean.Partecipant;
import it.pincio.persistence.bean.PartecipantId;
import it.pincio.persistence.dao.PartecipantRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PartecipantService {

	@Autowired
	PartecipantRepository partecipantRepository;
	
	public boolean checkParticipation(String user, Event e) {
		PartecipantId id = new PartecipantId(user, e);
		Optional<Partecipant> p = partecipantRepository.findById(id);
		
		return p.isPresent();
		
	}
}
