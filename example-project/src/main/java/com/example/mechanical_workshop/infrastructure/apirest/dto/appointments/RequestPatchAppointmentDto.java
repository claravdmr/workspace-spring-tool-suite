package com.example.mechanical_workshop.infrastructure.apirest.dto.appointments;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestPatchAppointmentDto {

	private String customerFirstName;
	private String customerLastName;
	private String customerPhoneNumber;
	private String customerEmail;
	private String licensePlate;
	private String mileage;
	private String date;
	private List<String> repairs;
}
