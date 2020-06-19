package it.pincio.telegrambot.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import com.vdurmont.emoji.EmojiParser;

import it.pincio.persistence.bean.Faq;
import it.pincio.persistence.dao.FaqRepository;
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
			String WINKING_FACE_ICON = EmojiParser.parseToUnicode("&#128521; Questa risposta mi è stata utile!");
			inlineKB = new InlineKeyboardButton(WINKING_FACE_ICON);
			inlineKB.setCallbackData("conferma_risposta,"+faq.getId());
			
			keyboardButtons = new ArrayList<InlineKeyboardButton>();
			keyboardButtons.add(inlineKB);
			keyboardRows.add(keyboardButtons);
		}
		{
			String PENSIVE_FACE_ICON = EmojiParser.parseToUnicode("&#128532; Questa risposta non è stata utile!");
			inlineKB = new InlineKeyboardButton(PENSIVE_FACE_ICON);
			inlineKB.setCallbackData("conferma_risposta,"+faq.getId());
			
			keyboardButtons = new ArrayList<InlineKeyboardButton>();
			keyboardButtons.add(inlineKB);
			keyboardRows.add(keyboardButtons);
		}
		
		replyMarkup.setKeyboard(keyboardRows);
		
		return new SendMessage().setReplyMarkup(replyMarkup).setText(faq.getAnswer());
	}
	
}
