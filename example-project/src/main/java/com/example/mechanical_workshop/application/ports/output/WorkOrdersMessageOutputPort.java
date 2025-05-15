package com.example.mechanical_workshop.application.ports.output;

import com.example.mechanical_workshop.domain.model.WorkOrder;

import jakarta.validation.Valid;

public interface WorkOrdersMessageOutputPort {

	void eventCreateWorkOrder(@Valid WorkOrder input);

	void eventModifyWorkOrder(@Valid WorkOrder input);

	void eventDeleteWorkOrder(@Valid WorkOrder workOrder);

}
