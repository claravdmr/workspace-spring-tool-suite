package com.example.mechanical_workshop.infrastructure.integrationevents.mapper;

import org.mapstruct.Mapper;

import com.example.mechanical_workshop.domain.mapper.BaseMapper;
import com.example.mechanical_workshop.domain.model.Appointment;
import com.example.mechanical_workshop.infrastructure.integrationevents.dto.AppointmentEventDto;

@Mapper(componentModel = "spring")
public interface AppointmentToAppointmentEventDtoMapper extends BaseMapper<Appointment, AppointmentEventDto> {

}
