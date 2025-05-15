package com.example.mechanical_workshop.application.ports.output;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.mechanical_workshop.domain.exception.BusinessException;
import com.example.mechanical_workshop.domain.model.Appointment;

import jakarta.validation.Valid;

public interface AppointmentsRepositoryOutputPort {

	public Page<Appointment> listAppointments(@Valid Pageable pageable) throws BusinessException;

	public Optional<Appointment> getAppointment(@Valid String idAppointment) throws BusinessException;

	public String saveAppointment(@Valid Appointment appointment) throws BusinessException;

	public void deleteAppointment(@Valid String idAppointment) throws BusinessException;

	public void modifyAppointment(@Valid Appointment appointment) throws BusinessException;

	public Page<Appointment> searchAppointmentBy(@Valid String typeSearch, @Valid String value,
			@Valid Pageable pageable) throws BusinessException;
}
