package com.jpa;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.jpa.entity.Person;
import com.jpa.entity.Vehicle;
import com.jpa.repository.PersonRepository;
import com.jpa.repository.VehicleRepository;


@SpringBootTest
class JpaApplicationTests {

	@Autowired
	PersonRepository personRepository;
	
	@Autowired
	VehicleRepository vehicleRepository;
	
	@BeforeEach
	void beforeEachTest() {
		personRepository.deleteAll();
		vehicleRepository.deleteAll();
	}
	
	@Test
	void contextLoads() {
	}

	@Test
	void count() {
		System.out.println(personRepository.count());
		System.out.println(personRepository.save(new Person("CLARA")));
		System.out.println(personRepository.save(new Person("GIANLUCA")));
		System.out.println(personRepository.save(new Person("THOMAS")));
		System.out.println(personRepository.count());
	}
	
	@Test
	void insertPeople() {
		System.out.println(personRepository.count());
		for (int i = 0; i < 10; i++) {
			System.out.println(personRepository.save(new Person("Name " + i, 10 + i)));
		}
		System.out.println(personRepository.count());

	}
	
	@Test
	void update() {
		List<Person> people = personRepository.findAll();
		if (!people.isEmpty()) {
			people.get(10).setAge(60);
			personRepository.save(people.get(10));
		}
	}
	
	@Test
	void delete() {
		List<Person> people = personRepository.findAll();
		if (!people.isEmpty()) {
			personRepository.deleteById(people.get(0).getId());
		}
	}
	
	@Test
	void ejemplo() {
	    List<Person> personName = personRepository.findByName("Name 2");
	    if (!personName.isEmpty()) {
	        System.out.println("Names: " + personName.get(0).getAge());
	    }

	    List<Person> personAge = personRepository.findByAge(50000);
	    if (!personAge.isEmpty()) {
	        System.out.println("Ages: " + personAge.get(0).getAge());
	    }

	    List<Person> personNameAge = personRepository.findByNameAndAge("Name 2", 2);
	    if (!personNameAge.isEmpty()) {
	        System.out.println("Names and ages: " + personNameAge.get(0).getName());
	    }
	}
	
	@Transactional
	@Test
	void eliminar() {
	    Long filasAfectadas = personRepository.deleteAllByName("Name 2");

	    System.out.println(filasAfectadas);

	    List<Person> salida = personRepository.findByNameIgnoreCase("Name 2");

	    Assertions.assertTrue(salida.isEmpty());
	}

	@Test
	void withCars() {
		System.out.println(personRepository.count());

		for (int i = 0; i < 10; i++) {
			Vehicle vehicle = vehicleRepository.save(new Vehicle("Niro" + i, "Kia" + i));
			Vehicle vehicle2 = vehicleRepository.save(new Vehicle("Sportage" + i, "Kia" + i));

			personRepository.save(new Person("Name" + i, 10, List.of(vehicle, vehicle2)));
		}
		
		System.out.println(personRepository.count());

	}

}

