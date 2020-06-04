package it.pincio.telegrambot;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import it.pincio.telegrambot.component.LoggingRequestInterceptor;
import it.pincio.telegrambot.controller.FirstEntryPointTelegramBot;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableEurekaClient
@EnableAutoConfiguration
@SpringBootApplication
@Slf4j
public class TelegramBotConfiguration {

	@Value("${spring.security.user.password}")
	private String BASIC_PW;

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder)
	{
		List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
		interceptors.add(new LoggingRequestInterceptor());
		interceptors.add(new BasicAuthenticationInterceptor("user", BASIC_PW));
		
		RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());
		restTemplate.setInterceptors(interceptors);
		
		return restTemplate;
	}
	
	private HttpClient httpClient()
	{
	
		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

		credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("user", BASIC_PW));

		HttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider).build();

		return client;

		
	}

	private HttpComponentsClientHttpRequestFactory getClientHttpRequestFactory() 
    {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory
                          = new HttpComponentsClientHttpRequestFactory();
         
        clientHttpRequestFactory.setHttpClient(httpClient());
              
        return clientHttpRequestFactory;
    }
	
	@Bean
	public HttpHeaders httpHeaders() {
		HttpHeaders httpHeaders = new HttpHeaders();

		String plainCredentials="user:"+BASIC_PW;
        String base64Credentials = Base64.getEncoder().encodeToString(plainCredentials.getBytes());

		httpHeaders.add("Content-Type", "application/json");
		httpHeaders.set("Authorization","Basic " + base64Credentials);
	    
	    return httpHeaders;
	}

	public static void main(String[] args) 
	{
		ApiContextInitializer.init();
		
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new FirstEntryPointTelegramBot());
        } catch (TelegramApiException e) {
            log.error("Registration hook error");
        }
		
		log.info("******************* ApiContextInitializer.init() *******************");
		SpringApplication.run(TelegramBotConfiguration.class, args);
	}
}
