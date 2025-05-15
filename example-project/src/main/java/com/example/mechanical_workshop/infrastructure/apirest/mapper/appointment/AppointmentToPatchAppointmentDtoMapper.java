package com.example.mechanical_workshop.infrastructure.apirest.mapper.appointment;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.example.mechanical_workshop.domain.mapper.BaseMapper;
import com.example.mechanical_workshop.domain.model.Appointment;
import com.example.mechanical_workshop.infrastructure.apirest.dto.appointments.RequestPatchAppointmentDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AppointmentToPatchAppointmentDtoMapper extends BaseMapper<Appointment, RequestPatchAppointmentDto> {

}
