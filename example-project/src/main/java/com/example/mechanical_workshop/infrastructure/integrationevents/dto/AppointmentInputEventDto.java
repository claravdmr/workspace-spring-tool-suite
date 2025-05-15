package com.example.mechanical_workshop.infrastructure.integrationevents.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class AppointmentInputEventDto {

	@NotNull
	private String id;
	@NotNull
	private String customerFirstName;
	@NotNull
	private String customerLastName;
	@NotNull
	private String customerPhoneNumber;
	@NotNull
	private String customerEmail;
	@NotNull
	private String licensePlate;
	@NotNull
	private String mileage;
	@NotNull
	private String date;
	@NotNull
	private List<String> repairs;
}
