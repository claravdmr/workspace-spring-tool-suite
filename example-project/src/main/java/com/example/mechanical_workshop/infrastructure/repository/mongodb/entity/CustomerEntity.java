package com.example.mechanical_workshop.infrastructure.repository.mongodb.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.mechanical_workshop.domain.model.DocumentType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("CUSTOMERS")
public class CustomerEntity {

	@Id
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
	boolean eliminado;
}
