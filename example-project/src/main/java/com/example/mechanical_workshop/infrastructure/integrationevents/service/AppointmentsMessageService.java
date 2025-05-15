package com.example.mechanical_workshop.infrastructure.integrationevents.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.mechanical_workshop.application.ports.output.AppointmentsMessageOutputPort;
import com.example.mechanical_workshop.domain.model.Appointment;
import com.example.mechanical_workshop.infrastructure.integrationevents.dto.AppointmentEventDto;
import com.example.mechanical_workshop.infrastructure.integrationevents.mapper.AppointmentToAppointmentEventDtoMapper;
import com.example.mechanical_workshop.infrastructure.integrationevents.producer.KafkaProducer;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AppointmentsMessageService implements AppointmentsMessageOutputPort {

	@Value("${custom.topic.appointment.posted}")
	private String topicPostedAppointment;

	@Value("${custom.topic.appointment.modified}")
	private String topicModifiedAppointment;

	@Value("${custom.topic.appointment.deleted}")
	private String topicDeletedAppointment;

	@Autowired
	KafkaProducer kafkaProducer;

	@Autowired
	AppointmentToAppointmentEventDtoMapper appointmentToAppointmentEventDtoMapper;

	@Override
	public void eventCreateAppointment(@Valid Appointment input) {
		log.debug("eventCreateAppointment");

		AppointmentEventDto eventoSalida = appointmentToAppointmentEventDtoMapper.fromInputToOutput(input);

		kafkaProducer.sendMessageAsync(topicPostedAppointment, eventoSalida);
	}

	@Override
	public void eventModifyAppointment(@Valid Appointment input) {
		log.debug("eventModifyAppointment");

		AppointmentEventDto eventoSalida = appointmentToAppointmentEventDtoMapper.fromInputToOutput(input);

		kafkaProducer.sendMessageAsync(topicModifiedAppointment, eventoSalida);
	}

	@Override
	public void eventDeleteAppointment(@Valid Appointment appointment) {
		log.debug("eventDeleteAppointment");

		AppointmentEventDto eventoSalida = appointmentToAppointmentEventDtoMapper.fromInputToOutput(appointment);

		kafkaProducer.sendMessageAsync(topicDeletedAppointment, eventoSalida);
	}

}
