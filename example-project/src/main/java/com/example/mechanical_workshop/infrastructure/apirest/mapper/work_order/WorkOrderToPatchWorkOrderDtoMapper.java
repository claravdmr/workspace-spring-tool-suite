package com.example.mechanical_workshop.infrastructure.apirest.mapper.work_order;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.example.mechanical_workshop.domain.mapper.BaseMapper;
import com.example.mechanical_workshop.domain.model.WorkOrder;
import com.example.mechanical_workshop.infrastructure.apirest.dto.work_order.RequestPatchWorkOrderDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WorkOrderToPatchWorkOrderDtoMapper extends BaseMapper<WorkOrder, RequestPatchWorkOrderDto> {

}
