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
import it.pincio.persistence.dao.FaqRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FaqService {
	
	@Autowired
	FaqRepository faqRepository;
	
	public Faq getFaqById(Integer id) {
		return faqRepository.findById(id).get();
	}
	
}
