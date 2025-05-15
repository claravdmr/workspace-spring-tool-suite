package com.example.mechanical_workshop.infrastructure.integrationevents.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.mechanical_workshop.application.ports.output.VehiclesMessageOutputPort;
import com.example.mechanical_workshop.domain.model.Vehicle;
import com.example.mechanical_workshop.infrastructure.integrationevents.dto.VehicleEventDto;
import com.example.mechanical_workshop.infrastructure.integrationevents.mapper.VehicleToVehicleEventDtoMapper;
import com.example.mechanical_workshop.infrastructure.integrationevents.producer.KafkaProducer;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class VehiclesMessageService implements VehiclesMessageOutputPort {

	@Value("${custom.topic.vehicle.posted}")
	private String topicPostedVehicle;

	@Value("${custom.topic.vehicle.modified}")
	private String topicModifiedVehicle;

	@Value("${custom.topic.vehicle.deleted}")
	private String topicDeletedVehicle;

	@Autowired
	KafkaProducer kafkaProducer;

	@Autowired
	VehicleToVehicleEventDtoMapper vehicleEventDtoMapper;

	@Override
	public void eventCreateVehicle(@Valid Vehicle input) {
		log.debug("eventoCreacionVehicle");

		VehicleEventDto eventoSalida = vehicleEventDtoMapper.fromInputToOutput(input);

		kafkaProducer.sendMessageAsync(topicPostedVehicle, eventoSalida);
	}

	@Override
	public void eventModifyVehicle(@Valid Vehicle input) {
		log.debug("eventoModificacionVehicle");

		VehicleEventDto eventoSalida = vehicleEventDtoMapper.fromInputToOutput(input);

		kafkaProducer.sendMessageAsync(topicModifiedVehicle, eventoSalida);
	}

	@Override
	public void eventDeleteVehicle(@Valid Vehicle vehicle) {
		log.debug("eventoEliminacionVehicle");

		VehicleEventDto eventoSalida = vehicleEventDtoMapper.fromInputToOutput(vehicle);

		kafkaProducer.sendMessageAsync(topicDeletedVehicle, eventoSalida);
	}

}
