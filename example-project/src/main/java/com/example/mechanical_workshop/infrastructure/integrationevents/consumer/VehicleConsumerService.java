package com.example.mechanical_workshop.infrastructure.integrationevents.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.mechanical_workshop.application.ports.input.VehiclesServiceInputPort;
import com.example.mechanical_workshop.domain.exception.BusinessException;
import com.example.mechanical_workshop.infrastructure.integrationevents.dto.VehicleInputEventDto;
import com.example.mechanical_workshop.infrastructure.integrationevents.mapper.VehicleToVehicleInputEventDtoMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class VehicleConsumerService {

	@Autowired
	VehiclesServiceInputPort vehiclesServiceInputPort;

	@Autowired
	VehicleToVehicleInputEventDtoMapper vehicleInputEventDtoMapper;

	@KafkaListener(topics = "${custom.topic.vehicle.input-event}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "containerFactory")
	public void receive(ConsumerRecord<String, VehicleInputEventDto> consumerRecord) {
		log.debug("receive");
		try {
			vehiclesServiceInputPort
					.partialModificationVehicle(vehicleInputEventDtoMapper.fromOutputToInput(consumerRecord.value()));
		} catch (BusinessException e) {
			log.error("Error guardando mensaje Kafka", e);
		}
	}

}
