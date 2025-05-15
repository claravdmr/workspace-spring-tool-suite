package com.example.mechanical_workshop.application.service;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mechanical_workshop.application.ports.input.VehiclesServiceInputPort;
import com.example.mechanical_workshop.application.ports.output.CustomersMessageOutputPort;
import com.example.mechanical_workshop.application.ports.output.CustomersRepositoryOutputPort;
import com.example.mechanical_workshop.application.ports.output.VehiclesMessageOutputPort;
import com.example.mechanical_workshop.application.ports.output.VehiclesRepositoryOutputPort;
import com.example.mechanical_workshop.application.util.Constants;
import com.example.mechanical_workshop.application.util.Errors;
import com.example.mechanical_workshop.domain.exception.BusinessException;
import com.example.mechanical_workshop.domain.mapper.VehiclePatchMapper;
import com.example.mechanical_workshop.domain.model.Customer;
import com.example.mechanical_workshop.domain.model.Vehicle;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio para gestionar vehículos en la aplicación.
 */
@Slf4j
@Service
public class VehiclesService implements VehiclesServiceInputPort {

	@Autowired
	VehiclesRepositoryOutputPort vehiclesRepository;

	@Autowired
	VehiclesMessageOutputPort kafkaProducer;

	@Autowired
	VehiclePatchMapper vehiclePatchMapper;

	@Autowired
	CustomersMessageOutputPort customersMessageOutputPort;

	@Autowired
	CustomersRepositoryOutputPort customersRepositoryOutputPort;

	/**
	 * Crea un nuevo vehículo y lo asocia a un cliente existente.
	 *
	 * @param vehicle Vehículo a crear.
	 * @return ID del vehículo creado.
	 * @throws BusinessException si el cliente especificado no existe.
	 */
	@Override
	@Transactional
	public String createVehicle(@Valid Vehicle vehicle) throws BusinessException {
		log.debug("Init createVehicle");
		String vehicleId = "";
		Optional<Customer> customerOpt = customersRepositoryOutputPort.getCustomer(vehicle.getIdCustomer());
		if (!customerOpt.isPresent()) {
			throw new BusinessException("El cliente con ID " + vehicle.getIdCustomer() + " no existe.");
		}
		try {
			vehicleId = vehiclesRepository.saveVehicle(vehicle);
			/*
			 * if (vehicleId == null) { throw new BusinessException(
			 * "El vehículo con la placa de matrícula " + vehicle.getLicensePlate() +
			 * " ya existe."); }
			 */
			vehicle.setId(vehicleId);
			kafkaProducer.eventCreateVehicle(vehicle);

			// Agrego el ID del nuevo vehículo a la lista de vehículos del cliente
			Customer customerToSave = customerOpt.get();
			if (customerToSave.getIdVehicles() == null) {
				customerToSave.setIdVehicles(new ArrayList<>());
			}
			customerToSave.getIdVehicles().add(vehicleId);
			customersRepositoryOutputPort.modifyCustomer(customerToSave);
			customersMessageOutputPort.eventModifyCustomer(customerToSave);
		} catch (BusinessException e) {
			log.error("Error al guardar el vehículo: " + vehicle.getLicensePlate(), e);
			throw e;
		}
		log.debug("End createVehicle");
		return vehicleId;
	}

	/**
	 * Obtiene todos los vehículos paginados.
	 *
	 * @param pageable Información de paginación.
	 * @return Página de vehículos.
	 * @throws BusinessException si se excede el máximo de paginación permitido.
	 */
	@Override
	public Page<Vehicle> getVehicles(@Valid Pageable pageable) throws BusinessException {
		log.debug("Init getVehicles");
		try {
			if (pageable.getPageSize() > Constants.MAXIMUM_PAGINATION) {
				throw new BusinessException(Errors.MAXIMUM_PAGINATION_EXCEEDED);
			}
			return vehiclesRepository.listVehicles(pageable);
		} catch (BusinessException e) {
			log.error("Error obteniendo los vehículos", e);
			throw e;
		}
	}

	/**
	 * Obtiene un vehículo por su ID.
	 *
	 * @param idVehicle ID del vehículo.
	 * @return Optional que contiene el vehículo si se encuentra, de lo contrario
	 *         vacío.
	 * @throws BusinessException
	 */
	@Override
	public Optional<Vehicle> getVehicle(@Valid String idVehicle) throws BusinessException {
		log.debug("Init getVehicle");
		try {
			Optional<Vehicle> vehicleOpt = vehiclesRepository.getVehicle(idVehicle);
			if (!vehicleOpt.isPresent()) {
				throw new BusinessException(Errors.VEHICLE_NOT_FOUND);
			}
			return vehicleOpt;
		} catch (BusinessException e) {
			log.error("Error al obtener el vehículo", e);
			throw e;
		}
	}

	/**
	 * Realiza una modificación parcial de un vehículo existente.
	 *
	 * @param vehicle Vehículo con los campos a modificar.
	 * @throws BusinessException si el vehículo no se encuentra.
	 */
	@Override
	public void partialModificationVehicle(@Valid Vehicle vehicle) throws BusinessException {
		log.debug("Init partialModificationVehicle");
		try {
			Optional<Vehicle> opt = vehiclesRepository.getVehicle(vehicle.getId());
			if (!opt.isPresent()) {
				throw new BusinessException(Errors.VEHICLE_NOT_FOUND);
			}
			Vehicle vehicleUpdated = opt.get();
			vehiclePatchMapper.update(vehicleUpdated, vehicle);
			vehiclesRepository.modifyVehicle(vehicleUpdated);
			kafkaProducer.eventModifyVehicle(vehicleUpdated);
			log.debug("End partialModificationVehicle");
		} catch (BusinessException e) {
			log.error("Error al modificar parcialmente el vehículo", e);
			throw e;
		}
	}

	/**
	 * Actualiza un vehículo existente.
	 *
	 * @param vehicle Vehículo actualizado.
	 * @throws BusinessException si el vehículo no se encuentra.
	 */
	@Override
	public void updateVehicle(@Valid Vehicle vehicle) throws BusinessException {
		log.debug("Init updateVehicle");
		try {
			Optional<Vehicle> opt = vehiclesRepository.getVehicle(vehicle.getId());
			if (!opt.isPresent()) {
				throw new BusinessException(Errors.VEHICLE_NOT_FOUND);
			}
			vehiclesRepository.modifyVehicle(vehicle);
			kafkaProducer.eventModifyVehicle(vehicle);
			log.debug("End updateVehicle");
		} catch (BusinessException e) {
			log.error("Error al actualizar el vehículo", e);
			throw e;
		}
	}

	/**
	 * Elimina un vehículo por su ID.
	 *
	 * @param idVehicle ID del vehículo a eliminar.
	 * @throws BusinessException si el vehículo no se encuentra.
	 */
	@Override
	@Transactional
	public void deleteVehicle(@Valid String idVehicle) throws BusinessException {
		log.debug("Init deleteVehicle");
		try {
			Optional<Vehicle> vehicleOpt = vehiclesRepository.getVehicle(idVehicle);
			if (!vehicleOpt.isPresent()) {
				throw new BusinessException(Errors.VEHICLE_NOT_FOUND);
			}
			// Elimino el vehículo de la lista de vehículos de su propietario
			Vehicle vehicle = vehicleOpt.get();
			Optional<Customer> customerOpt = customersRepositoryOutputPort.getCustomer(vehicle.getIdCustomer());
			if (customerOpt.isPresent()) {
				Customer customer = customerOpt.get();
				customer.getIdVehicles().remove(vehicle.getId());
				log.debug(customer.toString());
				customersRepositoryOutputPort.modifyCustomer(customer);
			}
			vehiclesRepository.deleteVehicle(idVehicle);
			kafkaProducer.eventDeleteVehicle(vehicle);
		} catch (BusinessException e) {
			log.error("Error al guardar el vehículo", e);
			throw e;
		}
	}

	/**
	 * Cambia el propietario de un vehículo.
	 *
	 * @param vehicleSaved    Vehículo con el propietario actual.
	 * @param vehicleModified Vehículo con el nuevo propietario.
	 * @throws BusinessException si hay errores al modificar los clientes asociados.
	 */
	@Override
	@Transactional
	public void changeVehicleCustomer(@Valid Vehicle vehicleSaved, @Valid Vehicle vehicleModified)
			throws BusinessException {
		log.debug("Init changeVehicleCustomer");
		try {

			// Quito el vehiculo del propietario anterior
			Optional<Customer> previousCustomerOpt = customersRepositoryOutputPort
					.getCustomer(vehicleSaved.getIdCustomer());
			if (previousCustomerOpt.isPresent()) {
				Customer previousCustomer = previousCustomerOpt.get();
				previousCustomer.getIdVehicles().remove(vehicleSaved.getId());
				customersRepositoryOutputPort.modifyCustomer(previousCustomer);
				customersMessageOutputPort.eventModifyCustomer(previousCustomer);
			}

			// Añado el vehiculo al nuevo propietario
			Optional<Customer> newCustomerOpt = customersRepositoryOutputPort
					.getCustomer(vehicleModified.getIdCustomer());
			if (newCustomerOpt.isPresent()) {
				Customer newCustomer = newCustomerOpt.get();
				if (newCustomer.getIdVehicles() == null) {
					newCustomer.setIdVehicles(new ArrayList<>());
				}
				newCustomer.getIdVehicles().add(vehicleSaved.getId());
				customersRepositoryOutputPort.modifyCustomer(newCustomer);
				customersMessageOutputPort.eventModifyCustomer(newCustomer);
			}
		} catch (BusinessException e) {
			log.error("Error al cambiar el propietario del vehículo", e);
			throw e;
		}
	}

	/**
	 * Busca vehículos basándose en un criterio específico.
	 *
	 * @param typeSearch Tipo de criterio de búsqueda.
	 * @param value      Valor a buscar.
	 * @param pageable   Información de paginación.
	 * @return Página de vehículos que coinciden con el criterio de búsqueda.
	 * @throws BusinessException
	 */
	@Override
	public Page<Vehicle> searchVehicleBy(@Valid String typeSearch, @Valid String value, @Valid Pageable pageable)
			throws BusinessException {
		log.debug("Init searchVehicleBy");
		try {
			return vehiclesRepository.searchVehicleBy(typeSearch, value, pageable);
		} catch (BusinessException e) {
			log.error("Error al buscar el vehículo", e);
			throw new BusinessException("Error al buscar el vehículo");
		}
	}

	/**
	 * Elimina una orden de trabajo de la lista de órdenes del vehículo.
	 *
	 * @param idWorkOrder Id de la orden de trabajo a eliminar .
	 * @param vehicle     Vehículo a modificar.
	 * @throws BusinessException
	 */
	@Override
	public void deleteWorkOrder(@Valid String idWorkOrder, @Valid Vehicle vehicle) throws BusinessException {
		log.debug("Init deleteWorkOrder");
		try {
			vehicle.getIdWorkOrders().remove(idWorkOrder);
			vehiclesRepository.modifyVehicle(vehicle);
			kafkaProducer.eventModifyVehicle(vehicle);
		} catch (BusinessException e) {
			log.error("Error al eliminar orden del vehículo", e);
			throw new BusinessException("Error al eliminar orden del vehículo");
		}
	}

	/**
	 * Elimina una orden de trabajo de la lista de órdenes del vehículo.
	 *
	 * @param idWorkOrder Id de la orden de trabajo a eliminar .
	 * @param vehicle     Vehículo a modificar.
	 * @throws BusinessException
	 */
	@Override
	public void addWorkOrder(@Valid String idWorkOrder, @Valid Vehicle vehicle) throws BusinessException {
		log.debug("Init addWorkOrder");
		try {
			vehicle.getIdWorkOrders().add(idWorkOrder);
			vehiclesRepository.modifyVehicle(vehicle);
			kafkaProducer.eventModifyVehicle(vehicle);
		} catch (BusinessException e) {
			log.error("Error al añadir orden al vehículo", e);
			throw new BusinessException("Error al añadir orden al vehículo");
		}
	}

}
