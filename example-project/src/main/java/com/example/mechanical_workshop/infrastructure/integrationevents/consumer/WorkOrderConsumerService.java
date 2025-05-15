package com.example.mechanical_workshop.infrastructure.integrationevents.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.mechanical_workshop.application.ports.input.WorkOrdersServiceInputPort;
import com.example.mechanical_workshop.domain.exception.BusinessException;
import com.example.mechanical_workshop.infrastructure.integrationevents.dto.WorkOrderInputEventDto;
import com.example.mechanical_workshop.infrastructure.integrationevents.mapper.WorkOrderToWorkOrderInputEventDtoMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class WorkOrderConsumerService {

	@Autowired
	WorkOrdersServiceInputPort workOrdersServiceInputPort;

	@Autowired
	WorkOrderToWorkOrderInputEventDtoMapper workOrderInputEventDtoMapper;

	@KafkaListener(topics = "${custom.topic.workOrder.input-event}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "containerFactory")
	public void receive(ConsumerRecord<String, WorkOrderInputEventDto> consumerRecord) {
		log.debug("receive");
		try {
			workOrdersServiceInputPort.partialModificationWorkOrder(
					workOrderInputEventDtoMapper.fromOutputToInput(consumerRecord.value()));
		} catch (BusinessException e) {
			log.error("Error guardando mensaje Kafka", e);
		}
	}

}
