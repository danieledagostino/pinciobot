package it.pincio.persistence.bean;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Embeddable
public class PartecipantId implements Serializable{

	private static final long serialVersionUID = -8566032946826250018L;

	private String user;
	
	@JoinColumn(referencedColumnName = "id", name = "id_evento")
	@ManyToOne
	private Event event;
	
}
