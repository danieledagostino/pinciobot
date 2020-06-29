package it.pincio.webapp.formbean;

import java.io.Serializable;

public class FaqFormBean implements Serializable {

	private static final long serialVersionUID = 8133207049493595802L;
	
	private String id;
	private String domanda;
	private String parole;
	private String risposta;
	private String attivo;
	
	public String getDomanda() {
		return domanda;
	}
	public void setDomanda(String domanda) {
		this.domanda = domanda;
	}
	public String getParole() {
		return parole;
	}
	public void setParole(String parole) {
		this.parole = parole;
	}
	public String getRisposta() {
		return risposta;
	}
	public void setRisposta(String risposta) {
		this.risposta = risposta;
	}
	public String getAttivo() {
		return attivo;
	}
	public void setAttivo(String attivo) {
		this.attivo = attivo;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
