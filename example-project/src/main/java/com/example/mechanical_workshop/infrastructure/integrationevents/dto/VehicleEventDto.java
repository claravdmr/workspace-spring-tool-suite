package com.example.mechanical_workshop.infrastructure.integrationevents.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class VehicleEventDto {
	@NotNull
	private String id;
	private String licensePlate;
	private String vin;
	private String brand;
	private String model;
	private String fuelType;
	private String mileage;
	private String registrationDate;
	private String idCustomer;
	private List<String> idWorkOrders;

}
