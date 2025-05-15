package com.example.mechanical_workshop.infrastructure.integrationevents.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.example.mechanical_workshop.domain.mapper.BaseMapper;
import com.example.mechanical_workshop.domain.model.WorkOrder;
import com.example.mechanical_workshop.infrastructure.integrationevents.dto.WorkOrderInputEventDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WorkOrderToWorkOrderInputEventDtoMapper extends BaseMapper<WorkOrder, WorkOrderInputEventDto> {

}
