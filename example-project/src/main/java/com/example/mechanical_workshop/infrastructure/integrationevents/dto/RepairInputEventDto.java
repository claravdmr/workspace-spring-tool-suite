package com.example.mechanical_workshop.infrastructure.integrationevents.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class RepairInputEventDto {
	@NotNull
	private String description;
	@NotNull
	private double labor;
}
