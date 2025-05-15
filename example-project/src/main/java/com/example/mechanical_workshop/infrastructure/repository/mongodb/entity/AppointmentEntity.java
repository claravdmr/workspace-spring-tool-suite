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
@Document("APPOINTMENTS")
public class AppointmentEntity {

	@Id
	private String id;
	private String customerFirstName;
	private String customerLastName;
	private String customerPhoneNumber;
	private String customerEmail;
	private String licensePlate;
	private String date;
	private String mileage;
	private List<String> repairs;
	boolean eliminado;
}
