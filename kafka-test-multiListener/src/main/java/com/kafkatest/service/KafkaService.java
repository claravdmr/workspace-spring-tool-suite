package com.kafkatest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kafkatest.dto.MessageDto;
import com.kafkatest.dto.NewMessageDto;
import com.kafkatest.producer.KafkaProducer;

import java.util.Random;

@Service
public class KafkaService {
	
	private static final String[] USERS = { "Manolo", "Benito" };
	
	@Value("${custom.topics.one}")
	String topic1;
	
	@Autowired
	private KafkaProducer producer;
	
	private Random random = new Random();
	
	
	public void sendMessages() {
		int counter = 0;
		do {
			
			
			Object message = createRandomMessage();
			
			producer.sendMessage(topic1, message);
			counter++;
			
		} while(counter < 10);
	}


	private Object createRandomMessage() {
		
		Object message;
		switch(random.nextInt(3)) {
		
		case 0:
			
			NewMessageDto message2 = new NewMessageDto();
			message2.setMessage("MessageDto");
			message2.setUser(getRandomUser());
			message = message2;
			
			break;
		
		case 1:
			
			MessageDto message3 = new MessageDto();
			message3.setMessage("MessageDto");
			message3.setUser(getRandomUser());
			message = message3;
			
			break;
		
		case 2:
			
			message = "Simple string: " + getRandomUser();
			
			break;
		
		default:
			
			throw new UnsupportedOperationException();
		}
		
		return message;
	}
	
	private String getRandomUser() {
		
		int index = random.nextInt(USERS.length);
		return USERS[index];
		
	}

}










