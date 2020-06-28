package it.pincio.persistence.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "comando_utente")
public class UserCommand implements Serializable {
	
	private static final long serialVersionUID = 8656023480220750867L;

	@Id
	private Integer id;
	
	@JoinColumn(name = "id_chat_user", referencedColumnName = "id")
	@ManyToOne(fetch = FetchType.LAZY)
	private ChatUser chatUser;
	
	@JoinColumn(name = "id_comando", referencedColumnName = "id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Command command;
	
	@DateTimeFormat
	@Column(name = "data_insrimento")
	private Date insertDate;
	
	@Column(name = "messaggio_in")
	private String messageIn;
	
	@Column(name = "messaggio_out")
	private String messageOut;
	
	@Column(name = "eccezione") 
	private String exception;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getInsertDate() {
		return insertDate;
	}

	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}

	public String getMessageIn() {
		return messageIn;
	}

	public void setMessageIn(String messageIn) {
		this.messageIn = messageIn;
	}

	public String getMessageOut() {
		return messageOut;
	}

	public void setMessageOut(String messageOut) {
		this.messageOut = messageOut;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public ChatUser getChatUser() {
		return chatUser;
	}

	public void setChatUser(ChatUser chatUser) {
		this.chatUser = chatUser;
	}

	public Command getCommand() {
		return command;
	}

	public void setCommand(Command command) {
		this.command = command;
	}

}
