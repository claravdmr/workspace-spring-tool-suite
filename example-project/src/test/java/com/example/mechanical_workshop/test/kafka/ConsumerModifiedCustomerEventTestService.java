package com.example.mechanical_workshop.test.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.mechanical_workshop.infrastructure.integrationevents.dto.CustomerEventDto;
import com.example.mechanical_workshop.test.util.ConsumerLatch;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConsumerModifiedCustomerEventTestService extends ConsumerLatch<CustomerEventDto> {

	@KafkaListener(topics = "${custom.topic.customer.modified}", groupId = "${spring.kafka.consumer.group-id-test}", containerFactory = "containerFactory")
	public void receive(ConsumerRecord<String, CustomerEventDto> consumerRecord) {
		log.debug("receive");
		this.consumeMsg(consumerRecord);
	}

}