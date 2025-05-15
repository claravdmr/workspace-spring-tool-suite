package com.example.mechanical_workshop.application.ports.output;

import com.example.mechanical_workshop.domain.model.Appointment;

import jakarta.validation.Valid;

public interface AppointmentsMessageOutputPort {

	void eventCreateAppointment(@Valid Appointment input);

	void eventModifyAppointment(@Valid Appointment input);

	void eventDeleteAppointment(@Valid Appointment appointment);
}
