package com.example.mechanical_workshop.infrastructure.integrationevents.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.example.mechanical_workshop.domain.mapper.BaseMapper;
import com.example.mechanical_workshop.domain.model.Repair;
import com.example.mechanical_workshop.infrastructure.integrationevents.dto.RepairInputEventDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RepairToRepairInputEventDtoMapper extends BaseMapper<Repair, RepairInputEventDto> {

}
