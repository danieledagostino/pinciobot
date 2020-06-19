package it.pincio.persistence.bean;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Embeddable
public class ParticipantId implements Serializable{

	private static final long serialVersionUID = -8566032946826250018L;

	private String user;
	private Event event;

	public ParticipantId() {
	}

	public ParticipantId(String user, Event event) {
		super();
		this.user = user;
		this.event = event;
	}
	
}
