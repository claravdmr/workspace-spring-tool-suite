package com.example.mechanical_workshop.application.ports.input;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.mechanical_workshop.domain.exception.BusinessException;
import com.example.mechanical_workshop.domain.model.WorkOrder;

import jakarta.validation.Valid;

public interface WorkOrdersServiceInputPort {

	public String createWorkOrder(@Valid WorkOrder workOrder) throws BusinessException;

	public Page<WorkOrder> getWorkOrders(@Valid Pageable pageable) throws BusinessException;

	public Optional<WorkOrder> getWorkOrder(@Valid String idWorkOrder) throws BusinessException;

	public void deleteWorkOrder(@Valid String idWorkOrder) throws BusinessException;

	void partialModificationWorkOrder(@Valid WorkOrder workOrder) throws BusinessException;

	void updateWorkOrder(@Valid WorkOrder workOrder) throws BusinessException;

	public Page<WorkOrder> searchWorkOrderBy(@Valid String typeSearch, @Valid String value, @Valid Pageable pageable)
			throws BusinessException;

	public Page<WorkOrder> getOpenWorkOrders(@Valid Pageable pageable) throws BusinessException;
}
