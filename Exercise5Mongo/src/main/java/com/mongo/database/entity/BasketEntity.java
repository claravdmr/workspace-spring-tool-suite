package com.mongo.database.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("BASKETS")
public class BasketEntity {
	
	@Id
	private String id;
	private Double totalCost;
	private BasketState state;
	
	
	public BasketEntity() {
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

	
	public BasketState getState() {
		return state;
	}

	
	public void setState(BasketState state) {
		this.state = state;
	}
	
}
