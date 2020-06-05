package it.pincio.telegrambot.test.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.junit4.SpringRunner;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;
import com.rabbitmq.client.ShutdownSignalException;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@Slf4j
public class SendMessageToRabbitMQTest {

	@Value("${CLOUDAMQP_APIKEY}")
	private String CLOUDAMQP_APIKEY;

	@Value("${CLOUDAMQP_URL}")
	private String CLOUDAMQP_URL;

	private final static String QUEUE_NAME = "bot_message";

//	@Test
	public void A_produce() throws IOException {
		ConnectionFactory factory = new ConnectionFactory();
		Connection connection = null;
		try {
			factory.setUri(CLOUDAMQP_URL);
			factory.setRequestedHeartbeat(30);
			factory.setConnectionTimeout(120);
			
			connection = factory.newConnection();
			Channel channel = connection.createChannel();
			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			channel.basicPublish("", QUEUE_NAME, null, "prova di test".getBytes());

//			QueueingConsumer consumer = new QueueingConsumer(channel);
//			channel.basicConsume(QUEUE_NAME, true, consumer);
		} catch (IOException e) {
			log.error("", e);
		} catch (KeyManagementException | NoSuchAlgorithmException | URISyntaxException e1) {
			log.error("", e1);
		} finally {
			if (connection.isOpen()) {
				connection.close();
			}
		}
	}
	
	@Test
	public void B_consume() throws IOException {
		ConnectionFactory factory = new ConnectionFactory();
		Connection connection = null;
		try {
			factory.setUri(CLOUDAMQP_URL);
			factory.setRequestedHeartbeat(30);
			factory.setConnectionTimeout(120);
			
			connection = factory.newConnection();
			Channel channel = connection.createChannel();

			QueueingConsumer consumer = new QueueingConsumer(channel);
			channel.basicConsume(QUEUE_NAME, true, consumer);
			
			Delivery delivery = null;
			while ((delivery = consumer.nextDelivery()) != null)
			{
				log.info(new String(delivery.getBody()));
			}
		} catch (IOException e) {
			log.error("", e);
		} catch (KeyManagementException | NoSuchAlgorithmException | URISyntaxException e1) {
			log.error("", e1);
		} catch (ShutdownSignalException e) {
			log.error("", e);
		} catch (ConsumerCancelledException e) {
			log.error("", e);
		} catch (InterruptedException e) {
			log.error("", e);
		} finally {
			if (connection.isOpen()) {
				connection.close();
			}
		}
	}
}
