package com.example.mechanical_workshop.infrastructure.apirest.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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

import com.example.mechanical_workshop.application.ports.input.WorkOrdersServiceInputPort;
import com.example.mechanical_workshop.domain.exception.BusinessException;
import com.example.mechanical_workshop.domain.model.WorkOrder;
import com.example.mechanical_workshop.infrastructure.apirest.dto.work_order.RequestPatchWorkOrderDto;
import com.example.mechanical_workshop.infrastructure.apirest.dto.work_order.RequestPostPutWorkOrderDto;
import com.example.mechanical_workshop.infrastructure.apirest.dto.work_order.ResponseWorkOrderDto;
import com.example.mechanical_workshop.infrastructure.apirest.mapper.work_order.WorkOrderToPatchWorkOrderDtoMapper;
import com.example.mechanical_workshop.infrastructure.apirest.mapper.work_order.WorkOrderToPostPutWorkOrderDtoMapper;
import com.example.mechanical_workshop.infrastructure.apirest.mapper.work_order.WorkOrderToWorkOrderDtoMapper;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador REST para gestionar órdenes de trabajo en un taller mecánico.
 */
@Slf4j
@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("/workorders")
@CrossOrigin(origins = "http://localhost:4200")
public class WorkOrderController {

	@Autowired
	WorkOrdersServiceInputPort workOrdersService;

	@Autowired
	WorkOrderToWorkOrderDtoMapper workOrderToWorkOrderDtoMapper;

	@Autowired
	WorkOrderToPatchWorkOrderDtoMapper workOrderToPatchWorkOrderDtoMapper;

	@Autowired
	WorkOrderToPostPutWorkOrderDtoMapper workOrderToPostPutWorkOrderDtoMapper;

	/**
	 * Maneja las peticiones GET para obtener todas las órdenes de trabajo.
	 *
	 * @param pageable Objeto Pageable para paginación.
	 * @return ResponseEntity con estado 200 (OK) y una página de
	 *         ResponseWorkOrderDto. En caso de error, retorna ResponseEntity con
	 *         estado 400 (Bad Request) y mensaje de error.
	 */
	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping
	public ResponseEntity getWorkOrders(@Valid Pageable pageable) {
		log.debug("getWorkOrders");
		Page<WorkOrder> workOrderDomain;
		try {
			workOrderDomain = workOrdersService.getWorkOrders(pageable);
		} catch (BusinessException e) {
			log.error("Error obteniendo ordenes de trabajo", e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		// return
		// ResponseEntity.ok(workOrderToWorkOrderDtoMapper.fromInputToOutput(workOrderDomain));
		List<ResponseWorkOrderDto> listDto = new ArrayList<>();
		workOrderDomain.getContent().forEach(workOrder -> {
			ResponseWorkOrderDto workOrderDto = workOrderToWorkOrderDtoMapper.fromInputToOutput(workOrder);
			listDto.add(workOrderDto);
		});
		return ResponseEntity.ok(new PageImpl<>(listDto, pageable, listDto.size()));
	}

	/**
	 * Maneja las peticiones GET para obtener una orden de trabajo específica por su
	 * ID.
	 *
	 * @param idWorkOrder ID de la orden de trabajo a buscar.
	 * @return ResponseEntity con estado 200 (OK) y la orden de trabajo encontrada,
	 *         o ResponseEntity con estado 204 (No Content) si no se encuentra.
	 */
	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("/{id-workOrder}")
	public ResponseEntity getWorkOrder(@Valid @PathVariable("id-workOrder") final String idWorkOrder) {
		log.debug("getWorkOrder");
		try {
			Optional<WorkOrder> workOrderDomain = workOrdersService.getWorkOrder(idWorkOrder);

			if (workOrderDomain.isPresent()) {
				ResponseWorkOrderDto workOrderDto = workOrderToWorkOrderDtoMapper
						.fromInputToOutput(workOrderDomain.get());
				return ResponseEntity.ok(workOrderDto);
			} else {
				return ResponseEntity.noContent().build();
			}
		} catch (BusinessException e) {
			log.error("Error obteniendo órdenes", e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	/**
	 * Maneja las peticiones POST para crear una nueva orden de trabajo.
	 *
	 * @param workOrder Datos de la orden de trabajo en formato
	 *                  RequestPostPutWorkOrderDto.
	 * @return ResponseEntity con estado 201 (Creado) y URI del recurso creado. En
	 *         caso de conflicto, retorna ResponseEntity con estado 409 (Conflict) y
	 *         mensaje de error.
	 * @throws BusinessException Si hay un error al crear la orden de trabajo.
	 */
	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping
	@Transactional
	public ResponseEntity postWorkOrder(@Valid @RequestBody RequestPostPutWorkOrderDto workOrder)
			throws BusinessException {
		log.debug("postWorkOrder");
		try {
			WorkOrder workOrderToSave = workOrderToPostPutWorkOrderDtoMapper.fromOutputToInput(workOrder);
			String idWorkOrder = workOrdersService.createWorkOrder(workOrderToSave);
			URI locationHeader = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(idWorkOrder).toUri();
			return ResponseEntity.created(locationHeader).build();
		} catch (BusinessException e) {
			log.error("Error creando la orden", e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	/**
	 * Maneja las peticiones DELETE para eliminar una orden de trabajo por su ID.
	 *
	 * @param idWorkOrder ID de la orden de trabajo a eliminar.
	 * @return ResponseEntity con estado 204 (No Content) si la eliminación es
	 *         exitosa, o ResponseEntity con estado 400 (Bad Request) si hay un
	 *         error.
	 */
	@CrossOrigin(origins = "http://localhost:4200")
	@DeleteMapping("/{id-workOrder}")
	public ResponseEntity deleteWorkOrder(@Valid @PathVariable("id-workOrder") final String idWorkOrder) {
		log.debug("deleteWorkOrder");
		try {
			workOrdersService.deleteWorkOrder(idWorkOrder);
		} catch (BusinessException e) {
			log.error("Error eliminando orden de trabajo", e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.noContent().build();
	}

	/**
	 * Maneja las peticiones PATCH para modificar parcialmente una orden de trabajo
	 * existente.
	 *
	 * @param idWorkOrder ID de la orden de trabajo a modificar.
	 * @param workOrder   Datos actualizados de la orden de trabajo en formato
	 *                    RequestPatchWorkOrderDto.
	 * @return ResponseEntity con estado 200 (OK) y la orden de trabajo modificada,
	 *         o ResponseEntity con estado 400 (Bad Request) si hay un error.
	 */
	@CrossOrigin(origins = "http://localhost:4200")
	@PatchMapping("/{id-workOrder}")
	public ResponseEntity patchWorkOrder(@Valid @PathVariable("id-workOrder") final String idWorkOrder,
			@Valid @RequestBody RequestPatchWorkOrderDto workOrder) {
		log.debug("patchWorkOrder");

		WorkOrder workOrderDomain = workOrderToPatchWorkOrderDtoMapper.fromOutputToInput(workOrder);
		workOrderDomain.setId(idWorkOrder);

		try {
			workOrdersService.partialModificationWorkOrder(workOrderDomain);
		} catch (BusinessException e) {
			log.debug("Error modificando vehículo", e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}

		// URI locationHeader =
		// ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand().toUri();
		return ResponseEntity.ok(workOrderDomain);
	}

	/**
	 * Maneja las peticiones PUT para actualizar completamente una orden de trabajo
	 * existente.
	 *
	 * @param idWorkOrder ID de la orden de trabajo a actualizar.
	 * @param workOrder   Datos actualizados de la orden de trabajo en formato
	 *                    RequestPostPutWorkOrderDto.
	 * @return ResponseEntity con estado 204 (No Content) si la actualización es
	 *         exitosa, o ResponseEntity con estado 400 (Bad Request) si hay un
	 *         error.
	 */
	@CrossOrigin(origins = "http://localhost:4200")
	@PutMapping("/{id-workOrder}")
	public ResponseEntity putWorkOrder(@Valid @PathVariable("id-workOrder") final String idWorkOrder,
			@Valid @RequestBody RequestPostPutWorkOrderDto workOrder) {
		log.debug("putWorkOrder");

		WorkOrder domain = workOrderToPostPutWorkOrderDtoMapper.fromOutputToInput(workOrder);
		domain.setId(idWorkOrder);

		try {
			workOrdersService.updateWorkOrder(domain);
		} catch (BusinessException e) {
			log.debug("Error modificando vehículo", e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}

		// URI locationHeader =
		// ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand().toUri();
		return ResponseEntity.noContent().build();
	}

	/**
	 * Maneja las peticiones GET para buscar órdenes de trabajo según un criterio
	 * específico.
	 *
	 * @param typeSearch Tipo de búsqueda (por ejemplo, "orderNumber",
	 *                   "licensePlate").
	 * @param value      Valor a buscar dentro del criterio especificado.
	 * @param pageable   Objeto Pageable para paginación.
	 * @return ResponseEntity con estado 200 (OK) y una página de
	 *         ResponseWorkOrderDto que contiene las órdenes de trabajo encontradas.
	 *         En caso de error de validación, retorna ResponseEntity con estado 400
	 *         (Bad Request) y mensaje de error. Si no se encuentra ninguna orden de
	 *         trabajo, retorna ResponseEntity con estado 204 (No Content). Si
	 *         ocurre un error interno, retorna ResponseEntity con estado 500
	 *         (Internal Server Error) y mensaje de error.
	 */
	@GetMapping("/search/{type-search}")
	public ResponseEntity searchWorkOrderBy(@Valid @PathVariable("type-search") final String typeSearch,
			@Valid @RequestParam("value") final String value, @Valid Pageable pageable) {
		log.debug("searchWorkOrderBy");
		try {
			Page<WorkOrder> workOrder = workOrdersService.searchWorkOrderBy(typeSearch, value, pageable);
			/*
			 * if (workOrder.isPresent()) { return ResponseEntity.ok(workOrder); } else {
			 * return ResponseEntity.noContent().build(); }
			 */
			return ResponseEntity.ok(workOrderToWorkOrderDtoMapper.fromInputToOutput(workOrder));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body("Tipo de búsqueda inválido");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error buscando la orden de trabajo");
		}
		// return ResponseEntity.noContent().build();
	}

	/**
	 * Maneja las peticiones GET para obtener todas las órdenes de trabajo abiertas.
	 *
	 * @param pageable Objeto Pageable para paginación.
	 * @return ResponseEntity con estado 200 (OK) y una página de
	 *         ResponseWorkOrderDto. En caso de error, retorna ResponseEntity con
	 *         estado 400 (Bad Request) y mensaje de error.
	 */
	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("/open")
	public ResponseEntity getOpenWorkOrders(@Valid Pageable pageable) {
		log.debug("getOpenWorkOrders");
		Page<WorkOrder> workOrderDomain;
		try {
			workOrderDomain = workOrdersService.getOpenWorkOrders(pageable);
		} catch (BusinessException e) {
			log.error("Error obteniendo ordenes de trabajo", e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		// return
		// ResponseEntity.ok(workOrderToWorkOrderDtoMapper.fromInputToOutput(workOrderDomain));
		List<ResponseWorkOrderDto> listDto = new ArrayList<>();
		workOrderDomain.getContent().forEach(workOrder -> {
			ResponseWorkOrderDto workOrderDto = workOrderToWorkOrderDtoMapper.fromInputToOutput(workOrder);
			listDto.add(workOrderDto);
		});
		return ResponseEntity.ok(new PageImpl<>(listDto, pageable, listDto.size()));
	}

}
