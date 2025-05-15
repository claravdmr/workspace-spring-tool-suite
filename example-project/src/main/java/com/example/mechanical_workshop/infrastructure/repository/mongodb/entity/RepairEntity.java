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
@Document("REPAIRS")
public class RepairEntity {

	private String description;
	private double labor;
}
