package it.pincio.persistence.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "eventi")
@Data
public class Event {

	@Id
	private Integer id;
	
	@Column(name = "titolo")
	private String title;
	
	@Column(name = "descrizione")
	private String description;
	
	@DateTimeFormat
	@Column(name = "data_inizio")
	private String startDate;
	
	@Column(name = "luogo_nome")
	private String placeName;
	
	@Column(name = "latitudine")
	private String latitude;
	
	@Column(name = "longitudine")
	private String longitude;
	
	@Column(name = "cancellato")
	private String cancelled;
	
	@Column(name = "proprietario")
	private String owner;
	
	@DateTimeFormat
	@Column(name = "data_inserimento")
	private String insertDate;
	
	@Column(name = "step")
	private Integer step;
}
