package com.demo.arq.infrastructure.database.jpa.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.demo.arq.domain.mapper.BaseMapper;
import com.demo.arq.domain.model.Pecera;
import com.demo.arq.infrastructure.database.jpa.entity.PeceraJpaEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PeceraJpaToPeceraJpaEntityMapper extends BaseMapper<Pecera, PeceraJpaEntity> {

	@Mapping(target = "valueObjectValue", source = "input.valueObject.value")
	@Mapping(target = "id", source = "input.idJpa")
	PeceraJpaEntity fromInputToOutput(Pecera input);

	@InheritInverseConfiguration
	Pecera fromOutputToInput(PeceraJpaEntity output);
}
