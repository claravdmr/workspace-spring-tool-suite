package com.kafkatest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kafkatest.dto.MessageDto;
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
			
			MessageDto message = new MessageDto();
			message.setMessage("Message sent.");
			message.setUser("Clara");
			
			producer.sendMessage(topic1, message);
			counter++;
			
		} while(counter < 10);
	}

}
