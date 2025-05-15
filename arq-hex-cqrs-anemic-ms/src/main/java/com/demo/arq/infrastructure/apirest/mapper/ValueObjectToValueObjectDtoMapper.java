package com.demo.arq.infrastructure.apirest.mapper;

import org.mapstruct.Mapper;

import com.demo.arq.domain.mapper.BaseMapper;
import com.demo.arq.domain.model.ValueObject;
import com.demo.arq.infrastructure.apirest.dto.common.ValueObjectDto;

@Mapper(componentModel = "spring")
public interface ValueObjectToValueObjectDtoMapper extends BaseMapper<ValueObject, ValueObjectDto> {
	
}
