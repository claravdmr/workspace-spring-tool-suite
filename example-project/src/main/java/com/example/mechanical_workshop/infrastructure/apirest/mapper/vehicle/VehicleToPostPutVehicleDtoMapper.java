package com.example.mechanical_workshop.infrastructure.apirest.mapper.vehicle;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.example.mechanical_workshop.domain.mapper.BaseMapper;
import com.example.mechanical_workshop.domain.model.Vehicle;
import com.example.mechanical_workshop.infrastructure.apirest.dto.vehicle.RequestPostPutVehicleDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VehicleToPostPutVehicleDtoMapper extends BaseMapper<Vehicle, RequestPostPutVehicleDto> {

}
