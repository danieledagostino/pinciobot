package it.pincio.telegrambot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import it.pincio.persistence.bean.Faq;
import it.pincio.persistence.dao.FaqRepository;

@Service
public class PublicChatService {
	
	@Autowired
	FaqRepository faqRepository;
	
	@Value("${DB_REQ_SCORE}")
	private Float DB_REQ_SCORE;
	
	@Value("${DB_REQ_HINT}")
	private Float DB_REQ_HINT;
	
	public List<Faq> checkQuestion(String textMessage)
	{
		return faqRepository.searchReabilityAnswer(textMessage, DB_REQ_SCORE, DB_REQ_HINT);
	}
	
	
}
