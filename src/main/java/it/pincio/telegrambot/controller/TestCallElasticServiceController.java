package it.pincio.telegrambot.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class TestCallElasticServiceController {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${ELASTIC_SERVICE}")
	private String ELASTIC_SERVICE;
	
	private String EL_INSERT = "/bot/elastic/insert/";
	
	@ApiOperation(
		      value = "Ricerca l'articolo per BARCODE", 
		      notes = "Restituisce i dati dell'articolo in formato JSON",
		      response = String.class, 
		      produces = "application/json")
//	@ApiResponses(value =
//	{   @ApiResponse(code = 200, message = "L'articolo cercato è stato trovato!"),
//	    @ApiResponse(code = 404, message = "L'articolo cercato NON è stato trovato!"),
//	    @ApiResponse(code = 403, message = "Non sei AUTORIZZATO ad accedere alle informazioni"),
//	    @ApiResponse(code = 401, message = "Non sei AUTENTICATO")
//	})
	@PostMapping(value = "/bot/test", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> test(@RequestBody String question)
		throws Exception	 
	{
		log.debug("****** Test call the elastic service *******");
		
		restTemplate.put(ELASTIC_SERVICE+EL_INSERT, String.class, question);
		
		return new ResponseEntity<String>("OK", HttpStatus.OK);
	} 
}
