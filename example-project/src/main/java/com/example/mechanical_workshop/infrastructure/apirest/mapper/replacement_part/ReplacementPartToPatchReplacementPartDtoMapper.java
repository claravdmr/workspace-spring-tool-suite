package com.example.mechanical_workshop.infrastructure.apirest.mapper.replacement_part;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.example.mechanical_workshop.domain.mapper.BaseMapper;
import com.example.mechanical_workshop.domain.model.ReplacementPart;
import com.example.mechanical_workshop.infrastructure.apirest.dto.replacement_part.RequestPatchReplacementPartDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReplacementPartToPatchReplacementPartDtoMapper
		extends BaseMapper<ReplacementPart, RequestPatchReplacementPartDto> {

}
