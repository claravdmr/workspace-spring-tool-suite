package com.example.mechanical_workshop.infrastructure.repository.mongodb.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.example.mechanical_workshop.application.ports.output.VehiclesRepositoryOutputPort;
import com.example.mechanical_workshop.domain.exception.BusinessException;
import com.example.mechanical_workshop.domain.model.Vehicle;
import com.example.mechanical_workshop.infrastructure.repository.mongodb.entity.VehicleEntity;
import com.example.mechanical_workshop.infrastructure.repository.mongodb.mapper.VehicleToVehicleEntityMapper;

import jakarta.validation.Valid;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio para gestionar vehículos en el repositorio
 * MongoDB.
 */
@Slf4j
@Component
public class VehiclesRepositoryService implements VehiclesRepositoryOutputPort {

	@Autowired
	VehicleRepository vehicleRepository;

	@Autowired
	VehicleToVehicleEntityMapper vehiclesMapper;

	@Autowired
	CustomerRepository customerRepository;

	/**
	 * Obtiene una lista paginada de vehículos.
	 *
	 * @param pageable Información de paginación.
	 * @return Página de objetos Vehicle.
	 * @throws BusinessException
	 */
	@Override
	@Cacheable(value = "vehicles", key = "#pageable")
	public Page<Vehicle> listVehicles(Pageable pageable) throws BusinessException {
		log.debug("listVehicles");
		try {
			Page<VehicleEntity> vehicles = vehicleRepository.findByEliminado(false, pageable);
			return vehiclesMapper.fromOutputToInput(vehicles);
		} catch (Exception e) {
			log.error("Error al obtener los vehículos", e);
			throw new BusinessException("Error al obtener los vehículos");
		}
	}

	/**
	 * Obtiene un vehículo por su ID.
	 *
	 * @param id ID del vehículo.
	 * @return Optional que contiene al Vehicle si se encuentra, de lo contrario
	 *         vacío.
	 * @throws BusinessException
	 */
	@Override
	@Cacheable(value = "vehicles", key = "#id")
	public Optional<Vehicle> getVehicle(String id) throws BusinessException {
		log.debug("getVehicle");
		try {
			Optional<VehicleEntity> vehicleEntityOpt = vehicleRepository.findByEliminadoAndId(false, id);
			if (vehicleEntityOpt.isEmpty()) {
				return Optional.empty();
			}
			return vehiclesMapper.fromOutputToInput(vehicleEntityOpt);
		} catch (Exception e) {
			log.error("Error al obtener el vehículo", e);
			throw new BusinessException("Error al obtener el vehículo");
		}
	}

	/**
	 * Guarda un nuevo vehículo.
	 *
	 * @param vehicle Vehículo a guardar.
	 * @return ID del vehículo guardado.
	 * @throws BusinessException si ya existe un vehículo con la misma placa de
	 *                           matrícula.
	 */
	@Override
	@CacheEvict(value = "vehicles", allEntries = true)
	public String saveVehicle(@Valid Vehicle vehicle) throws BusinessException {
		log.debug("saveVehicle");
		try {
			Optional<VehicleEntity> existingVehicle = vehicleRepository
					.findByLicensePlateIgnoreCase(vehicle.getLicensePlate());

			if (existingVehicle.isPresent()) {
				throw new BusinessException(
						"El vehículo con la placa de matrícula " + vehicle.getLicensePlate() + " ya existe.");
			}
			existingVehicle = vehicleRepository.findByVinIgnoreCase(vehicle.getVin());

			if (existingVehicle.isPresent()) {
				throw new BusinessException("El vehículo con el bastidor " + vehicle.getVin() + " ya existe.");
			}
			VehicleEntity vehicleSaved = vehiclesMapper.fromInputToOutput(vehicle);
			return vehicleRepository.save(vehicleSaved).getId();
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			log.error("Error al guardar el vehículo", e);
			throw new BusinessException("Error al guardar el vehículo");
		}
	}

	/**
	 * Modifica un vehículo existente.
	 *
	 * @param vehicle Vehículo a modificar.
	 */
	@SneakyThrows
	@Override
	@CacheEvict(value = "vehicles", allEntries = true)
	public void modifyVehicle(@Valid Vehicle vehicle) throws BusinessException {
		log.debug("modifyVehicle");
		try {
			VehicleEntity vehicleSaved = vehiclesMapper.fromInputToOutput(vehicle);
			vehicleRepository.save(vehicleSaved);
		} catch (Exception e) {
			log.error("Error al modificar el vehículo", e);
			throw new BusinessException("Error al modificar el vehículo");
		}
	}

	/**
	 * Elimina lógicamente un vehículo por su ID.
	 *
	 * @param id ID del vehículo a eliminar.
	 * @throws BusinessException
	 */
	@Override
	@CacheEvict(value = "vehicles", allEntries = true)
	public void deleteVehicle(String id) throws BusinessException {
		log.debug("deleteVehicle");
		try {
			Optional<VehicleEntity> opt = vehicleRepository.findByEliminadoAndId(false, id);
			if (opt.isPresent()) {
				opt.get().setEliminado(true);
				vehicleRepository.save(opt.get());
			}
		} catch (Exception e) {
			log.error("Error al eliminar el vehículo", e);
			throw new BusinessException("Error al eliminar el vehículo");
		}
	}

	/**
	 * Busca vehículos basándose en un criterio específico.
	 *
	 * @param typeSearch Tipo de criterio de búsqueda ("licensePlate" o "vin").
	 * @param value      Valor a buscar.
	 * @param pageable   Información de paginación.
	 * @return Página de objetos Vehicle que coinciden con el criterio de búsqueda.
	 * @throws BusinessException
	 * @throws IllegalArgumentException si el formato del número de vehículo es
	 *                                  inválido.
	 */
	@Override
	@Cacheable(value = "vehicles", key = "#typeSearch")
	public Page<Vehicle> searchVehicleBy(@Valid String typeSearch, @Valid String value, Pageable pageable)
			throws BusinessException {
		log.debug("searchCustomerBy");
		Page<VehicleEntity> vehicleEntityOpt;
		try {
			if (typeSearch.equals("licensePlate")) {
				vehicleEntityOpt = vehicleRepository.findByEliminadoAndLicensePlateIgnoreCase(false,
						value.toUpperCase(), pageable);
			} else {
				vehicleEntityOpt = vehicleRepository.findByEliminadoAndVinIgnoreCase(false, value.toLowerCase(),
						pageable);
			}
			return vehiclesMapper.fromOutputToInput(vehicleEntityOpt);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Formato de búsqueda no válido");
		} catch (Exception e) {
			log.error("Error al buscar el vehículo", e);
			throw new BusinessException("Error al buscar el vehículo");
		}
	}

}
