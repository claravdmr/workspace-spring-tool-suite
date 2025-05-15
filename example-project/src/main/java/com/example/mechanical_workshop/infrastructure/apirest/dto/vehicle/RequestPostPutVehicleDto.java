package com.example.mechanical_workshop.infrastructure.apirest.dto.vehicle;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestPostPutVehicleDto {
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
}
