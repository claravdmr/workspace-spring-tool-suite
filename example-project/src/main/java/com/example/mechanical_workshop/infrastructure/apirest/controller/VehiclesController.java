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

import com.example.mechanical_workshop.application.ports.input.VehiclesServiceInputPort;
import com.example.mechanical_workshop.domain.exception.BusinessException;
import com.example.mechanical_workshop.domain.model.Vehicle;
import com.example.mechanical_workshop.infrastructure.apirest.dto.vehicle.RequestPatchVehicleDto;
import com.example.mechanical_workshop.infrastructure.apirest.dto.vehicle.RequestPostPutVehicleDto;
import com.example.mechanical_workshop.infrastructure.apirest.dto.vehicle.ResponseVehicleDto;
import com.example.mechanical_workshop.infrastructure.apirest.mapper.vehicle.VehicleToPatchVehicleDtoMapper;
import com.example.mechanical_workshop.infrastructure.apirest.mapper.vehicle.VehicleToPostPutVehicleDtoMapper;
import com.example.mechanical_workshop.infrastructure.apirest.mapper.vehicle.VehicleToVehicleDtoMapper;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador REST para gestionar vehículos en un taller mecánico.
 */
@Slf4j
@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("/vehicles")
public class VehiclesController {

	@Autowired
	VehiclesServiceInputPort vehiclesService;

	@Autowired
	VehicleToVehicleDtoMapper vehicleToVehicleDtoMapper;

	@Autowired
	VehicleToPatchVehicleDtoMapper vehicleToPatchVehicleDtoMapper;

	@Autowired
	VehicleToPostPutVehicleDtoMapper vehicleToPostPutVehicleDtoMapper;

	/**
	 * Maneja las peticiones GET para obtener todos los vehículos.
	 *
	 * @param pageable Objeto Pageable para paginación.
	 * @return ResponseEntity con estado 200 (OK) y una página de
	 *         ResponseVehicleDto. En caso de error, retorna ResponseEntity con
	 *         estado 400 (Bad Request) y mensaje de error.
	 */
	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping
	public ResponseEntity getVehicles(Pageable pageable) {
		log.debug("getVehicles");
		Page<Vehicle> vehiclesDomain;
		try {
			vehiclesDomain = vehiclesService.getVehicles(pageable);
			// return
			// ResponseEntity.ok(vehicleToVehicleDtoMapper.fromInputToOutput(vehiclesDomain));
			List<ResponseVehicleDto> listDto = new ArrayList<>();
			vehiclesDomain.getContent().forEach(vehicle -> {
				ResponseVehicleDto vehicleDto = vehicleToVehicleDtoMapper.fromInputToOutput(vehicle);
				listDto.add(vehicleDto);
			});
			return ResponseEntity.ok(new PageImpl<>(listDto, pageable, listDto.size()));
		} catch (BusinessException e) {
			log.error("Error obteniendo vehículos", e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	/**
	 * Maneja las peticiones GET para obtener un vehículo específico por su ID.
	 *
	 * @param idVehicle ID del vehículo a buscar.
	 * @return ResponseEntity con estado 200 (OK) y el vehículo encontrado, o
	 *         ResponseEntity con estado 204 (No Content) si no se encuentra.
	 */
	@GetMapping("/{id-vehicle}")
	public ResponseEntity getVehicle(@Valid @PathVariable("id-vehicle") final String idVehicle) {
		log.debug("getVehicle");
		try {
			Optional<Vehicle> vehicleDomain = vehiclesService.getVehicle(idVehicle);
			if (vehicleDomain.isPresent()) {
				return ResponseEntity.ok(vehicleToVehicleDtoMapper.fromInputToOutput(vehicleDomain.get()));
			} else {
				return ResponseEntity.noContent().build();
			}
		} catch (BusinessException e) {
			log.error("Error obteniendo el vehículo", e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	/**
	 * Maneja las peticiones GET para buscar vehículos según un criterio específico.
	 *
	 * @param typeSearch Tipo de búsqueda (por ejemplo, "licensePlate", "vin").
	 * @param value      Valor a buscar.
	 * @param pageable   Objeto Pageable para paginación.
	 * @return ResponseEntity con estado 200 (OK) y una página de vehículos que
	 *         coinciden con el criterio de búsqueda, o ResponseEntity con estado
	 *         400 (Bad Request) si el tipo de búsqueda es inválido, o
	 *         ResponseEntity con estado 500 (Internal Server Error) si ocurre un
	 *         error durante la búsqueda.
	 */
	@GetMapping("/search/{type-search}")
	public ResponseEntity searchVehicleBy(@Valid @PathVariable("type-search") final String typeSearch,
			@Valid @RequestParam("value") final String value, @Valid Pageable pageable) {
		log.debug("searchVehicleBy");
		try {
			Page<Vehicle> vehicle = vehiclesService.searchVehicleBy(typeSearch, value, pageable);
			/*
			 * if (vehicle.isPresent()) { return ResponseEntity.ok(vehicle); } else { return
			 * ResponseEntity.noContent().build(); }
			 */
			return ResponseEntity.ok(vehicleToVehicleDtoMapper.fromInputToOutput(vehicle));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body("Tipo de búsqueda inválido");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error al buscar vehículos");
		}
	}

	/**
	 * Maneja las peticiones POST para crear un nuevo vehículo.
	 *
	 * @param vehicle Datos del vehículo en formato RequestPostPutVehicleDto.
	 * @return ResponseEntity con estado 201 (Creado) y URI del recurso creado. En
	 *         caso de conflicto, retorna ResponseEntity con estado 409 (Conflict) y
	 *         mensaje de error.
	 * @throws BusinessException Si hay un error al crear el vehículo.
	 */
	@PostMapping
	public ResponseEntity postVehicle(@Valid @RequestBody RequestPostPutVehicleDto vehicle) throws BusinessException {
		log.debug("postVehicle");
		try {
			Vehicle vehicleToSave = vehicleToPostPutVehicleDtoMapper.fromOutputToInput(vehicle);
			String idVehicle = vehiclesService.createVehicle(vehicleToSave);
			URI locationHeader = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(idVehicle).toUri();
			return ResponseEntity.created(locationHeader).build();
		} catch (BusinessException e) {
			log.error("Error creando vehículo", e);
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		}
	}

	/**
	 * Maneja las peticiones DELETE para eliminar un vehículo por su ID.
	 *
	 * @param idVehicle ID del vehículo a eliminar.
	 * @return ResponseEntity con estado 204 (No Content) si la eliminación es
	 *         exitosa, o ResponseEntity con estado 400 (Bad Request) si hay un
	 *         error.
	 */
	@DeleteMapping("/{id-vehicle}")
	public ResponseEntity deleteVehicle(@Valid @PathVariable("id-vehicle") final String idVehicle) {
		log.debug("deleteVehicle");
		try {
			vehiclesService.deleteVehicle(idVehicle);
		} catch (BusinessException e) {
			log.error("Error eliminando vehículo", e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.noContent().build();
	}

	/**
	 * Maneja las peticiones PATCH para modificar parcialmente un vehículo
	 * existente.
	 *
	 * @param idVehicle ID del vehículo a modificar.
	 * @param vehicle   Datos actualizados del vehículo en formato
	 *                  RequestPatchVehicleDto.
	 * @return ResponseEntity con estado 200 (OK) y el vehículo modificado, o
	 *         ResponseEntity con estado 400 (Bad Request) si hay un error.
	 */
	@PatchMapping("/{id-vehicle}")
	public ResponseEntity patchVehicle(@Valid @PathVariable("id-vehicle") final String idVehicle,
			@Valid @RequestBody RequestPatchVehicleDto vehicle) {
		log.debug("patchVehicle");
		try {
			Optional<Vehicle> vehicleSavedOpt = vehiclesService.getVehicle(idVehicle);
			Vehicle vehicleSaved = vehicleSavedOpt.get();

			Vehicle vehicleDomain = vehicleToPatchVehicleDtoMapper.fromOutputToInput(vehicle);
			vehicleDomain.setId(idVehicle);
			vehiclesService.partialModificationVehicle(vehicleDomain);

			// Si se ha cambiado el propietario
			if (!vehicleSaved.getIdCustomer().equals(vehicle.getIdCustomer())) {
				try {
					vehiclesService.changeVehicleCustomer(vehicleSaved, vehicleDomain);
				} catch (BusinessException e) {
					log.debug("Error modificando el cliente", e);
					return ResponseEntity.badRequest().body(e.getMessage());
				}
			}
			// URI locationHeader =
			// ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand().toUri();
			return ResponseEntity.ok(vehicleDomain);
		} catch (BusinessException e) {
			log.debug("Error modificando vehículo", e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	/**
	 * Maneja las peticiones PUT para actualizar completamente un vehículo
	 * existente.
	 *
	 * @param idVehicle ID del vehículo a actualizar.
	 * @param vehicle   Datos actualizados del vehículo en formato
	 *                  RequestPostPutVehicleDto.
	 * @return ResponseEntity con estado 204 (No Content) si la actualización es
	 *         exitosa, o ResponseEntity con estado 400 (Bad Request) si hay un
	 *         error.
	 */
	@PutMapping("/{id-vehicle}")
	public ResponseEntity putVehicle(@Valid @PathVariable("id-vehicle") final String idVehicle,
			@Valid @RequestBody RequestPostPutVehicleDto vehicle) {
		log.debug("putVehicle");

		Vehicle domain = vehicleToPostPutVehicleDtoMapper.fromOutputToInput(vehicle);
		domain.setId(idVehicle);

		try {
			vehiclesService.updateVehicle(domain);
		} catch (BusinessException e) {
			log.debug("Error modificando vehículo", e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}

		// URI locationHeader =
		// ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand().toUri();
		return ResponseEntity.noContent().build();
	}

}
