package com.example.mechanical_workshop.infrastructure.integrationevents.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.mechanical_workshop.application.ports.input.AppointmentsServiceInputPort;
import com.example.mechanical_workshop.domain.exception.BusinessException;
import com.example.mechanical_workshop.infrastructure.integrationevents.dto.AppointmentInputEventDto;
import com.example.mechanical_workshop.infrastructure.integrationevents.mapper.AppointmentToAppointmentInputEventDtoMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AppointmentConsumerService {

	@Autowired
	AppointmentsServiceInputPort appointmentsServiceInputPort;

	@Autowired
	AppointmentToAppointmentInputEventDtoMapper appointmentInputEventDtoMapper;

	@KafkaListener(topics = "${custom.topic.appointment.input-event}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "containerFactory")
	public void receive(ConsumerRecord<String, AppointmentInputEventDto> consumerRecord) {
		log.debug("receive");
		try {
			appointmentsServiceInputPort.partialModificationAppointment(
					appointmentInputEventDtoMapper.fromOutputToInput(consumerRecord.value()));
		} catch (BusinessException e) {
			log.error("Error guardando mensaje Kafka", e);
		}
	}

}
