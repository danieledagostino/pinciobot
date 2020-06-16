package it.pincio.persistence.bean;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Embeddable
public class PartecipantId implements Serializable{

	private static final long serialVersionUID = -8566032946826250018L;

	private String user;
	
	@JoinColumn(name = "id_evento")
	@ManyToOne(fetch = FetchType.LAZY)
	private Event event;

	public PartecipantId(String user, Event event) {
		super();
		this.user = user;
		this.event = event;
	}
	
	public PartecipantId() {
	}
	
}
