package it.pincio.telegrambot.dto;

import java.util.Date;

import lombok.Data;

@Data
public class EventDto {

	private Integer id;
	private String title;
	private String description;
	private Date startDate;
	private Integer numberOfParticipants;
}
