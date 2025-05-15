package com.exampleKafka;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

import com.exampleKafka.dto.NewCarInputEventDto;
import com.exampleKafka.producer.BranchProducer;
import com.exampleKafka.service.BranchService;

@SpringBootTest
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9095", "port=9095" })
class ExampleFromHandoutApplicationTests {

	private CountDownLatch latch = new CountDownLatch(1);
	
	@Value("${custom.topic.branch}")
	private String topicBranch;
	
	@Value("${custom.topic.new-car}")
	private String topicNewCar;
	
	@Autowired
	BranchService service;
	
	@Autowired
	BranchProducer producer;
	
	@Autowired
	ConsumerBranchEventTestService consumerBranchEventTestService;
	
	@Test
	void testNewCar() throws InterruptedException{
		
		NewCarInputEventDto newCar = new NewCarInputEventDto();
		newCar.setId("id");
		newCar.setInfoNewCar("info");
		producer.sendMessage(topicNewCar, newCar);
		
		latch.await(5, TimeUnit.SECONDS);
		
		Assertions.assertFalse(service.info.isEmpty());
		Assertions.assertTrue(service.info.contains(newCar.getInfoNewCar())); // checks if the car reached the list in branchservice called info, meaning it was "added to database".
		
	}
	
	@Test
	void testBranch() throws InterruptedException {
		
	    // Para hacer un test de un productor del cual no tenemos consumidor, debemos montarnos uno en los test
		
	    service.saleCar("datos coche vendido");
	    consumerBranchEventTestService.getLatch().await(5, TimeUnit.SECONDS);
	    Assertions.assertNotNull(consumerBranchEventTestService.getPayload());
	    Assertions.assertNotNull(consumerBranchEventTestService.getPayload().getInfo());
	    Assertions.assertEquals("datos coche vendido", consumerBranchEventTestService.getPayload().getInfo());
	    consumerBranchEventTestService.getPayload().getInfo();
	}
	

}
