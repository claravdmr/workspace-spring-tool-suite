package com.example.mechanical_workshop.infrastructure.integrationevents.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.mechanical_workshop.application.ports.output.CustomersMessageOutputPort;
import com.example.mechanical_workshop.domain.model.Customer;
import com.example.mechanical_workshop.infrastructure.integrationevents.dto.CustomerEventDto;
import com.example.mechanical_workshop.infrastructure.integrationevents.mapper.CustomerToCustomerEventDtoMapper;
import com.example.mechanical_workshop.infrastructure.integrationevents.producer.KafkaProducer;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomersMessageService implements CustomersMessageOutputPort {

	@Value("${custom.topic.customer.posted}")
	private String topicPostedCustomer;

	@Value("${custom.topic.customer.modified}")
	private String topicModifiedCustomer;

	@Value("${custom.topic.customer.deleted}")
	private String topicDeletedCustomer;

	@Autowired
	KafkaProducer kafkaProducer;

	@Autowired
	CustomerToCustomerEventDtoMapper customerEventDtoMapper;

	@Override
	public void eventCreateCustomer(@Valid Customer input) {
		log.debug("eventCreateCustomer");

		CustomerEventDto eventoSalida = customerEventDtoMapper.fromInputToOutput(input);

		kafkaProducer.sendMessageAsync(topicPostedCustomer, eventoSalida);
	}

	@Override
	public void eventModifyCustomer(@Valid Customer input) {
		log.debug("eventModifyCustomer");

		CustomerEventDto eventoSalida = customerEventDtoMapper.fromInputToOutput(input);

		kafkaProducer.sendMessageAsync(topicModifiedCustomer, eventoSalida);
	}

	@Override
	public void eventDeleteCustomer(@Valid Customer customer) {
		log.debug("eventDeleteCustomer");

		CustomerEventDto eventoSalida = customerEventDtoMapper.fromInputToOutput(customer);

		kafkaProducer.sendMessageAsync(topicDeletedCustomer, eventoSalida);
	}

}
