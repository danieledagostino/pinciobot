package it.pincio.webapp.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import it.pincio.googlesheet.SheetsQuickstart;

@RestController
public class WakeupController {
	

	@GetMapping(value = "/wakeup")
	public ResponseEntity<String> wakeup() {
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping(value = "/sheet")
	public ResponseEntity<String> sheet() {
		
		try {
			SheetsQuickstart.main(null);
		} catch (IOException | GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}
