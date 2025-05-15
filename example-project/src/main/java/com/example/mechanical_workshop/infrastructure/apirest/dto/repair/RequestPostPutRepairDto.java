package com.example.mechanical_workshop.infrastructure.apirest.dto.repair;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestPostPutRepairDto {

	@NotNull
	private String description;

	private double labor;
}
