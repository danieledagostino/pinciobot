package it.pincio.telegrambot.test.service;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import it.pincio.telegrambot.Application;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Application.class)
@SpringBootTest
public class TestSecurity
{
//	private MockMvc mockMvc;
	
//	@Autowired
//	private WebApplicationContext wac;

	@Before
	public void setup()
	{
//		this.mockMvc = MockMvcBuilders
//				.webAppContextSetup(wac)
//				.apply(springSecurity()) //Attiva la sicurezza
//				.build();

	}
	
	private String ApiBaseUrl = "/api/articoli";
	
	@Test
	//@WithAnonymousUser
	public void D_testErrlistArtByCodArt() throws Exception
	{
//		mockMvc.perform(MockMvcRequestBuilders.get(ApiBaseUrl + "/cerca/codice/002000301")
//				.accept(MediaType.APPLICATION_JSON))
//				.andExpect(status().isUnauthorized())
//				.andReturn();
	}  
}
