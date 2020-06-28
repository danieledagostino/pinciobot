package it.pincio.persistence.bean;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "manutenzione")
public class Command implements Serializable{

	private static final long serialVersionUID = -6498257175228844443L;

	@Id
	private Integer id;
	
	@Column(name = "nome_comando")
	private String commandName;
	
	@Column(name = "in_manutenzione")
	private String isInMaintenance;
	
	@Column(name = "nome_comando_java")
	private String javaCommandName;
	
	@Column(name = "risposta_privata")
	private String privateAnswer;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "command")
	@JsonIgnore
	private Set<UserCommand> userComands;

	
	public Command() {
		// TODO Auto-generated constructor stub
	}
	
	public Command(Integer id) {
		this.id = id;
	}
	
	public Command(String commandName) {
		this.commandName = commandName;
	}
	
	public Command(Integer id, String commandName) {
		this.id = id;
		this.commandName = commandName;
	}
	
	public String getCommandName() {
		return commandName;
	}

	public void setCommandName(String commandName) {
		this.commandName = commandName;
	}

	public String getIsInMaintenance() {
		return isInMaintenance;
	}

	public void setIsInMaintenance(String isInMaintenance) {
		this.isInMaintenance = isInMaintenance;
	}

	public String getJavaCommandName() {
		return javaCommandName;
	}

	public void setJavaCommandName(String javaCommandName) {
		this.javaCommandName = javaCommandName;
	}

	public String getPrivateAnswer() {
		return privateAnswer;
	}

	public void setPrivateAnswer(String privateAnswer) {
		this.privateAnswer = privateAnswer;
	}
	
}
