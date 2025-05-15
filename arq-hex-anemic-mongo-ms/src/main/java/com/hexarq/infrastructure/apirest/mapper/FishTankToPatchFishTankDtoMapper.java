package com.hexarq.infrastructure.apirest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.hexarq.domain.model.FishTank;
import com.hexarq.infrastructure.apirest.dto.request.PatchFishTankDto;

//spring is just because we are using spring, and unmapped properties will be ignored
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FishTankToPatchFishTankDtoMapper {

	FishTank fromDtoToDomain(PatchFishTankDto dto);
	
}
