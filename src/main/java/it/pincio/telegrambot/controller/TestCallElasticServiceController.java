package it.pincio.telegrambot.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonArray;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class TestCallElasticServiceController {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private HttpHeaders httpHeaders;
	
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
	public ResponseEntity<String> test(@RequestBody List<String> questions)
		throws Exception	 
	{
		log.debug("****** Test call the elastic service *******");
		
		JsonArray jsonArray = new JsonArray(1);
		for (String q : questions) {
			jsonArray.add(q);
		}
		HttpEntity<String> request = new HttpEntity<>(jsonArray.toString(), httpHeaders);
		ResponseEntity<String> response = restTemplate.exchange(ELASTIC_SERVICE+EL_INSERT, HttpMethod.PUT, request, String.class);
		
		return response;
	} 
}