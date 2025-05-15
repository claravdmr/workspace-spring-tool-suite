package com.example.mechanical_workshop.infrastructure.integrationevents.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class ReplacementPartEventDto {
	private String partNumber;
	private String description;
	private String retailPrice;
	private String amount;
}
