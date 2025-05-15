package com.demo.arq.infrastructure.event.mapper;

import org.mapstruct.Mapper;

import com.demo.arq.domain.mapper.BaseMapper;
import com.demo.arq.domain.model.Pecera;
import com.demo.arq.infrastructure.event.dto.PeceraEventDto;

@Mapper(componentModel = "spring")
public interface PeceraToPeceraEventDtoMapper extends BaseMapper<Pecera, PeceraEventDto> {

}
