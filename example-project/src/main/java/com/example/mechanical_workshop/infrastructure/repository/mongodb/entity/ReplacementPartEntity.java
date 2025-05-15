package com.example.mechanical_workshop.infrastructure.repository.mongodb.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("REPLACEMENT_PARTS")
public class ReplacementPartEntity {
	private String partNumber;
	private String description;
	private String retailPrice;
	private String amount;
	boolean eliminado;
}
