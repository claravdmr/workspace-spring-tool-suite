package com.example.mechanical_workshop.infrastructure.repository.mongodb.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("VEHICLES")
public class VehicleEntity {
	@Id
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
	boolean eliminado;
}
