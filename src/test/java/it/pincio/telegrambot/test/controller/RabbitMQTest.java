package it.pincio.telegrambot.test.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.junit4.SpringRunner;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.GetResponse;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;
import com.rabbitmq.client.ShutdownSignalException;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@Slf4j
public class RabbitMQTest {

	@Value("${CLOUDAMQP_APIKEY}")
	private String CLOUDAMQP_APIKEY;

	@Value("${CLOUDAMQP_URL}")
	private String CLOUDAMQP_URL;

	private final static String QUEUE_NAME = "bot_update";
	
	Connection connection = null;
	Channel channel = null;
	
	@Before
	public void openQueueConnection() {
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setUri(CLOUDAMQP_URL);
			factory.setRequestedHeartbeat(30);
			factory.setConnectionTimeout(120);
			
			connection = factory.newConnection();
			channel = connection.createChannel();
		} catch (IOException e) {
			log.error("", e);
		} catch (KeyManagementException | NoSuchAlgorithmException | URISyntaxException e1) {
			log.error("", e1);
		}
	}
	
	@After
	public void closeQueueConnection() {
		try {
			if (connection != null && connection.isOpen())
			{
				connection.close();
			}
		
			if (channel != null && channel.isOpen())
			{
				channel.close();
			}
		} catch (IOException e) {
			log.error("", e);
		}
	}

//	@Test
	public void A_produce() throws IOException {
		
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		channel.basicPublish("", QUEUE_NAME, null, "prova di test".getBytes());

//		QueueingConsumer consumer = new QueueingConsumer(channel);
//		channel.basicConsume(QUEUE_NAME, true, consumer);
	}
	
//	@Test
	public void B_consumeWatingForDelivery() throws IOException, ShutdownSignalException, ConsumerCancelledException, InterruptedException {

		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(QUEUE_NAME, true, consumer);
		
		Delivery delivery = null;
		long start = System.currentTimeMillis();
		long end = 0;
		while (end - start < 20000)
		{
			delivery = consumer.nextDelivery(5000);
			end = System.currentTimeMillis();
			System.out.println(end - start);
			if (delivery != null)
			{
				log.info(new String(delivery.getBody()));
			}
		}
	}
	
	@Test
	public void C_consumeAllSoon() throws IOException {
		GetResponse response = channel.basicGet(QUEUE_NAME, false);
		String jsonUpdate = null;
		
		while (response != null)
		{
		    AMQP.BasicProperties props = response.getProps();
		    jsonUpdate = new String(response.getBody());
		    long deliveryTag = response.getEnvelope().getDeliveryTag();
		    System.out.println("");
		    channel.basicAck(deliveryTag, false);
		    response = channel.basicGet(QUEUE_NAME, false);
		}
		
		if (response == null) {
			channel.queueDelete(QUEUE_NAME, true, true);
		}
	}
	
	@Test
	public void D_queueList() throws IOException {
	}
}
