package com.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "VEHICLES")
public class Vehicle {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String model;
	
	
	@Column(nullable = true)
	private String brand;
	
	//constructors
	public Vehicle() {}
	

	public Vehicle(String model, String brand) {
		this.model = model;
		this.brand = brand;
	}


	//getters and setters
	
	public String getModel() {
		return model;
	}


	
	public void setModel(String model) {
		this.model = model;
	}


	
	public String getBrand() {
		return brand;
	}


	
	public void setBrand(String brand) {
		this.brand = brand;
	}
	
	
	
}
