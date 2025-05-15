package com.demo.arq.infrastructure.apirest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.demo.arq.domain.model.Pecera;
import com.demo.arq.infrastructure.apirest.dto.request.PatchPeceraDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PeceraToPatchPeceraDtoMapper {

	Pecera fromDtoToDomain(PatchPeceraDto dto);

}
