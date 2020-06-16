package it.pincio.batch;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;

import it.pincio.telegrambot.utility.Costants;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AnalyzeMessageFromQueue {
	
	@Value("${CLOUDAMQP_URL}")
	private String CLOUDAMQP_URL;
	
	Connection connection = null;
	Channel channel = null;
	
	@Scheduled
	public void search() {
		log.info("Get message from rabbit message queue starts");
		
		ConnectionFactory factory = new ConnectionFactory();
		try {
			factory.setUri(CLOUDAMQP_URL);
			factory.setRequestedHeartbeat(30);
			factory.setConnectionTimeout(120);
			
			connection = factory.newConnection();
			channel = connection.createChannel();
			
			GetResponse response = channel.basicGet(Costants.TG_UPDATE_QUEUE, false);
			String jsonUpdate = null;
			
			while (response != null) {
				AMQP.BasicProperties props = response.getProps();
				jsonUpdate = new String(response.getBody());
				long deliveryTag = response.getEnvelope().getDeliveryTag();
				Update update = new ObjectMapper().readValue(jsonUpdate, Update.class);
				
				
				
				channel.basicAck(deliveryTag, false);
				response = channel.basicGet(Costants.TG_UPDATE_QUEUE, false);
			}
			
			if (response == null) {
				channel.queueDelete(Costants.TG_UPDATE_QUEUE, true, true);
			}
			
			log.info("Get message from rabbit message queue ends");
		} catch (KeyManagementException | NoSuchAlgorithmException | URISyntaxException | IOException e) {
			log.error("Error with rabbit mq comunication", e);
		}
	}

}
