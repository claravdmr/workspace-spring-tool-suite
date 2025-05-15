package com.example.mechanical_workshop.infrastructure.apirest.mapper.vehicle;

import org.mapstruct.Mapper;

import com.example.mechanical_workshop.domain.mapper.BaseMapper;
import com.example.mechanical_workshop.domain.model.Vehicle;
import com.example.mechanical_workshop.infrastructure.apirest.dto.vehicle.ResponseVehicleDto;

@Mapper(componentModel = "spring")
public interface VehicleToVehicleDtoMapper extends BaseMapper<Vehicle, ResponseVehicleDto> {

}
