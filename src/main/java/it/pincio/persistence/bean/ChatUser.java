package it.pincio.persistence.bean;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Table(name = "utenti_chat")
public class ChatUser implements Serializable {

	private static final long serialVersionUID = 4575406331770542999L;

	@Id
	private Integer id;
	
	private String username;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			  name = "partecipanti", 
			  joinColumns = @JoinColumn(name = "user"), 
			  inverseJoinColumns = @JoinColumn(name = "id_evento"))
	@JsonIgnore
	private Set<Event> events;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "chatUser")
	@JsonIgnore
	private Set<UserCommand> commands;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Set<Event> getEvents() {
		return events;
	}

	public void setEvents(Set<Event> events) {
		this.events = events;
	}

	public Set<UserCommand> getCommands() {
		return commands;
	}

	public void setCommands(Set<UserCommand> commands) {
		this.commands = commands;
	}
}
