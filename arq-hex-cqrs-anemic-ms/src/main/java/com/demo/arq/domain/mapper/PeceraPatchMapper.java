package com.demo.arq.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.demo.arq.domain.command.PatchPeceraCommand;
import com.demo.arq.domain.model.Pecera;
import com.demo.arq.domain.model.ValueObject;

@Mapper(componentModel = "spring", 
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, 
	nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface PeceraPatchMapper {
	void update(@MappingTarget Pecera output, PatchPeceraCommand input);

	void update(@MappingTarget ValueObject output, ValueObject input);
}
