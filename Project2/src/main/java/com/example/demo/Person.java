package com.example.demo;

import java.util.List;

public class Person {

	private String firstName;
	private String surname;
	private String dni;
	private int age;
	private List<Vehicle> vehicles;
	private List<Property> properties;

	// constructor
	public Person(String firstName, String surname, String dni, int age) {
		this.firstName = firstName;
		this.surname = surname;
		this.dni = dni;
		this.age = age;
	}

	// getters and setters
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public List<Vehicle> getVehicles() {
		return vehicles;
	}

	public List<Property> getProperties() {
		return properties;
	}

}
