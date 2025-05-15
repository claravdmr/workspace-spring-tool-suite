package com.example.mechanical_workshop.infrastructure.integrationevents.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class AppointmentEventDto {

	@NotNull
	private String id;
	private String customerFirstName;
	private String customerLastName;
	private String customerPhoneNumber;
	private String customerEmail;
	private String licensePlate;
	private String mileage;
	private String date;
	private List<String> repairs;
}
