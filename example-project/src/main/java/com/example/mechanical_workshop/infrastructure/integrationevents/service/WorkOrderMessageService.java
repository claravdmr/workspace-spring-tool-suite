package com.example.mechanical_workshop.infrastructure.integrationevents.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.mechanical_workshop.application.ports.output.WorkOrdersMessageOutputPort;
import com.example.mechanical_workshop.domain.model.WorkOrder;
import com.example.mechanical_workshop.infrastructure.integrationevents.dto.WorkOrderEventDto;
import com.example.mechanical_workshop.infrastructure.integrationevents.mapper.WorkOrderToWorkOrderEventDtoMapper;
import com.example.mechanical_workshop.infrastructure.integrationevents.producer.KafkaProducer;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class WorkOrderMessageService implements WorkOrdersMessageOutputPort {

	@Value("${custom.topic.workOrder.posted}")
	private String topicPostedWorkOrder;

	@Value("${custom.topic.workOrder.modified}")
	private String topicModifiedWorkOrder;

	@Value("${custom.topic.workOrder.deleted}")
	private String topicDeletedWorkOrder;

	@Autowired
	KafkaProducer kafkaProducer;

	@Autowired
	WorkOrderToWorkOrderEventDtoMapper workOrderEventDtoMapper;

	@Override
	public void eventCreateWorkOrder(@Valid WorkOrder input) {
		log.debug("eventoCreacionWorkOrder");

		WorkOrderEventDto eventoSalida = workOrderEventDtoMapper.fromInputToOutput(input);

		kafkaProducer.sendMessageAsync(topicPostedWorkOrder, eventoSalida);
	}

	@Override
	public void eventModifyWorkOrder(@Valid WorkOrder input) {
		log.debug("eventoModificacionWorkOrder");

		WorkOrderEventDto eventoSalida = workOrderEventDtoMapper.fromInputToOutput(input);

		kafkaProducer.sendMessageAsync(topicModifiedWorkOrder, eventoSalida);
	}

	@Override
	public void eventDeleteWorkOrder(@Valid WorkOrder workOrder) {
		log.debug("eventoEliminacionWorkOrder");

		WorkOrderEventDto eventoSalida = workOrderEventDtoMapper.fromInputToOutput(workOrder);

		kafkaProducer.sendMessageAsync(topicDeletedWorkOrder, eventoSalida);
	}

}
