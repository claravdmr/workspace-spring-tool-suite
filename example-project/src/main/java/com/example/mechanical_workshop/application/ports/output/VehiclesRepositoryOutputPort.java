package com.example.mechanical_workshop.application.ports.output;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.mechanical_workshop.domain.exception.BusinessException;
import com.example.mechanical_workshop.domain.model.Vehicle;

import jakarta.validation.Valid;

public interface VehiclesRepositoryOutputPort {

	public Page<Vehicle> listVehicles(@Valid Pageable pageable) throws BusinessException;

	public Optional<Vehicle> getVehicle(@Valid String idVehicle) throws BusinessException;

	public String saveVehicle(@Valid Vehicle vehicle) throws BusinessException;

	public void deleteVehicle(@Valid String idVehicle) throws BusinessException;

	public void modifyVehicle(@Valid Vehicle vehicle) throws BusinessException;

	public Page<Vehicle> searchVehicleBy(@Valid String typeSearch, @Valid String value, @Valid Pageable pageable)
			throws BusinessException;

}
