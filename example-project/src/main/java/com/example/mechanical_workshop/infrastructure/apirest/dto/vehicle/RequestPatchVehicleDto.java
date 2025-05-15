package com.example.mechanical_workshop.infrastructure.apirest.dto.vehicle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestPatchVehicleDto {
	private String licensePlate;
	private String vin;
	private String brand;
	private String model;
	private String fuelType;
	private String mileage;
	private String idCustomer;
	private String registrationDate;
}
