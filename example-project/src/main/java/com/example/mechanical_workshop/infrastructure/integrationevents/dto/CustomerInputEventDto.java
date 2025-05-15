package com.example.mechanical_workshop.infrastructure.integrationevents.dto;

import java.util.List;

import com.example.mechanical_workshop.domain.model.DocumentType;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class CustomerInputEventDto {
	@NotNull
	private String id;
	@NotNull
	private Long customerNumber;
	@NotNull
	private String firstName;
	@NotNull
	private String lastName;
	@NotNull
	private String address;
	@NotNull
	private DocumentType documentType;
	@NotNull
	private String documentNumber;
	@NotNull
	private String email;
	@NotNull
	private String phoneNumber;
	@NotNull
	private List<String> idVehicles;
}
