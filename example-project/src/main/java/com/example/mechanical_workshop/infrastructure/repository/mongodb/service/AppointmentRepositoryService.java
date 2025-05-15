package com.example.mechanical_workshop.infrastructure.repository.mongodb.service;

import java.util.Optional;

import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.example.mechanical_workshop.application.ports.output.AppointmentsRepositoryOutputPort;
import com.example.mechanical_workshop.domain.exception.BusinessException;
import com.example.mechanical_workshop.domain.model.Appointment;
import com.example.mechanical_workshop.infrastructure.repository.mongodb.entity.AppointmentEntity;
import com.example.mechanical_workshop.infrastructure.repository.mongodb.mapper.AppointmentToAppointmentEntityMapper;

import jakarta.validation.Valid;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio para gestionar citas de clientes en el
 * repositorio MongoDB.
 */

@Slf4j
@Component
public class AppointmentRepositoryService implements AppointmentsRepositoryOutputPort {

	@Autowired
	AppointmentRepository appointmentRepository;

	@Autowired
	AppointmentToAppointmentEntityMapper appointmentMapper;

	/**
	 * Obtiene una lista paginada de citas del repositorio.
	 *
	 * @param pageable Información de paginación.
	 * @return Página de citas recuperadas del repositorio.
	 * @throws BusinessException
	 */
	@Override
	@Cacheable(value = "appointments", key = "#pageable")
	public Page<Appointment> listAppointments(@Valid Pageable pageable) throws BusinessException {
		log.debug("listAppointments");
		try {
			Page<AppointmentEntity> appointments = appointmentRepository.findByEliminado(false, pageable);
			return appointmentMapper.fromOutputToInput(appointments);
		} catch (Exception e) {
			log.error("Error obteniendo citas", e);
			throw new BusinessException("Error obteniendo citas");
		}
	}

	/**
	 * Obtiene una cita específica por su ID.
	 *
	 * @param id ID de la cita a recuperar.
	 * @return Optional que contiene la cita encontrada, o vacío si no existe.
	 * @throws BusinessException
	 */
	@Override
	@Cacheable(value = "appointments", key = "#id")
	public Optional<Appointment> getAppointment(@Valid String id) throws BusinessException {
		log.debug("getAppointment");
		try {
			Optional<AppointmentEntity> appointmentEntityOpt = appointmentRepository.findByEliminadoAndId(false, id);
			if (appointmentEntityOpt.isEmpty()) {
				return Optional.empty();
			}
			return appointmentMapper.fromOutputToInput(appointmentEntityOpt);
		} catch (ResourceNotFoundException e) {
			throw e; // Re-lanzar excepción específica de negocio
		} catch (Exception e) {
			log.error("Error obteniendo la cita con id: " + id, e);
			throw new BusinessException("Error obteniendo la cita con id: " + id);
		}
	}

	/**
	 * Guarda una nueva cita en el repositorio.
	 *
	 * @param appointment Cita a guardar.
	 * @return ID de la cita guardada.
	 * @throws BusinessException
	 */
	@Override
	@CacheEvict(value = "appointments", allEntries = true)
	public String saveAppointment(@Valid Appointment appointment) throws BusinessException {
		log.debug("saveAppointment");
		try {
			AppointmentEntity appointmentSaved = appointmentMapper.fromInputToOutput(appointment);
			return appointmentRepository.save(appointmentSaved).getId();
		} catch (Exception e) {
			log.error("Error guardando la cita", e);
			throw new BusinessException("Error guardando la cita");
		}
	}

	/**
	 * Modifica una cita existente en el repositorio.
	 *
	 * @param appointment Cita modificada.
	 */
	@SneakyThrows
	@Override
	@CacheEvict(value = "appointments", allEntries = true)
	public void modifyAppointment(@Valid Appointment appointment) throws BusinessException {
		log.debug("modifyAppointment");
		try {
			AppointmentEntity appointmentSaved = appointmentMapper.fromInputToOutput(appointment);
			appointmentRepository.save(appointmentSaved);
		} catch (Exception e) {
			log.error("Error modificando la cita", e);
			throw new BusinessException("Error modificando la cita");
		}
	}

	/**
	 * Elimina una cita del repositorio por su ID.
	 *
	 * @param id ID de la cita a eliminar.
	 * @throws BusinessException
	 */
	@Override
	@CacheEvict(value = "appointments", allEntries = true)
	public void deleteAppointment(@Valid String id) throws BusinessException {
		log.debug("deleteAppointment");
		try {
			Optional<AppointmentEntity> opt = appointmentRepository.findByEliminadoAndId(false, id);
			if (opt.isPresent()) {
				opt.get().setEliminado(true);
				appointmentRepository.save(opt.get());
			}
		} catch (Exception e) {
			log.error("Error eliminando la cita con id: " + id, e);
			throw new BusinessException("Error eliminando la cita con id: " + id);
		}
	}

	/**
	 * Busca citas por un tipo específico de criterio de búsqueda y valor.
	 *
	 * @param typeSearch Tipo de búsqueda (por ejemplo, "customerEmail",
	 *                   "customerPhoneNumber", "date").
	 * @param value      Valor a buscar.
	 * @param pageable   Información de paginación.
	 * @return Página de citas que cumplen con el criterio de búsqueda especificado.
	 * @throws BusinessException
	 * @throws IllegalArgumentException Si el formato del número de cliente es
	 *                                  inválido.
	 */
	@Override
	@Cacheable(value = "appointments", key = "#typeSearch")
	public Page<Appointment> searchAppointmentBy(@Valid String typeSearch, @Valid String value,
			@Valid Pageable pageable) throws BusinessException {
		log.debug("searchAppointmentBy");
		Page<AppointmentEntity> appointmentEntityOpt;
		try {
			switch (typeSearch) {
			case "customerEmail":
				appointmentEntityOpt = appointmentRepository.findByEliminadoAndCustomerEmailIgnoreCase(false, value,
						pageable);
				break;
			case "customerPhoneNumber":
				appointmentEntityOpt = appointmentRepository.findByEliminadoAndCustomerPhoneNumberIgnoreCase(false,
						value, pageable);
				break;
			default:
				appointmentEntityOpt = appointmentRepository.findByEliminadoAndDate(false, value, pageable);
				break;
			}
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Formato typeSearch no válido");
		} catch (Exception e) {
			log.error("Error buscando la cita", e);
			throw new BusinessException("Error buscando la cita");
		}
		return appointmentMapper.fromOutputToInput(appointmentEntityOpt);
	}

}
