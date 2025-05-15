package com.example.mechanical_workshop.infrastructure.integrationevents.mapper;

import org.mapstruct.Mapper;

import com.example.mechanical_workshop.domain.mapper.BaseMapper;
import com.example.mechanical_workshop.domain.model.ReplacementPart;
import com.example.mechanical_workshop.infrastructure.integrationevents.dto.ReplacementPartEventDto;

@Mapper(componentModel = "spring")
public interface ReplacementPartToReplacementPartEventDtoMapper
		extends BaseMapper<ReplacementPart, ReplacementPartEventDto> {

}
