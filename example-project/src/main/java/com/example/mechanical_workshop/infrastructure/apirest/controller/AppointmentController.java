package com.example.mechanical_workshop.infrastructure.apirest.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.mechanical_workshop.application.ports.input.AppointmentsServiceInputPort;
import com.example.mechanical_workshop.domain.exception.BusinessException;
import com.example.mechanical_workshop.domain.model.Appointment;
import com.example.mechanical_workshop.infrastructure.apirest.dto.appointments.RequestPatchAppointmentDto;
import com.example.mechanical_workshop.infrastructure.apirest.dto.appointments.RequestPostPutAppointmentDto;
import com.example.mechanical_workshop.infrastructure.apirest.dto.appointments.ResponseAppointmentDto;
import com.example.mechanical_workshop.infrastructure.apirest.mapper.appointment.AppointmentToAppointmentDtoMapper;
import com.example.mechanical_workshop.infrastructure.apirest.mapper.appointment.AppointmentToPatchAppointmentDtoMapper;
import com.example.mechanical_workshop.infrastructure.apirest.mapper.appointment.AppointmentToPostPutAppointmentDtoMapper;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador REST para gestionar citas en un taller mecánico.
 */
@Slf4j
@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("/appointments")
public class AppointmentController {

	@Autowired
	AppointmentsServiceInputPort appointmentService;

	@Autowired
	AppointmentToAppointmentDtoMapper appointmentDtoMapper;

	@Autowired
	AppointmentToPatchAppointmentDtoMapper appointmentToPatchAppointmentDtoMapper;

	@Autowired
	AppointmentToPostPutAppointmentDtoMapper appointmentToPostPutAppointmentDtoMapper;

	/**
	 * Maneja las peticiones POST para crear una nueva cita.
	 *
	 * @param appointment Datos de la cita en formato RequestPostPutAppointmentDto.
	 * @return ResponseEntity con estado 201 (Creado) y URI del recurso creado.
	 */
	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping
	public ResponseEntity postAppointment(@Valid @RequestBody RequestPostPutAppointmentDto appointment) {
		log.debug("postAppointment");
		Appointment appointmentToSave = appointmentToPostPutAppointmentDtoMapper.fromOutputToInput(appointment);
		try {
			String idAppointment = appointmentService.createAppointment(appointmentToSave);
			URI locationHeader = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(idAppointment).toUri();
			return ResponseEntity.created(locationHeader).build();
		} catch (BusinessException e) {
			log.error("Error creando la cita", e);
			return ResponseEntity.badRequest().body("Error creando la cita: " + e.getMessage());
		}
	}

	/**
	 * Maneja las peticiones GET para obtener todas las citas.
	 *
	 * @param pageable Objeto Pageable para paginación.
	 * @return ResponseEntity con estado 200 (OK) y una página de
	 *         ResponseAppointmentDto.
	 * @throws BusinessException Si hay un error al recuperar las citas.
	 */
	@GetMapping
	public ResponseEntity getAppointments(@Valid Pageable pageable) throws BusinessException {
		log.debug("getAppointments");
		Page<Appointment> appointmentsDomain;
		try {
			appointmentsDomain = appointmentService.getAppointments(pageable);
		} catch (BusinessException e) {
			log.error("Error obteniendo citas", e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		// return
		// ResponseEntity.ok(appointmentDtoMapper.fromInputToOutput(appointmentsDomain));
		List<ResponseAppointmentDto> listDto = new ArrayList<>();
		appointmentsDomain.getContent().forEach(appointment -> {
			ResponseAppointmentDto appointmentDto = appointmentDtoMapper.fromInputToOutput(appointment);
			listDto.add(appointmentDto);
		});
		return ResponseEntity.ok(new PageImpl<>(listDto, pageable, listDto.size()));
	}

	/**
	 * Maneja las peticiones GET para obtener una cita específica por su ID.
	 *
	 * @param idAppointment ID de la cita a buscar.
	 * @return ResponseEntity con estado 200 (OK) y la cita encontrada, o
	 *         ResponseEntity con estado 204 (No Content) si no se encuentra.
	 */
	@GetMapping("/{id-appointment}")
	public ResponseEntity getAppointment(@Valid @PathVariable("id-appointment") final String idAppointment) {
		log.debug("getAppointment");
		Optional<Appointment> appointmentOpt;
		try {
			appointmentOpt = appointmentService.getAppointment(idAppointment);
			if (appointmentOpt.isPresent()) {
				return ResponseEntity.ok(appointmentDtoMapper.fromInputToOutput(appointmentOpt.get()));
			} else {
				return ResponseEntity.noContent().build();
			}
		} catch (BusinessException e) {
			log.error("Error obteniendo la cita con id: " + idAppointment, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error obteniendo la cita: " + e.getMessage());
		}

	}

	/**
	 * Maneja las peticiones DELETE para eliminar una cita por su ID.
	 *
	 * @param idAppointment ID de la cita a eliminar.
	 * @return ResponseEntity con estado 204 (No Content) si la eliminación es
	 *         exitosa, o ResponseEntity con estado 400 (Bad Request) si hay un
	 *         error.
	 */
	@DeleteMapping("/{id-appointment}")
	public ResponseEntity deleteAppointment(@Valid @PathVariable("id-appointment") final String idAppointment) {
		log.debug("deleteAppointment");
		try {
			appointmentService.deleteAppointment(idAppointment);
		} catch (BusinessException e) {
			log.error("Error eliminando cita", e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.noContent().build();
	}

	/**
	 * Maneja las peticiones PATCH para modificar parcialmente una cita existente.
	 *
	 * @param idAppointment ID de la cita a modificar.
	 * @param appointment   Datos actualizados de la cita en formato
	 *                      RequestPatchAppointmentDto.
	 * @return ResponseEntity con estado 200 (OK) y la cita modificada, o
	 *         ResponseEntity con estado 400 (Bad Request) si hay un error.
	 */
	@PatchMapping("/{id-appointment}")
	public ResponseEntity patchAppointment(@Valid @PathVariable("id-appointment") final String idAppointment,
			@Valid @RequestBody RequestPatchAppointmentDto appointment) {
		log.debug("patchAppointment");

		Appointment appointmentDomain = appointmentToPatchAppointmentDtoMapper.fromOutputToInput(appointment);
		appointmentDomain.setId(idAppointment);

		try {
			appointmentService.partialModificationAppointment(appointmentDomain);
		} catch (BusinessException e) {
			log.debug("Error modificando cita", e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}

		// URI locationHeader =
		// ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand().toUri();
		return ResponseEntity.ok(appointmentDomain);
	}

	/**
	 * Maneja las peticiones PUT para actualizar completamente una cita existente.
	 *
	 * @param idAppointment ID de la cita a actualizar.
	 * @param appointment   Datos actualizados de la cita en formato
	 *                      RequestPostPutAppointmentDto.
	 * @return ResponseEntity con estado 204 (No Content) si la actualización es
	 *         exitosa, o ResponseEntity con estado 400 (Bad Request) si hay un
	 *         error.
	 */
	@PutMapping("/{id-appointment}")
	public ResponseEntity putAppointment(@Valid @PathVariable("id-appointment") final String idAppointment,
			@Valid @RequestBody RequestPostPutAppointmentDto appointment) {
		log.debug("putAppointment");

		Appointment domain = appointmentToPostPutAppointmentDtoMapper.fromOutputToInput(appointment);
		domain.setId(idAppointment);

		try {
			appointmentService.updateAppointment(domain);
		} catch (BusinessException e) {
			log.debug("Error modificando cita", e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}

		// URI locationHeader =
		// ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand().toUri();
		return ResponseEntity.noContent().build();
	}

	/**
	 * Maneja las peticiones GET para buscar citas según un criterio específico.
	 *
	 * @param typeSearch Tipo de búsqueda (por ejemplo, "customerEmail",
	 *                   "customerPhoneNumber", "date").
	 * @param value      Valor a buscar.
	 * @param pageable   Objeto Pageable para paginación.
	 * @return ResponseEntity con estado 200 (OK) y una página de citas que
	 *         coinciden con el criterio de búsqueda, o ResponseEntity con estado
	 *         400 (Bad Request) si el tipo de búsqueda es inválido, o
	 *         ResponseEntity con estado 500 (Internal Server Error) si ocurre un
	 *         error durante la búsqueda.
	 */
	@GetMapping("/search/{type-search}")
	public ResponseEntity searchAppointmentBy(@Valid @PathVariable("type-search") final String typeSearch,
			@Valid @RequestParam("value") final String value, @Valid Pageable pageable) {
		log.debug("searchAppointmentBy");
		try {
			Page<Appointment> appointment = appointmentService.searchAppointmentBy(typeSearch, value, pageable);
			return ResponseEntity.ok(appointmentDtoMapper.fromInputToOutput(appointment));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body("Tipo de búsqueda inválido");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error buscando la cita");
		}
	}

}
