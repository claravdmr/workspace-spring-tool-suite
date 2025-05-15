package com.example.mechanical_workshop.infrastructure.repository.mongodb.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.example.mechanical_workshop.application.ports.output.WorkOrdersRepositoryOutputPort;
import com.example.mechanical_workshop.domain.exception.BusinessException;
import com.example.mechanical_workshop.domain.model.StatusRepair;
import com.example.mechanical_workshop.domain.model.WorkOrder;
import com.example.mechanical_workshop.infrastructure.repository.mongodb.entity.WorkOrderEntity;
import com.example.mechanical_workshop.infrastructure.repository.mongodb.mapper.WorkOrderToWorkOrderEntityMapper;

import jakarta.validation.Valid;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio para gestionar órdenes de trabajo en el
 * repositorio MongoDB.
 */
@Slf4j
@Component
public class WorkOrderRepositoryService implements WorkOrdersRepositoryOutputPort {

	@Autowired
	WorkOrderRepository workOrderRepository;

	@Autowired
	WorkOrderToWorkOrderEntityMapper workOrderMapper;

	/**
	 * Obtiene una lista paginada de órdenes de trabajo.
	 *
	 * @param pageable Información de paginación.
	 * @return Página de objetos WorkOrder.
	 * @throws BusinessException
	 */
	@Override
	@Cacheable(value = "workOrders", key = "#pageable")
	public Page<WorkOrder> listWorkOrders(Pageable pageable) throws BusinessException {
		log.debug("listWorkOrders");
		try {
			Page<WorkOrderEntity> workOrders = workOrderRepository.findByEliminado(false, pageable);
			return workOrderMapper.fromOutputToInput(workOrders);
		} catch (Exception e) {
			log.error("Error al listar órdenes de trabajo", e);
			throw new BusinessException("Error al obtener la lista de órdenes de trabajo");
		}
	}

	/**
	 * Obtiene una orden de trabajo por su ID.
	 *
	 * @param id ID de la orden de trabajo.
	 * @return Optional que contiene la WorkOrder si se encuentra, de lo contrario
	 *         vacío.
	 * @throws BusinessException
	 */
	@Override
	@Cacheable(value = "workOrders", key = "#id")
	public Optional<WorkOrder> getWorkOrder(String id) throws BusinessException {
		log.debug("getWorkOrder");
		try {
			Optional<WorkOrderEntity> workOrderEntityOpt = workOrderRepository.findByEliminadoAndId(false, id);
			if (workOrderEntityOpt.isEmpty()) {
				return Optional.empty();
			}
			return workOrderMapper.fromOutputToInput(workOrderEntityOpt);
		} catch (Exception e) {
			log.error("Error al obtener la orden de trabajo con ID: " + id, e);
			throw new BusinessException("Error al obtener la orden de trabajo con ID: " + id);
		}
	}

	/**
	 * Guarda una nueva orden de trabajo.
	 *
	 * @param workOrder Orden de trabajo a guardar.
	 * @return ID de la orden de trabajo guardada.
	 * @throws BusinessException
	 */
	@Override
	@CacheEvict(value = "workOrders", allEntries = true)
	public String saveWorkOrder(@Valid WorkOrder workOrder) throws BusinessException {
		log.debug("saveWorkOrder");
		try {
			long newWorkOrderNumber = 10;

			Optional<WorkOrderEntity> lastWorkOrderOpt = workOrderRepository.findTopByOrderByWorkOrderNumberDesc();
			if (lastWorkOrderOpt.isPresent()) {
				WorkOrderEntity lastWorkOrder = lastWorkOrderOpt.get();
				newWorkOrderNumber = lastWorkOrder.getWorkOrderNumber() + 10;
			}

			workOrder.setWorkOrderNumber(newWorkOrderNumber);
			WorkOrderEntity workOrderSaved = workOrderMapper.fromInputToOutput(workOrder);
			return workOrderRepository.save(workOrderSaved).getId();
		} catch (Exception e) {
			log.error("Error al guardar la orden de trabajo", e);
			throw new BusinessException("Error al guardar la orden de trabajo");
		}
	}

	/**
	 * Modifica una orden de trabajo existente.
	 *
	 * @param workOrder Orden de trabajo a modificar.
	 */
	@SneakyThrows
	@Override
	@CacheEvict(value = "workOrders", allEntries = true)
	public void modifyWorkOrder(@Valid WorkOrder workOrder) {
		log.debug("modifyWorkOrder");
		try {
			WorkOrderEntity workOrderSaved = workOrderMapper.fromInputToOutput(workOrder);
			workOrderRepository.save(workOrderSaved);
		} catch (Exception e) {
			log.error("Error al modificar la orden de trabajo", e);
			throw new BusinessException("Error al modificar la orden de trabajo");
		}
	}

	/**
	 * Elimina lógicamente una orden de trabajo por su ID.
	 *
	 * @param id ID de la orden de trabajo a eliminar.
	 * @throws BusinessException
	 */
	@Override
	@CacheEvict(value = "workOrders", allEntries = true)
	public void deleteWorkOrder(String id) throws BusinessException {
		log.debug("deleteWorkOrder");
		try {
			Optional<WorkOrderEntity> opt = workOrderRepository.findByEliminadoAndId(false, id);
			if (opt.isPresent()) {
				opt.get().setEliminado(true);
				workOrderRepository.save(opt.get());
			}
		} catch (Exception e) {
			log.error("Error al eliminar la orden de trabajo con ID: " + id, e);
			throw new BusinessException("Error al eliminar la orden de trabajo con ID: " + id);
		}
	}

	/**
	 * Busca órdenes de trabajo basándose en un criterio específico.
	 *
	 * @param typeSearch Tipo de criterio de búsqueda ("workOrderNumber" o
	 *                   "licensePlate").
	 * @param value      Valor a buscar.
	 * @param pageable   Información de paginación.
	 * @return Página de objetos WorkOrder que coinciden con el criterio de
	 *         búsqueda.
	 * @throws BusinessException
	 * @throws IllegalArgumentException si el formato del número de cliente es
	 *                                  inválido.
	 */
	@Override
	@Cacheable(value = "workOrders", key = "#typeSearch")
	public Page<WorkOrder> searchWorkOrderBy(@Valid String typeSearch, @Valid String value, Pageable pageable)
			throws BusinessException {
		log.debug("searchWorkOrderBy");
		Page<WorkOrderEntity> workOrderEntity;
		try {
			if (typeSearch.equals("workOrderNumber")) {
				Long valueLong = Long.parseLong(value);
				workOrderEntity = workOrderRepository.findByEliminadoAndWorkOrderNumber(false, valueLong, pageable);
			} else {
				workOrderEntity = workOrderRepository.findByEliminadoAndLicensePlate(false, value, pageable);
			}
			/*
			 * if (workOrderEntityOpt.isEmpty()) { return Optional.empty(); }
			 */
			return workOrderMapper.fromOutputToInput(workOrderEntity);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Formato de búsqueda inválido");
		} catch (Exception e) {
			log.error("Error al buscar órdenes de trabajo por " + typeSearch + ": " + value, e);
			throw new BusinessException("Error al buscar órdenes de trabajo por " + typeSearch + ": " + value);
		}
	}

	/**
	 * Obtiene una lista paginada de órdenes de trabajo abiertas.
	 *
	 * @param pageable Información de paginación.
	 * @return Página de objetos WorkOrder.
	 * @throws BusinessException
	 */
	@Override
	@Cacheable(value = "workOrders", key = "#pageable")
	public Page<WorkOrder> listOpenWorkOrders(@Valid Pageable pageable) throws BusinessException {
		log.debug("listOpenWorkOrders");
		try {
			Page<WorkOrderEntity> workOrders = workOrderRepository.findByEliminadoAndStatusNot(false,
					StatusRepair.TERMINADA, pageable);
			return workOrderMapper.fromOutputToInput(workOrders);
		} catch (Exception e) {
			log.error("Error al listar órdenes de trabajo abiertas", e);
			throw new BusinessException("Error al obtener la lista de órdenes de trabajo abiertas");
		}
	}
}
