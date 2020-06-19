package it.pincio.persistence.bean;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "utenti_chat")
@Data
public class ChatUser {

	@Id
	private Integer id;
	
	private String username;
	
	@ManyToMany(mappedBy = "participants")
	private Set<Event> events;
	
}
