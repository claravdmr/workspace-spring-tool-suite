package com.hexarq.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.hexarq.domain.model.FishTank;
import com.hexarq.domain.model.ValueObject;



@Mapper(componentModel = "spring", 
		nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, 
		nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
	
public interface FishTankPatchMapper {
	
	void update(@MappingTarget FishTank output, FishTank input);
	
	void update(@MappingTarget ValueObject output, ValueObject input);

}
