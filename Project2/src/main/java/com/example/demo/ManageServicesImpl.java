package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import org.jeasy.random.EasyRandom;

import jakarta.annotation.PostConstruct;

public class ManageServicesImpl implements ManageServices {

	private static final EasyRandom ER = new EasyRandom();

	private List<Person> db;

	@PostConstruct
	public void postContruct() {
		db = new ArrayList<>();
		db.addAll(ER.objects(Person.class, 10).toList());
		db.forEach(System.out::println);

	}

	@Override
	public List<Person> getDatabase() {

		return db;
	}

	@Override
	public Person getPersonByDni(String dni) {
		return db.stream().filter(p -> p.getDni().equalsIgnoreCase(dni)).findFirst().orElse(null);
	}

	@Override
	public List<Person> getPersonByAge(int age) {
		return db.stream().filter(p -> p.getAge() == age).toList();
	}

	@Override
	public List<Person> getPersonByName(String name, String surname) {
		return db.stream().filter(
				person -> person.getFirstName().equalsIgnoreCase(name) && person.getSurname().equalsIgnoreCase(surname))
				.toList();
	}

	@Override
	public long numberPeopleByAge(int age) {
		return db.stream().filter(p -> p.getAge() == age).count();
	}

	@Override
	public List<Vehicle> getVehiclesByDni(String dni) {
		return db.stream().filter(p -> p.getDni().equalsIgnoreCase(dni)).flatMap(p -> p.getVehicles().stream())
				.toList();
	}

	@Override
	public List<Property> getPropertiesByDni(String dni) {
		return db.stream().filter(p -> p.getDni().equalsIgnoreCase(dni)).flatMap(p -> p.getProperties().stream())
				.toList();
	}

	@Override
	public long numberVehiclesPerModel(Models model) {
		return db.stream().flatMap(person -> person.getVehicles().stream())
				.filter(vehicle -> vehicle.getModel().equals(model)).count();
	}

	@Override
	public List<Person> getPersonByRegitration(String registration) {
		return null;
	}

	@Override
	public List<Person> getPersonByNumberOfRooms(int rooms) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long numberOfPeopleByNumberOfRooms(int rooms) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Person> getPersonByAddress(String address) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getDnisByAge(int age) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNameByRegitration(String registration) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDniByAddress(String address) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getAverageAgeByAddress(String address) {
		// TODO Auto-generated method stub
		return 0;
	}

}
