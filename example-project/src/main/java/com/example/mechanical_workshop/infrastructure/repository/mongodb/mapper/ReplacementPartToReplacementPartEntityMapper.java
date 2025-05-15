package com.example.mechanical_workshop.infrastructure.repository.mongodb.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.example.mechanical_workshop.domain.mapper.BaseMapper;
import com.example.mechanical_workshop.domain.model.ReplacementPart;
import com.example.mechanical_workshop.infrastructure.repository.mongodb.entity.ReplacementPartEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReplacementPartToReplacementPartEntityMapper
		extends BaseMapper<ReplacementPart, ReplacementPartEntity> {

}
