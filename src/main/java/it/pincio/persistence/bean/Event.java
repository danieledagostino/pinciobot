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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "eventi")
@DynamicInsert
public class Event implements Serializable{

	private static final long serialVersionUID = -5085471528148027217L;

	public Event() {
		
	}
	
	public Event(Integer id) {
		this.id = id;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	
	@JoinColumn(name = "proprietario", referencedColumnName = "id")
	@OneToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private ChatUser owner;
	
	@DateTimeFormat
	@Column(name = "data_inserimento")
	private Date insertDate;
	
	@Column(name = "step")
	private Integer step;
	
	@Column(name = "id_facebook")
	private String facebookId;
	
	@ManyToMany(mappedBy = "events", fetch = FetchType.LAZY)
	@JsonIgnore
	Set<ChatUser> participants;
	
	@Column(name = "token")
	private String token;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getCancelled() {
		return cancelled;
	}

	public void setCancelled(String cancelled) {
		this.cancelled = cancelled;
	}

	public ChatUser getOwner() {
		return owner;
	}

	public void setOwner(ChatUser owner) {
		this.owner = owner;
	}

	public Date getInsertDate() {
		return insertDate;
	}

	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}

	public Integer getStep() {
		return step;
	}

	public void setStep(Integer step) {
		this.step = step;
	}

	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	public Set<ChatUser> getParticipants() {
		return participants;
	}

	public void setParticipants(Set<ChatUser> participants) {
		this.participants = participants;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}
