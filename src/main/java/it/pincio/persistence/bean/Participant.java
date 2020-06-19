package it.pincio.persistence.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "partecipanti")
@IdClass(ParticipantId.class)
//to prevent hibernate proxy list getter when joined table, remove lombok and implements getter and setter apart
public class Participant implements Serializable {

	private static final long serialVersionUID = 8698575360282095037L;
	
	@Id
	private String user;
	
	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(referencedColumnName = "id", name = "id_evento")
	private Event event;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}
}
