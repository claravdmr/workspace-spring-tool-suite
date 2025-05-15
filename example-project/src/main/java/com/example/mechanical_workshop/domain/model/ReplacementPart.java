package com.example.mechanical_workshop.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReplacementPart {
	private String partNumber;
	private String description;
	private String retailPrice;
	private String amount;
}
