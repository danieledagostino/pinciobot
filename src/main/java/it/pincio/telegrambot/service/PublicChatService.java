package it.pincio.telegrambot.service;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

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
	
	public InlineKeyboardMarkup prepareFaqResponse(List<Faq> listFaq) {
		InlineKeyboardMarkup replyMarkup = new InlineKeyboardMarkup();
		List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<List<InlineKeyboardButton>>();
		List<InlineKeyboardButton> keyboardButtons = null;
		InlineKeyboardButton inlineKB = null;
		
		for (Faq faq : listFaq) {
			inlineKB = new InlineKeyboardButton(faq.getHint());
			inlineKB.setCallbackData(String.valueOf(faq.getId()));
			
			keyboardButtons = new ArrayList<InlineKeyboardButton>();
			keyboardButtons.add(inlineKB);
			keyboardRows.add(keyboardButtons);
		}
		
		replyMarkup.setKeyboard(keyboardRows);
		
		return replyMarkup;
	}
	
}
