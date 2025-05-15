package com.example.mechanical_workshop.infrastructure.integrationevents.mapper;

import org.mapstruct.Mapper;

import com.example.mechanical_workshop.domain.mapper.BaseMapper;
import com.example.mechanical_workshop.domain.model.Repair;
import com.example.mechanical_workshop.infrastructure.integrationevents.dto.RepairEventDto;

@Mapper(componentModel = "spring")
public interface RepairToRepairEventDtoMapper extends BaseMapper<Repair, RepairEventDto> {

}
