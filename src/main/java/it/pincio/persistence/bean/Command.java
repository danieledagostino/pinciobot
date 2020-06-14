package it.pincio.persistence.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "manutenzione")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Command {

	
	@Id
	@Column(name = "nome_comando")
	private String commandName;
	
	@Column(name = "in_manutenzione")
	private String isInMaintenance;
	
	@Column(name = "nome_comando_java")
	private String javaCommandName;
}
