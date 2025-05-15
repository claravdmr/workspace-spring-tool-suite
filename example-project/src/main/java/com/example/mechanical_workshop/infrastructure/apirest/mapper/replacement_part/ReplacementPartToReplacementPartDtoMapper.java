package com.example.mechanical_workshop.infrastructure.apirest.mapper.replacement_part;

import org.mapstruct.Mapper;

import com.example.mechanical_workshop.domain.mapper.BaseMapper;
import com.example.mechanical_workshop.domain.model.ReplacementPart;
import com.example.mechanical_workshop.infrastructure.apirest.dto.replacement_part.ResponseReplacementPartDto;

@Mapper(componentModel = "spring")
public interface ReplacementPartToReplacementPartDtoMapper
		extends BaseMapper<ReplacementPart, ResponseReplacementPartDto> {

}
