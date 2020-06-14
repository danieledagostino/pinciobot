package it.pincio.persistence.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "faq")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Faq {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(name = "keywords")
	private String keywords;
	
	@Column(name = "answer")
	private String answer;
	
	@Column(name = "hint")
	private String hint;
	
	@Column(name = "active")
	private String active;
}
