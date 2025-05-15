package com.example.mechanical_workshop.infrastructure.repository.mongodb.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
@Document("WORK_ORDERS")
public class WorkOrderEntity {
	@Id
	private String id;
	private Long workOrderNumber;
	private Customer customer;
	private Vehicle vehicle;
	private String mileage;
	private String date;
	private List<Repair> repairs;
	private List<ReplacementPart> replacementParts;
	private StatusRepair status;
	boolean eliminado;
}
