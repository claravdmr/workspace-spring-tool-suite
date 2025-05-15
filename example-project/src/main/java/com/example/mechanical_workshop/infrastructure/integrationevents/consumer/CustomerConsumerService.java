package com.example.mechanical_workshop.infrastructure.integrationevents.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.mechanical_workshop.application.ports.input.CustomersServiceInputPort;
import com.example.mechanical_workshop.domain.exception.BusinessException;
import com.example.mechanical_workshop.infrastructure.integrationevents.dto.CustomerInputEventDto;
import com.example.mechanical_workshop.infrastructure.integrationevents.mapper.CustomerToCustomerInputEventDtoMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomerConsumerService {

	@Autowired
	CustomersServiceInputPort customersServiceInputPort;

	@Autowired
	CustomerToCustomerInputEventDtoMapper customerInputEventDtoMapper;

	@KafkaListener(topics = "${custom.topic.customer.input-event}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "containerFactory")
	public void receive(ConsumerRecord<String, CustomerInputEventDto> consumerRecord) {
		log.debug("receive");
		try {
			customersServiceInputPort
					.partialModificationCustomer(customerInputEventDtoMapper.fromOutputToInput(consumerRecord.value()));
		} catch (BusinessException e) {
			log.error("Error guardando mensaje Kafka", e);
		}
	}

}
