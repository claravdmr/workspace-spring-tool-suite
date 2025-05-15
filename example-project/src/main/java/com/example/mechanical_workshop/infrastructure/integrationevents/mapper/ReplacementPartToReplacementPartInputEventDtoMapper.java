package com.example.mechanical_workshop.infrastructure.integrationevents.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.example.mechanical_workshop.domain.mapper.BaseMapper;
import com.example.mechanical_workshop.domain.model.ReplacementPart;
import com.example.mechanical_workshop.infrastructure.integrationevents.dto.ReplacementPartInputEventDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReplacementPartToReplacementPartInputEventDtoMapper
		extends BaseMapper<ReplacementPart, ReplacementPartInputEventDto> {

}
