package com.example.mechanical_workshop.application.service;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mechanical_workshop.application.ports.input.VehiclesServiceInputPort;
import com.example.mechanical_workshop.application.ports.input.WorkOrdersServiceInputPort;
import com.example.mechanical_workshop.application.ports.output.VehiclesMessageOutputPort;
import com.example.mechanical_workshop.application.ports.output.VehiclesRepositoryOutputPort;
import com.example.mechanical_workshop.application.ports.output.WorkOrdersMessageOutputPort;
import com.example.mechanical_workshop.application.ports.output.WorkOrdersRepositoryOutputPort;
import com.example.mechanical_workshop.application.util.Constants;
import com.example.mechanical_workshop.application.util.Errors;
import com.example.mechanical_workshop.domain.exception.BusinessException;
import com.example.mechanical_workshop.domain.mapper.WorkOrderPatchMapper;
import com.example.mechanical_workshop.domain.model.Vehicle;
import com.example.mechanical_workshop.domain.model.WorkOrder;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio para gestionar las órdenes de trabajo en la
 * aplicación.
 */
@Slf4j
@Service
public class WorkOrderService implements WorkOrdersServiceInputPort {

	@Autowired
	WorkOrdersRepositoryOutputPort workOrderRepository;

	@Autowired
	WorkOrdersMessageOutputPort kafkaProducer;

	@Autowired
	WorkOrderPatchMapper workOrderPatchMapper;

	@Autowired
	VehiclesRepositoryOutputPort vehiclesRepositoryOutputPort;

	@Autowired
	VehiclesMessageOutputPort vehiclesMessageOutputPort;

	@Autowired
	VehiclesServiceInputPort vehiclesServiceInputPort;

	/**
	 * Crea una nueva orden de trabajo.
	 *
	 * @param workOrder La orden de trabajo que se va a crear.
	 * @return El ID de la orden de trabajo recién creada.
	 * @throws BusinessException Si hay errores durante el proceso de creación, como
	 *                           por ejemplo si el vehículo asociado no existe.
	 */
	@Override
	@Transactional
	public String createWorkOrder(@Valid WorkOrder workOrder) throws BusinessException {
		log.debug("Init createWorkOrder");
		try {
			String workOrderId = "";
			Optional<Vehicle> vehicleInOrder = vehiclesRepositoryOutputPort.getVehicle(workOrder.getVehicle().getId());
			if (!vehicleInOrder.isPresent()) {
				throw new BusinessException("El vehículo asociado a la orden de trabajo no existe.");
			}
			workOrderId = workOrderRepository.saveWorkOrder(workOrder);
			workOrder.setId(workOrderId);
			kafkaProducer.eventCreateWorkOrder(workOrder);

			// Agrego el ID de la nueva orden de trabajo a la lista de órdenes del vehículo
			// y actualizo su kilometraje
			Vehicle vehicleToSave = vehicleInOrder.get();
			if (vehicleToSave.getIdWorkOrders() == null) {
				vehicleToSave.setIdWorkOrders(new ArrayList<>());
			}
			vehicleToSave.getIdWorkOrders().add(workOrderId);
			vehicleToSave.setMileage(workOrder.getMileage());
			vehiclesRepositoryOutputPort.modifyVehicle(vehicleToSave);
			vehiclesMessageOutputPort.eventModifyVehicle(vehicleToSave);

			log.debug("End createWorkOrder");
			return workOrderId;
		} catch (BusinessException e) {
			log.error("Error al guardar la orden", e);
			throw e;
		}
	}

	/**
	 * Recupera una página de órdenes de trabajo.
	 *
	 * @param pageable Información de paginación.
	 * @return Una página de órdenes de trabajo.
	 * @throws BusinessException Si la paginación excede los límites máximos
	 *                           establecidos.
	 */
	@Override
	public Page<WorkOrder> getWorkOrders(@Valid Pageable pageable) throws BusinessException {
		log.debug("Init getWorkOrders");
		try {
			if (pageable.getPageSize() > Constants.MAXIMUM_PAGINATION) {
				throw new BusinessException(Errors.MAXIMUM_PAGINATION_EXCEEDED);
			}
			return workOrderRepository.listWorkOrders(pageable);
		} catch (BusinessException e) {
			log.error("Error al obtener las órdenes", e);
			throw e;
		}
	}

	/**
	 * Recupera una orden de trabajo por su ID.
	 *
	 * @param idWorkOrder El ID de la orden de trabajo a recuperar.
	 * @return Un Optional que contiene la orden de trabajo recuperada, o vacío si
	 *         no se encuentra.
	 * @throws BusinessException
	 */
	@Override
	public Optional<WorkOrder> getWorkOrder(@Valid String idWorkOrder) throws BusinessException {
		log.debug("Init getWorkOrder");
		try {
			return workOrderRepository.getWorkOrder(idWorkOrder);
		} catch (BusinessException e) {
			log.error("Error al obtener la orden", e);
			throw new BusinessException("Error al obtener la orden");
		}
	}

	/**
	 * Modifica parcialmente una orden de trabajo.
	 *
	 * @param workOrder Los datos modificados de la orden de trabajo.
	 * @throws BusinessException Si no se encuentra la orden de trabajo a modificar.
	 */
	@Override
	@Transactional
	public void partialModificationWorkOrder(@Valid WorkOrder workOrder) throws BusinessException {
		log.debug("Init partialModificationWorkOrder");
		try {
			Optional<WorkOrder> opt = workOrderRepository.getWorkOrder(workOrder.getId());
			if (!opt.isPresent()) {
				throw new BusinessException(Errors.WORK_ORDER_NOT_FOUND);
			}
			WorkOrder workOrderSaved = opt.get();

			// Si se ha cambiado el vehículo asociado a la orden
			if (!workOrderSaved.getVehicle().equals(workOrder.getVehicle())) {

				// Eliminar orden de la lista de órdenes del vehículo anterior.
				vehiclesServiceInputPort.deleteWorkOrder(workOrder.getId(), workOrderSaved.getVehicle());

				// Añadir la orden a la lista de órdenes del nuevo vehículo asociado a la orden
				vehiclesServiceInputPort.addWorkOrder(workOrder.getId(), workOrder.getVehicle());

			}
			workOrderPatchMapper.update(workOrderSaved, workOrder);
			workOrderRepository.modifyWorkOrder(workOrderSaved);
			kafkaProducer.eventModifyWorkOrder(workOrderSaved);

			log.debug("End partialModificationWorkOrder");
		} catch (BusinessException e) {
			log.error("Error al modificar la orden", e);
			throw e;
		}
	}

	/**
	 * Actualiza una orden de trabajo.
	 *
	 * @param workOrder Los datos actualizados de la orden de trabajo.
	 * @throws BusinessException Si no se encuentra la orden de trabajo a
	 *                           actualizar.
	 */
	@Override
	public void updateWorkOrder(@Valid WorkOrder workOrder) throws BusinessException {
		log.debug("Init updateWorkOrder");
		try {
			Optional<WorkOrder> opt = workOrderRepository.getWorkOrder(workOrder.getId());
			if (!opt.isPresent()) {
				throw new BusinessException(Errors.WORK_ORDER_NOT_FOUND);
			}
			workOrderRepository.modifyWorkOrder(workOrder);
			kafkaProducer.eventModifyWorkOrder(workOrder);
			log.debug("End updateWorkOrder");
		} catch (BusinessException e) {
			log.error("Error al actualizar la orden", e);
			throw e;
		}
	}

	/**
	 * Elimina una orden de trabajo.
	 *
	 * @param idWorkOrder El ID de la orden de trabajo a eliminar.
	 * @throws BusinessException Si no se encuentra la orden de trabajo a eliminar.
	 */
	@Override
	@Transactional
	public void deleteWorkOrder(@Valid String idWorkOrder) throws BusinessException {
		log.debug("Init deleteWorkOrder");
		try {
			Optional<WorkOrder> workOrderOpt = workOrderRepository.getWorkOrder(idWorkOrder);
			if (!workOrderOpt.isPresent()) {
				throw new BusinessException(Errors.WORK_ORDER_NOT_FOUND);
			}
			WorkOrder workOrder = workOrderOpt.get();

			// Elimino la orden de trabajo de la lista de órdenes del vehículo
			Optional<Vehicle> vehicleOpt = vehiclesServiceInputPort.getVehicle(workOrder.getVehicle().getId());
			if (vehicleOpt.isPresent()) {
				Vehicle vehicle = vehicleOpt.get();
				vehiclesServiceInputPort.deleteWorkOrder(idWorkOrder, vehicle);
				/*
				 * vehicle.getIdWorkOrders().remove(workOrder.getId());
				 * vehiclesRepositoryOutputPort.modifyVehicle(vehicle);
				 */
			}
			workOrderRepository.deleteWorkOrder(idWorkOrder);
			kafkaProducer.eventDeleteWorkOrder(workOrder);
		} catch (BusinessException e) {
			log.error("Error al eliminar la orden", e);
			throw e;
		}
	}

	/**
	 * Busca órdenes de trabajo según un criterio de búsqueda.
	 *
	 * @param typeSearch Tipo de búsqueda (por ejemplo: número de orden de trabajo,
	 *                   matrícula, etc.).
	 * @param value      Valor a buscar.
	 * @param pageable   Información de paginación.
	 * @return Una página de órdenes de trabajo que coinciden con los criterios de
	 *         búsqueda.
	 * @throws BusinessException
	 */
	@Override
	public Page<WorkOrder> searchWorkOrderBy(@Valid String typeSearch, @Valid String value, @Valid Pageable pageable)
			throws BusinessException {
		log.debug("Init searchWorkOrderBy");
		try {
			return workOrderRepository.searchWorkOrderBy(typeSearch, value, pageable);
		} catch (BusinessException e) {
			log.error("Error al buscar la orden", e);
			throw new BusinessException("Error al buscar la orden");
		}
	}

	/**
	 * Recupera una página de órdenes de trabajo abiertas.
	 *
	 * @param pageable Información de paginación.
	 * @return Una página de órdenes de trabajo.
	 * @throws BusinessException Si la paginación excede los límites máximos
	 *                           establecidos.
	 */
	@Override
	public Page<WorkOrder> getOpenWorkOrders(@Valid Pageable pageable) throws BusinessException {
		log.debug("Init getOpenWorkOrders");
		try {
			if (pageable.getPageSize() > Constants.MAXIMUM_PAGINATION) {
				throw new BusinessException(Errors.MAXIMUM_PAGINATION_EXCEEDED);
			}
			return workOrderRepository.listOpenWorkOrders(pageable);
		} catch (BusinessException e) {
			log.error("Error al obtener las órdenes abiertas", e);
			throw e;
		}
	}

}
