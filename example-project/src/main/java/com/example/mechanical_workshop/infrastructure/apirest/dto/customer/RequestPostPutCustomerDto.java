package com.example.mechanical_workshop.infrastructure.apirest.dto.customer;

import com.example.mechanical_workshop.domain.model.DocumentType;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestPostPutCustomerDto {
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
}
