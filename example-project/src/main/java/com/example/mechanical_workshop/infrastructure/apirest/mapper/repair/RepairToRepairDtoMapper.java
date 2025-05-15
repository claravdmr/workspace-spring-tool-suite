package com.example.mechanical_workshop.infrastructure.apirest.mapper.repair;

import org.mapstruct.Mapper;

import com.example.mechanical_workshop.domain.mapper.BaseMapper;
import com.example.mechanical_workshop.domain.model.Repair;
import com.example.mechanical_workshop.infrastructure.apirest.dto.repair.ResponseRepairDto;

@Mapper(componentModel = "spring")
public interface RepairToRepairDtoMapper extends BaseMapper<Repair, ResponseRepairDto> {

}
