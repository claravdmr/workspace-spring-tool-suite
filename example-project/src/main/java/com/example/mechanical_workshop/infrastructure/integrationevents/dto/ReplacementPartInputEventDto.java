package com.example.mechanical_workshop.infrastructure.integrationevents.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class ReplacementPartInputEventDto {
	@NotNull
	private String partNumber;
	@NotNull
	private String description;
	@NotNull
	private String retailPrice;
	@NotNull
	private String amount;
}
