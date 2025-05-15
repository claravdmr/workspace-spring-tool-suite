package com.example.mechanical_workshop.application.ports.output;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.mechanical_workshop.domain.exception.BusinessException;
import com.example.mechanical_workshop.domain.model.WorkOrder;

import jakarta.validation.Valid;

public interface WorkOrdersRepositoryOutputPort {

	public Page<WorkOrder> listWorkOrders(@Valid Pageable pageable) throws BusinessException;

	public Optional<WorkOrder> getWorkOrder(@Valid String idWorkOrder) throws BusinessException;

	public String saveWorkOrder(@Valid WorkOrder workOrder) throws BusinessException;

	public void deleteWorkOrder(@Valid String idWorkOrder) throws BusinessException;

	public void modifyWorkOrder(@Valid WorkOrder workOrder) throws BusinessException;

	public Page<WorkOrder> searchWorkOrderBy(@Valid String typeSearch, @Valid String value, Pageable pageable)
			throws BusinessException;

	public Page<WorkOrder> listOpenWorkOrders(@Valid Pageable pageable) throws BusinessException;

}
