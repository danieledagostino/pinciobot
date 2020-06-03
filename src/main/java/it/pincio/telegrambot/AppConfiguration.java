package it.pincio.telegrambot;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import it.pincio.telegrambot.component.LoggingRequestInterceptor;

@Configuration
@EnableEurekaClient
@EnableAutoConfiguration
public class AppConfiguration {
	
	@Value("PINCIO_PASSWORD")
	private String BASIC_PW;

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
		interceptors.add(new LoggingRequestInterceptor());
		
		RestTemplate restTemplate = builder.basicAuthentication("pincio", BASIC_PW).build();
		restTemplate.setInterceptors(interceptors);
		
		return restTemplate;
	 }

}
