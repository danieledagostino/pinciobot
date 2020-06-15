package it.pincio.persistence.bean;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "partecipanti")
@Data
public class Partecipant implements Serializable {

	private static final long serialVersionUID = 8698575360282095037L;
	
	@EmbeddedId
	private PartecipantId partecipantId;
	
}
