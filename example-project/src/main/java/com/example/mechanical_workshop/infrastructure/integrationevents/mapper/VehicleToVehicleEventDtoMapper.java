package com.example.mechanical_workshop.infrastructure.integrationevents.mapper;

import org.mapstruct.Mapper;

import com.example.mechanical_workshop.domain.mapper.BaseMapper;
import com.example.mechanical_workshop.domain.model.Vehicle;
import com.example.mechanical_workshop.infrastructure.integrationevents.dto.VehicleEventDto;

@Mapper(componentModel = "spring")
public interface VehicleToVehicleEventDtoMapper extends BaseMapper<Vehicle, VehicleEventDto> {

}
