package com.example.mechanical_workshop.application.ports.input;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.mechanical_workshop.domain.exception.BusinessException;
import com.example.mechanical_workshop.domain.model.Vehicle;

import jakarta.validation.Valid;

public interface VehiclesServiceInputPort {

	public String createVehicle(@Valid Vehicle vehicle) throws BusinessException;

	public Page<Vehicle> getVehicles(@Valid Pageable pageable) throws BusinessException;

	public Optional<Vehicle> getVehicle(@Valid String idVehicle) throws BusinessException;

	public void deleteVehicle(@Valid String idVehicle) throws BusinessException;

	void partialModificationVehicle(@Valid Vehicle vehicle) throws BusinessException;

	void updateVehicle(@Valid Vehicle vehicle) throws BusinessException;

	public Page<Vehicle> searchVehicleBy(@Valid String typeSearch, @Valid String value, @Valid Pageable pageable)
			throws BusinessException;

	void changeVehicleCustomer(@Valid Vehicle vehicleSaved, @Valid Vehicle vehicleModified) throws BusinessException;

	public void deleteWorkOrder(@Valid String idWorkOrder, @Valid Vehicle vehicle) throws BusinessException;

	public void addWorkOrder(@Valid String idWorkOrder, @Valid Vehicle vehicle) throws BusinessException;

}
