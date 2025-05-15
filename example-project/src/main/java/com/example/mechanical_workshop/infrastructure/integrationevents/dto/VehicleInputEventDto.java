package com.example.mechanical_workshop.infrastructure.integrationevents.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class VehicleInputEventDto {
	@NotNull
	private String id;
	@NotNull
	private String licensePlate;
	@NotNull
	private String vin;
	@NotNull
	private String brand;
	@NotNull
	private String model;
	@NotNull
	private String fuelType;
	@NotNull
	private String mileage;
	@NotNull
	private String registrationDate;
	@NotNull
	private String idCustomer;
	@NotNull
	private List<String> idWorkOrders;

}
