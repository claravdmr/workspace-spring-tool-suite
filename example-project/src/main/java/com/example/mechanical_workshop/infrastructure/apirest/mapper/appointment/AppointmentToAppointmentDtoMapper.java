package com.example.mechanical_workshop.infrastructure.apirest.mapper.appointment;

import org.mapstruct.Mapper;

import com.example.mechanical_workshop.domain.mapper.BaseMapper;
import com.example.mechanical_workshop.domain.model.Appointment;
import com.example.mechanical_workshop.infrastructure.apirest.dto.appointments.ResponseAppointmentDto;

@Mapper(componentModel = "spring")
public interface AppointmentToAppointmentDtoMapper extends BaseMapper<Appointment, ResponseAppointmentDto> {

}
