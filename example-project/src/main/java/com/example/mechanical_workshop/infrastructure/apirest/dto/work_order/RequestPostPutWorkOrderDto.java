package com.example.mechanical_workshop.infrastructure.apirest.dto.work_order;

import java.util.List;

import com.example.mechanical_workshop.domain.model.Customer;
import com.example.mechanical_workshop.domain.model.ReplacementPart;
import com.example.mechanical_workshop.domain.model.StatusRepair;
import com.example.mechanical_workshop.domain.model.Vehicle;
import com.example.mechanical_workshop.infrastructure.apirest.dto.repair.RequestPostPutRepairDto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestPostPutWorkOrderDto {
	@NotNull
	private Customer customer;

	@NotNull
	private Vehicle vehicle;

	@NotNull
	private String date;

	@NotNull
	private List<RequestPostPutRepairDto> repairs;

	private List<ReplacementPart> replacementParts;

	@NotNull
	private String mileage;

	@NotNull
	private StatusRepair status;
}
