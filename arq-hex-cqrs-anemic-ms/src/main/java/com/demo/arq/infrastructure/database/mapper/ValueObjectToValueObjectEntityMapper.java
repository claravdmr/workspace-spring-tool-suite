package com.demo.arq.infrastructure.database.mapper;

import org.mapstruct.Mapper;

import com.demo.arq.domain.mapper.BaseMapper;
import com.demo.arq.domain.model.ValueObject;
import com.demo.arq.infrastructure.database.entity.ValueObjectEntity;

@Mapper(componentModel = "spring")
public interface ValueObjectToValueObjectEntityMapper extends BaseMapper<ValueObject, ValueObjectEntity> {

}
