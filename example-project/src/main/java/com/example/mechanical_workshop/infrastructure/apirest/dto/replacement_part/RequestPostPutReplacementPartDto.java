package com.example.mechanical_workshop.infrastructure.apirest.dto.replacement_part;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestPostPutReplacementPartDto {
	@NotNull
	private String partNumber;
	@NotNull
	private String description;
	@NotNull
	private String retailPrice;
	@NotNull
	private String amount;
}
