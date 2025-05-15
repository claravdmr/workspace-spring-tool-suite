package com.example.mechanical_workshop.infrastructure.integrationevents.dto;

import java.util.List;

import com.example.mechanical_workshop.domain.model.Customer;
import com.example.mechanical_workshop.domain.model.Repair;
import com.example.mechanical_workshop.domain.model.ReplacementPart;
import com.example.mechanical_workshop.domain.model.StatusRepair;
import com.example.mechanical_workshop.domain.model.Vehicle;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class WorkOrderEventDto {
	@NotNull
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
