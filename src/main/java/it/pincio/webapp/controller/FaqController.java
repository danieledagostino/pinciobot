package it.pincio.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.pincio.webapp.formbean.FaqFormBean;
import it.pincio.webapp.service.FaqWebappService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/faq/")
@Slf4j
public class FaqController {
	
	@Autowired
	FaqWebappService service;
	
	@PostMapping(value = "/new", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> newFaq(@ModelAttribute FaqFormBean request) {
		
		try {
			service.insert(request);
			
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			log.error("Faq not inserted", e);
			
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> update(@ModelAttribute FaqFormBean request) {
		
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping(value = "/list", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> faqList(@RequestParam("filter") String filter) {
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping(value = "/detail/{id}", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> faqDetail(@RequestParam("id") String id) {
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}
