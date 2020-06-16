package it.pincio.telegrambot.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import it.pincio.persistence.bean.Event;
import it.pincio.persistence.bean.Faq;
import it.pincio.persistence.bean.Partecipant;
import it.pincio.persistence.dao.FaqRepository;
import it.pincio.telegrambot.utility.EmojiiCode;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FaqService {
	
	@Autowired
	FaqRepository faqRepository;
	
	public SendMessage getFaqById(Integer id) {
		Faq faq = faqRepository.findById(id).get();
		
		List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<List<InlineKeyboardButton>>();
		
		InlineKeyboardMarkup replyMarkup = new InlineKeyboardMarkup();
		List<InlineKeyboardButton> keyboardButtons = null;
		InlineKeyboardButton inlineKB = null;
		{
			inlineKB = new InlineKeyboardButton(EmojiiCode.PENSIVE_FACE_ICON+"Questa risposta mi è stata utile!");
			inlineKB.setCallbackData("conferma_risposta,"+faq.getId());
			
			keyboardButtons = new ArrayList<InlineKeyboardButton>();
			keyboardButtons.add(inlineKB);
			keyboardRows.add(keyboardButtons);
		}
		{
			inlineKB = new InlineKeyboardButton(EmojiiCode.WINKING_FACE_ICON+"Questa risposta non è stata utile!");
			inlineKB.setCallbackData("conferma_risposta,"+faq.getId());
			
			keyboardButtons = new ArrayList<InlineKeyboardButton>();
			keyboardButtons.add(inlineKB);
			keyboardRows.add(keyboardButtons);
		}
		
		replyMarkup.setKeyboard(keyboardRows);
		
		return new SendMessage().setReplyMarkup(replyMarkup).setText(faq.getAnswer());
	}
	
}
