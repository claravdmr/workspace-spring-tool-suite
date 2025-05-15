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
public class CustomerEventDto {
	@NotNull
	private String id;
	private Long customerNumber;
	private String firstName;
	private String lastName;
	private String address;
	private DocumentType documentType;
	private String documentNumber;
	private String email;
	private String phoneNumber;
	private List<String> idVehicles;
}
