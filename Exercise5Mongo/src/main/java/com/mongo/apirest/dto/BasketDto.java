package com.mongo.apirest.dto;

public class BasketDto {
	
	private String id;
	private Double totalCost;
	private String state;
	
	public BasketDto() {
	}

	public BasketDto(Double totalCost, String state) {
		this.totalCost = totalCost;
		this.state = state;
	}
	
	public String getId() {
		return id;
	}

	
	public void setId(String id) {
		this.id = id;
	}

	public Double getTotalCost() {
		return totalCost;
	}
	
	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}

	
	public String getState() {
		return state;
	}

	
	public void setState(String state) {
		this.state = state;
	}
}
