package com.example.mechanical_workshop.infrastructure.integrationevents.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.example.mechanical_workshop.domain.mapper.BaseMapper;
import com.example.mechanical_workshop.domain.model.Vehicle;
import com.example.mechanical_workshop.infrastructure.integrationevents.dto.VehicleInputEventDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VehicleToVehicleInputEventDtoMapper extends BaseMapper<Vehicle, VehicleInputEventDto> {

}
