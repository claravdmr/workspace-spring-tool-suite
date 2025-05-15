package com.example.mechanical_workshop.infrastructure.apirest.dto.replacement_part;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestPatchReplacementPartDto {
	private String partNumber;
	private String description;
	private String amount;
	private String retailPrice;
}
