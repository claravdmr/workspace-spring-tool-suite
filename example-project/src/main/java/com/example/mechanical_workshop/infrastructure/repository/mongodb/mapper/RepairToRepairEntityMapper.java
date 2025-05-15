package com.example.mechanical_workshop.infrastructure.repository.mongodb.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.example.mechanical_workshop.domain.mapper.BaseMapper;
import com.example.mechanical_workshop.domain.model.Repair;
import com.example.mechanical_workshop.infrastructure.repository.mongodb.entity.RepairEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RepairToRepairEntityMapper extends BaseMapper<Repair, RepairEntity> {

}
