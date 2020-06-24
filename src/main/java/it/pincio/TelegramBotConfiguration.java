package it.pincio;

import java.util.Locale;

import javax.annotation.PreDestroy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.generics.BotSession;

import lombok.extern.slf4j.Slf4j;

@Configuration
@SpringBootApplication
@PropertySource("classpath:app.properties")
@EnableJpaRepositories
@ComponentScans({
	@ComponentScan("it.pincio.telegrambot.service"),
	@ComponentScan("it.pincio.persistence.dao"),
	@ComponentScan("it.pincio.telegrambot.command"),
	@ComponentScan("it.pincio.telegrambot.utility"),
	@ComponentScan("it.pincio.webapp.controller")
})
@Slf4j
@EnableWebMvc
public class TelegramBotConfiguration {
	
	private static BotSession session;
	
	public static void main(String[] args) 
	{
		ApiContextInitializer.init();
		
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
//        try {
//        	session = telegramBotsApi.registerBot(new FirstEntryPointTelegramBot());
//        	
//        } catch (TelegramApiException e) {
//            log.error("******************* Registration hook error *******************");
//        }
		
		log.info("******************* ApiContextInitializer.init() *******************");
		SpringApplication.run(TelegramBotConfiguration.class, args);
	}
	
	@PreDestroy
	public static void destroy() {
//		if (session != null && session.isRunning()) {
//			session.stop();
//		}
	}
	
	@Bean
	public MessageSource messageSource() {
	    ReloadableResourceBundleMessageSource messageSource
	      = new ReloadableResourceBundleMessageSource();
	     
	    messageSource.setBasename("classpath:messages");
	    messageSource.setDefaultEncoding("UTF-8");
	    return messageSource;
	}
	
	@Bean                 
    public LocaleResolver localeResolver() {

        SessionLocaleResolver localResolver=new SessionLocaleResolver();
        localResolver.setDefaultLocale(Locale.ITALY);
        return localResolver;
    }
}
