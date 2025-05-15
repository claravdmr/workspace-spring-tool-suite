package com.example.mechanical_workshop.infrastructure.apirest.mapper.work_order;

import org.mapstruct.Mapper;

import com.example.mechanical_workshop.domain.mapper.BaseMapper;
import com.example.mechanical_workshop.domain.model.WorkOrder;
import com.example.mechanical_workshop.infrastructure.apirest.dto.work_order.ResponseWorkOrderDto;

@Mapper(componentModel = "spring")
public interface WorkOrderToWorkOrderDtoMapper extends BaseMapper<WorkOrder, ResponseWorkOrderDto> {

}
