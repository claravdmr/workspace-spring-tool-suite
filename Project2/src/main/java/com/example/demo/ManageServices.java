package com.example.demo;

import java.util.List;

public interface ManageServices {

	List<Person> getDatabase();

	Person getPersonByDni(String dni);

	List<Person> getPersonByAge(int age);

	List<Person> getPersonByName(String name, String surname);

	long numberPeopleByAge(int age);

	List<Vehicle> getVehiclesByDni(String dni);

	List<Property> getPropertiesByDni(String dni);

	long numberVehiclesPerModel(Models model);

	List<Person> getPersonByRegitration(String registration);

	List<Person> getPersonByNumberOfRooms(int rooms);

	long numberOfPeopleByNumberOfRooms(int rooms);

	List<Person> getPersonByAddress(String address);

	List<String> getDnisByAge(int age);

	String getNameByRegitration(String registration);

	String getDniByAddress(String address);

	int getAverageAgeByAddress(String address);

}
