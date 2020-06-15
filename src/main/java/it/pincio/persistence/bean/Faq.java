package it.pincio.persistence.bean;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "faq")
@Data
public class Faq implements Serializable{

	private static final long serialVersionUID = -3047990182101512244L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	private String keywords;
	
	private String answer;
	
	private String hint;
	
	private String active;
}
