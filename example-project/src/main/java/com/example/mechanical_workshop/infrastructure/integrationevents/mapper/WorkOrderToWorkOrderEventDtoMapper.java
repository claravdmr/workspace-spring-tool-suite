package com.example.mechanical_workshop.infrastructure.integrationevents.mapper;

import org.mapstruct.Mapper;

import com.example.mechanical_workshop.domain.mapper.BaseMapper;
import com.example.mechanical_workshop.domain.model.WorkOrder;
import com.example.mechanical_workshop.infrastructure.integrationevents.dto.WorkOrderEventDto;

@Mapper(componentModel = "spring")
public interface WorkOrderToWorkOrderEventDtoMapper extends BaseMapper<WorkOrder, WorkOrderEventDto> {

}
