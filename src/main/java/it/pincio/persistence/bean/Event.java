package it.pincio.persistence.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "eventi")
@Data
public class Event implements Serializable{

	private static final long serialVersionUID = -5085471528148027217L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(name = "titolo")
	private String title;
	
	@Column(name = "descrizione")
	private String description;
	
	@DateTimeFormat
	@Column(name = "data_inizio")
	private Date startDate;
	
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
	private Date insertDate;
	
	@Column(name = "step")
	private Integer step;
	
	@Column(name = "id_facebook")
	private String idFacebook;	
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "partecipantId.event")
	Set<Partecipant> partecipants = new HashSet<Partecipant>();
}
