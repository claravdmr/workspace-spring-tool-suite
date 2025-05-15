package com.example.mechanical_workshop.infrastructure.apirest.dto.appointments;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestPostPutAppointmentDto {

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
