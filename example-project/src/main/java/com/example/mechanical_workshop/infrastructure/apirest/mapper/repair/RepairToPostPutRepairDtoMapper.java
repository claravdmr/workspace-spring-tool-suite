package com.example.mechanical_workshop.infrastructure.apirest.mapper.repair;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.example.mechanical_workshop.domain.mapper.BaseMapper;
import com.example.mechanical_workshop.domain.model.Repair;
import com.example.mechanical_workshop.infrastructure.apirest.dto.repair.RequestPostPutRepairDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RepairToPostPutRepairDtoMapper extends BaseMapper<Repair, RequestPostPutRepairDto> {

}
