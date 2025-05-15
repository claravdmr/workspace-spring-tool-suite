package com.demo.arq.infrastructure.event.mapper;

import org.mapstruct.Mapper;

import com.demo.arq.domain.mapper.BaseMapper;
import com.demo.arq.domain.model.ValueObject;
import com.demo.arq.infrastructure.event.dto.ValueObjectEventDto;

@Mapper(componentModel = "spring")
public interface ValueObjectToValueObjectEventDtoMapper extends BaseMapper<ValueObject, ValueObjectEventDto> {

}
