package com.kafkatest.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.kafkatest.dto.MessageDto;

@Component
public class KafkaConsumer {
	
	//@RetryableTopic(attempts = "3")
	@KafkaListener(topics = "${custom.topics.one}", groupId = "${custom.groups.one}", containerFactory = "messageContainerFactory")
	public void messageListener(MessageDto message) {
		System.out.println("Message received: " + message.getMessage());
	}
	
	@KafkaListener(topics = "${custom.topics.one}", groupId = "${custom.groups.two}", containerFactory = "messageContainerFactory")
	public void messageListener2(String message) {
		System.out.println("Message 2 received: " + message);
	}

}
