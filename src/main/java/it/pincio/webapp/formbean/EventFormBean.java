package it.pincio.webapp.formbean;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EventFormBean implements Serializable {

	private static final long serialVersionUID = -5319411669686280726L;

	private String id;
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("place_name")
	private String placeName;
	
	@JsonProperty("description")
	private String description;
	
	@JsonProperty("start_time")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
	private Date startTime;
	
	@JsonProperty("id")
	private String facebookId;
	
	@JsonProperty("cover_source")
	private String coverSource;
	
	@JsonProperty("is_cancelled")
	private String isCancelled;
	
	@JsonProperty("place_lat")
	private String placeLat;
	
	@JsonProperty("place_lon")
	private String placeLon;
	
	@JsonProperty("token")
	private String token;
	
	private Integer startDateDay;
	private Integer startDateMonth;
	private Integer startDateYear;
	
	private Integer startTimeHour;
	private Integer startTimeMinute;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCoverSource() {
		return coverSource;
	}

	public void setCoverSource(String coverSource) {
		this.coverSource = coverSource;
	}

	public String getIsCancelled() {
		return isCancelled;
	}

	public void setIsCancelled(String isCancelled) {
		this.isCancelled = isCancelled;
	}

	public String getPlaceLat() {
		return placeLat;
	}

	public void setPlaceLat(String placeLat) {
		this.placeLat = placeLat;
	}

	public String getPlaceLon() {
		return placeLon;
	}

	public void setPlaceLon(String placeLon) {
		this.placeLon = placeLon;
	}

	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Integer getStartDateDay() {
		return startDateDay;
	}

	public void setStartDateDay(Integer startDateDay) {
		this.startDateDay = startDateDay;
	}

	public Integer getStartDateMonth() {
		return startDateMonth;
	}

	public void setStartDateMonth(Integer startDateMonth) {
		this.startDateMonth = startDateMonth;
	}

	public Integer getStartDateYear() {
		return startDateYear;
	}

	public void setStartDateYear(Integer startDateYear) {
		this.startDateYear = startDateYear;
	}

	public Integer getStartTimeHour() {
		return startTimeHour;
	}

	public void setStartTimeHour(Integer startTimeHour) {
		this.startTimeHour = startTimeHour;
	}

	public Integer getStartTimeMinute() {
		return startTimeMinute;
	}

	public void setStartTimeMinute(Integer startTimeMinute) {
		this.startTimeMinute = startTimeMinute;
	}
}
