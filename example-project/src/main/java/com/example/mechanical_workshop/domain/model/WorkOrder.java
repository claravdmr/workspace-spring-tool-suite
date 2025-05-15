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
public class WorkOrder {
	private String id;
	private Long workOrderNumber;
	private Customer customer;
	private Vehicle vehicle;
	private String mileage;
	private String date;
	private List<Repair> repairs;
	private List<ReplacementPart> replacementParts;
	private StatusRepair status;
}
