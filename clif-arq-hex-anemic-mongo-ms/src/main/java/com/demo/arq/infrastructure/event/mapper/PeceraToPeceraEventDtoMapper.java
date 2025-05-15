package com.demo.arq.infrastructure.event.mapper;

import org.mapstruct.Mapper;

import com.demo.arq.domain.model.Pecera;
import com.demo.arq.infrastructure.event.dto.output.PeceraEventDto;

@Mapper(componentModel = "spring")
public interface PeceraToPeceraEventDtoMapper {

	PeceraEventDto fromDomainToEventDto(Pecera input);

}
