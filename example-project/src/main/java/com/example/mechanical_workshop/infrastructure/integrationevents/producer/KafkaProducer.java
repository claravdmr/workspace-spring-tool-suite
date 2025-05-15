package com.example.mechanical_workshop.infrastructure.integrationevents.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;

	public void sendMessageAsync(String topic, Object message) {
		kafkaTemplate.send(topic, message);
	}
}
