package com.kafkatest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.kafkatest.service.KafkaService;


@SpringBootTest
class KafkaTestApplicationTests {

	@Value("${custom.topics.one}")
	private String topic1;
	
	@Autowired
	KafkaService service;
	
	@Test
	void contextLoads() {
		service.sendMessages();
	
	}

}
