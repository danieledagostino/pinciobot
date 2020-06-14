package it.pincio.telegrambot.service;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import it.pincio.persistence.bean.Faq;
import it.pincio.persistence.dao.FaqRepository;
import it.pincio.telegrambot.utility.NormalizeText;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PublicChatService {
	
	@Autowired
	FaqRepository faqRepository;
	
	@Value("${DB_REQ_SCORE}")
	private BigDecimal DB_REQ_SCORE;
	
	@Value("${DB_REQ_HINT}")
	private BigDecimal DB_REQ_HINT;
	
	public List<Faq> checkQuestion(String textMessage)
	{
		try {
			textMessage = NormalizeText.execute(textMessage);
		} catch (FileNotFoundException e) {
			log.error("Generic error: stopwords.txt not found");
		}
		return faqRepository.searchReabilityAnswer(textMessage, DB_REQ_SCORE, DB_REQ_HINT);
	}
	
	
}
