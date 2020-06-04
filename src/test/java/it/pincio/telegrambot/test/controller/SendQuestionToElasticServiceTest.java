package it.pincio.telegrambot.test.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonArray;

import it.pincio.telegrambot.TelegramBotConfiguration;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TelegramBotConfiguration.class)
@SpringBootTest
public class SendQuestionToElasticServiceTest {

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private HttpHeaders httpHeaders;
	
	@Value("${ELASTIC_SERVICE}")
	private String ELASTIC_SERVICE;
	
	private String EL_INSERT = "/bot/elastic/insert/";
	
	private String EL_SEARCH = "/bot/elastic/search/";
	
//	@Test
//	public void A_givenAuthentication_whenAccessHome_thenOK() {
//	    int statusCode = RestAssured.given().auth().basic("john", "123")
//	      .get("http://localhost:8080/")
//	      .statusCode();
//	     
//	    assertEquals(HttpStatus.OK.value(), statusCode);
//	}
	
	@Test
	public void B_test() throws Exception
	{
		//JSONObject json = new JSONObject("[\"prova\"]");
		JsonArray jsonArray = new JsonArray(1);
		jsonArray.add("prova1 prova1 prova1");
		HttpEntity<String> request = new HttpEntity<>(jsonArray.toString(), httpHeaders);
		ResponseEntity<String> response = restTemplate.exchange(ELASTIC_SERVICE+EL_INSERT, HttpMethod.PUT, request, String.class);
	}
	
	@Test
	public void C_test() throws Exception
	{
		//JSONObject json = new JSONObject("[\"prova\"]");
		JsonArray jsonArray = new JsonArray(1);
		jsonArray.add("prova");
		HttpEntity<String> request = new HttpEntity<>(jsonArray.toString(), httpHeaders);
		ResponseEntity<String> response = restTemplate.exchange(ELASTIC_SERVICE+EL_SEARCH, HttpMethod.GET, request, String.class);
	}
}
