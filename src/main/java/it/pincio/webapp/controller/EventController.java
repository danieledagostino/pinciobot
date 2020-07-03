package it.pincio.webapp.controller;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.pincio.webapp.formbean.EventFormBean;
import it.pincio.webapp.formbean.FaqFormBean;
import it.pincio.webapp.service.EventWebappService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/event/")
@Slf4j
public class EventController {
	
	@Autowired
	EventWebappService service;
	
	private ObjectMapper jacksonObjectMapper;
	
	@PostConstruct
	public void postConstuct() {
		jacksonObjectMapper = new ObjectMapper();
	}
	
	@PostMapping(value = "/new", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> newFaq(@RequestBody EventFormBean request) {
		
		try {
			service.insert(request);
			
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			log.error("Faq not inserted", e);
			
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> update(@RequestBody EventFormBean request) {
		
		try {
			service.update(request);
			
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			log.error("Faq not updated", e);
			
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> faqList(@RequestParam(value = "filter", required=false) String filter) {
		
		String json = "";
		try {
			List<EventFormBean> eventList = service.list();
			json = jacksonObjectMapper.writeValueAsString(eventList);
		} catch (JsonProcessingException e) {
			log.error("Error while convert list to json");
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<String>(json, HttpStatus.OK);
	}
	
	@GetMapping(value = "/detail/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> faqDetail(@PathVariable("id") Integer id) {
		
		String json = "";
		try {
			EventFormBean event = service.detail(id);
			if (event == null) {
				log.warn("Warning: Faq with id {} not exist", id);
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			json = jacksonObjectMapper.writeValueAsString(event);
		} catch (JsonProcessingException e) {
			log.error("Error while detail request");
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<String>(json, HttpStatus.OK);
	}
	
	@GetMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> faqDeete(@PathVariable("id") Integer id) {
		
		String json = "";
		try {
			service.delete(id);
		} catch (Exception e) {
			log.error("Error while deleting the faq");
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<String>(json, HttpStatus.OK);
	}
	
}
