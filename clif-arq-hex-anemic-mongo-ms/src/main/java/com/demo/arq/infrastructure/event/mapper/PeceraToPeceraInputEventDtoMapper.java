package com.demo.arq.infrastructure.event.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.demo.arq.domain.model.Pecera;
import com.demo.arq.infrastructure.event.dto.input.PeceraInputEventDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PeceraToPeceraInputEventDtoMapper {

	Pecera fromEventDtoToDomain(PeceraInputEventDto value);

}
