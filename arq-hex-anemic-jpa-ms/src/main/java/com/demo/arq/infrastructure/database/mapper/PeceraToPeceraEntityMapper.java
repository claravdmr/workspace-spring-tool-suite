package com.demo.arq.infrastructure.database.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.demo.arq.domain.mapper.BaseMapper;
import com.demo.arq.domain.model.Pecera;
import com.demo.arq.infrastructure.database.entity.PeceraEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PeceraToPeceraEntityMapper extends BaseMapper<Pecera, PeceraEntity> {
	
	@Mapping(target = "valueObjectValue", source = "valueObject.value")
	PeceraEntity fromInputToOutput(Pecera input);

	@InheritInverseConfiguration
	Pecera fromOutputToInput(PeceraEntity output);
}
