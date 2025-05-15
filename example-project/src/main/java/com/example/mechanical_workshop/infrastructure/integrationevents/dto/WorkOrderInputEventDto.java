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
public class WorkOrderInputEventDto {
	@NotNull
	private String id;
	@NotNull
	private Long workOrderNumber;
	@NotNull
	private Customer customer;
	@NotNull
	private Vehicle vehicle;
	@NotNull
	private String mileage;
	@NotNull
	private String date;
	@NotNull
	private List<Repair> repairs;
	@NotNull
	private List<ReplacementPart> replacementParts;
	@NotNull
	private StatusRepair status;
}
