package com.kafkatest.producer;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import com.kafkatest.dto.MessageDto;

@Component
public class KafkaProducer {
	
	@Autowired
	private KafkaTemplate<String,Object> kafkaTemplate;
	
	public void sendMessageAsync(String topic, Object message) {
		kafkaTemplate.send(topic, message);
	}
	
	public void sendMessage(String topic, Object message) {
		CompletableFuture<SendResult<String,Object>> future = kafkaTemplate.send(topic, message);
		
		// ex is an exception
		
		future.whenComplete((result, ex) -> {
			if (ex == null) {
				System.out.println("Message sent: " + result);
			} else {
				System.out.println("Error in sending message: " + ex.getMessage());
			}
		});
	}

}
