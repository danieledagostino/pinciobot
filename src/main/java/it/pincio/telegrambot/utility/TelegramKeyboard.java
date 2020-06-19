package it.pincio.telegrambot.utility;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class TelegramKeyboard {

	public static InlineKeyboardMarkup makeOneRow(String text, String callbackData) {
		List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<List<InlineKeyboardButton>>();
		
		InlineKeyboardMarkup replyMarkup = new InlineKeyboardMarkup();
		List<InlineKeyboardButton> keyboardButtons = null;
		InlineKeyboardButton inlineKB = null;
		
		inlineKB = new InlineKeyboardButton(text);
		inlineKB.setCallbackData(callbackData);
		
		keyboardButtons = new ArrayList<InlineKeyboardButton>();
		keyboardButtons.add(inlineKB);
		keyboardRows.add(keyboardButtons);
		
		return replyMarkup;
	}
}
