package com.example.mechanical_workshop.application.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.mechanical_workshop.application.ports.input.AppointmentsServiceInputPort;
import com.example.mechanical_workshop.application.ports.output.AppointmentsMessageOutputPort;
import com.example.mechanical_workshop.application.ports.output.AppointmentsRepositoryOutputPort;
import com.example.mechanical_workshop.application.util.Constants;
import com.example.mechanical_workshop.application.util.Errors;
import com.example.mechanical_workshop.domain.exception.BusinessException;
import com.example.mechanical_workshop.domain.mapper.AppointmentPatchMapper;
import com.example.mechanical_workshop.domain.model.Appointment;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio para gestionar citas en la aplicación.
 */
@Slf4j
@Service
public class AppointmentService implements AppointmentsServiceInputPort {

	@Autowired
	AppointmentsRepositoryOutputPort appointmentRepository;

	@Autowired
	AppointmentsMessageOutputPort kafkaProducer;

	@Autowired
	AppointmentPatchMapper appointmentPatchMapper;

	/**
	 * Crea una nueva cita.
	 *
	 * @param appointment Cita a crear.
	 * @return ID de la cita creada.
	 * @throws BusinessException
	 */
	@Override
	public String createAppointment(@Valid Appointment appointment) throws BusinessException {
		log.debug("Init createAppointment");
		try {
			String appointmentId = appointmentRepository.saveAppointment(appointment);
			appointment.setId(appointmentId);
			kafkaProducer.eventCreateAppointment(appointment);
			log.debug("End createAppointment");
			return appointmentId;
		} catch (BusinessException e) {
			log.error("Error creando la cita", e);
			throw e;
		}
	}

	/**
	 * Obtiene todas las citas paginadas.
	 *
	 * @param pageable Información de paginación.
	 * @return Página de citas.
	 * @throws BusinessException si se excede el máximo de paginación permitido.
	 */
	@Override
	public Page<Appointment> getAppointments(@Valid Pageable pageable) throws BusinessException {
		log.debug("Init getAppointments");
		try {
			if (pageable.getPageSize() > Constants.MAXIMUM_PAGINATION) {
				throw new BusinessException(Errors.MAXIMUM_PAGINATION_EXCEEDED);
			}
			return appointmentRepository.listAppointments(pageable);
		} catch (BusinessException e) {
			log.error("Error obteniendo las citas", e);
			throw e;
		}
	}

	/**
	 * Obtiene una cita por su ID.
	 *
	 * @param idAppointment ID de la cita.
	 * @return Optional que contiene la cita si se encuentra, de lo contrario vacío.
	 * @throws BusinessException
	 */
	@Override
	public Optional<Appointment> getAppointment(@Valid String idAppointment) throws BusinessException {
		log.debug("Init getAppointment");
		try {
			return appointmentRepository.getAppointment(idAppointment);
		} catch (BusinessException e) {
			log.error("Error obteniendo las cita con id" + idAppointment, e);
			throw e;
		}
	}

	/**
	 * Realiza una modificación parcial de una cita existente.
	 *
	 * @param appointment Cita con los campos a modificar.
	 * @throws BusinessException si la cita no se encuentra.
	 */
	@Override
	public void partialModificationAppointment(@Valid Appointment appointment) throws BusinessException {
		log.debug("Init updateAppointment");
		try {
			Optional<Appointment> opt = appointmentRepository.getAppointment(appointment.getId());
			if (!opt.isPresent()) {
				throw new BusinessException(Errors.APPOINTMENT_NOT_FOUND);
			}
			Appointment appointmentUpdated = opt.get();
			appointmentPatchMapper.update(appointmentUpdated, appointment);
			appointmentRepository.modifyAppointment(appointmentUpdated);
			kafkaProducer.eventModifyAppointment(appointmentUpdated);
			log.debug("End updateAppointment");
		} catch (BusinessException e) {
			log.error("Error modificando la cita", e);
			throw e;
		}
	}

	/**
	 * Actualiza una cita existente.
	 *
	 * @param appointment Cita actualizada.
	 * @throws BusinessException si la cita no se encuentra.
	 */
	@Override
	public void updateAppointment(@Valid Appointment appointment) throws BusinessException {
		log.debug("Init updateAppointment");
		try {
			Optional<Appointment> opt = appointmentRepository.getAppointment(appointment.getId());
			if (!opt.isPresent()) {
				throw new BusinessException(Errors.APPOINTMENT_NOT_FOUND);
			}
			appointmentRepository.modifyAppointment(appointment);
			kafkaProducer.eventModifyAppointment(appointment);
			log.debug("End updateAppointment");
		} catch (BusinessException e) {
			log.error("Error actualizando la cita", e);
			throw e;
		}
	}

	/**
	 * Elimina una cita por su ID.
	 *
	 * @param idAppointment ID de la cita a eliminar.
	 * @throws BusinessException si la cita no se encuentra.
	 */
	@Override
	public void deleteAppointment(@Valid String idAppointment) throws BusinessException {
		log.debug("Init deleteAppointment");
		try {
			Optional<Appointment> appointmentSavedOpt = appointmentRepository.getAppointment(idAppointment);
			if (!appointmentSavedOpt.isPresent()) {
				throw new BusinessException(Errors.APPOINTMENT_NOT_FOUND);
			}
			appointmentRepository.deleteAppointment(idAppointment);
			kafkaProducer.eventDeleteAppointment(appointmentSavedOpt.get());
		} catch (BusinessException e) {
			log.error("Error eliminando la cita", e);
			throw e;
		}
	}

	/**
	 * Busca citas basándose en un criterio específico.
	 *
	 * @param typeSearch Tipo de criterio de búsqueda.
	 * @param value      Valor a buscar.
	 * @param pageable   Información de paginación.
	 * @return Página de citas que coinciden con el criterio de búsqueda.
	 * @throws BusinessException
	 */
	@Override
	public Page<Appointment> searchAppointmentBy(@Valid String typeSearch, @Valid String value,
			@Valid Pageable pageable) throws BusinessException {
		log.debug("Init searchAppointmentBy");
		try {
			return appointmentRepository.searchAppointmentBy(typeSearch, value, pageable);
		} catch (BusinessException e) {
			log.error("Error obteniendo la cita", e);
			throw e;
		}
	}

}
