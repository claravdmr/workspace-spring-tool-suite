package com.kafkatest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kafkatest.producer.KafkaProducer;

@Service
public class KafkaService {
	
	@Value("${custom.topics.one}")
	String topic1;
	
	@Autowired
	private KafkaProducer producer;
	
	public void sendMessages() {
		int counter = 0;
		do {
			
			producer.sendMessage(topic1, "Message " + counter);
			counter++;
			
		} while(counter < 10);
	}

}
