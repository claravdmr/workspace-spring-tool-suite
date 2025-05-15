package com.example.mechanical_workshop.domain.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
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
