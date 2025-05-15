package com.demo.arq.infrastructure.database.mapper;

import org.mapstruct.Mapper;

import com.demo.arq.domain.mapper.BaseMapper;
import com.demo.arq.domain.model.Pecera;
import com.demo.arq.infrastructure.database.entity.PeceraEntity;

@Mapper(componentModel = "spring", uses = ValueObjectToValueObjectEntityMapper.class)
public interface PeceraToPeceraEntityMapper extends BaseMapper<Pecera, PeceraEntity> {

}
