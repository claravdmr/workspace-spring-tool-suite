package com.demo.arq.infrastructure.apirest.mapper;

import org.mapstruct.Mapper;

import com.demo.arq.domain.mapper.BaseMapper;
import com.demo.arq.domain.model.Pecera;
import com.demo.arq.infrastructure.apirest.dto.response.PeceraDto;

@Mapper(componentModel = "spring")
public interface PeceraToPeceraDtoMapper extends BaseMapper<Pecera, PeceraDto> {

}
