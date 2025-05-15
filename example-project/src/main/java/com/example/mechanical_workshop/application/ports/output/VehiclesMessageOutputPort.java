package com.example.mechanical_workshop.application.ports.output;

import com.example.mechanical_workshop.domain.model.Vehicle;

import jakarta.validation.Valid;

public interface VehiclesMessageOutputPort {

	void eventCreateVehicle(@Valid Vehicle input);

	void eventModifyVehicle(@Valid Vehicle input);

	void eventDeleteVehicle(@Valid Vehicle vehicle);

}
