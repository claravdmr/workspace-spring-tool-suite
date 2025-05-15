package com.example.mechanical_workshop.infrastructure.apirest.dto.customer;

import com.example.mechanical_workshop.domain.model.DocumentType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestPatchCustomerDto {
	private String firstName;
	private String lastName;
	private String address;
	private DocumentType documentType;
	private String documentNumber;
	private String email;
	private String phoneNumber;
}
