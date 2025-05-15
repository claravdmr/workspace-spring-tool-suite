package com.example.mechanical_workshop.infrastructure.apirest.dto.work_order;

import java.util.List;

import com.example.mechanical_workshop.domain.model.Customer;
import com.example.mechanical_workshop.domain.model.Repair;
import com.example.mechanical_workshop.domain.model.ReplacementPart;
import com.example.mechanical_workshop.domain.model.StatusRepair;
import com.example.mechanical_workshop.domain.model.Vehicle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestPatchWorkOrderDto {
	private Customer customer;
	private Vehicle vehicle;
	private String mileage;
	private List<Repair> repairs;
	private List<ReplacementPart> replacementParts;
	private String date;
	private StatusRepair status;
}
