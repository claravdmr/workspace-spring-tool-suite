package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class CustomApplicationReadyEvent implements ApplicationListener<ApplicationReadyEvent> {

	@Value("${my.property.string}")
	private String myPropertyString;

	@Value("${my.property.int}")
	private int myPropertyint;

	@Autowired
	ManageServices manageServices;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		System.out.println("on application event");

		System.out.println(myPropertyString);
		System.out.println(myPropertyint);

		List<Vehicle> vehicles = new ArrayList<>();

		vehicles.add(new Vehicle("111AAA", Models.SEAT, 1200, false));
		vehicles.add(new Vehicle("222BBB", Models.AUDI, 1300, true));
		vehicles.add(new Vehicle("333CCC", Models.TESLA, 1400, false));
		vehicles.add(new Vehicle("444DDD", Models.BMW, 1500, true));
		vehicles.add(new Vehicle("555EEE", Models.HONDA, 1600, false));
		vehicles.add(new Vehicle("666FFF", Models.SEAT, 1700, true));
		vehicles.add(new Vehicle("777GGG", Models.TESLA, 1800, false));

		// to get list of a specific model
		List<Vehicle> teslaVehicles = vehicles.stream().filter(v -> v.getModel().equals(Models.TESLA)).toList();
		teslaVehicles.forEach(System.out::println);
		// System.out.println(teslaVehicles);

		// list of registrations
		List<String> registrations = vehicles.stream().map(v -> v.getRegistration()).toList();
		registrations.forEach(System.out::println);
		// System.out.println(teslaVehicles);

		// ordered ascending kms
		List<Vehicle> orderByKms = vehicles.stream().sorted((v1, v2) -> Integer.compare(v1.getKms(), v2.getKms()))
				.toList();
		orderByKms.forEach(System.out::println);

		// total kms
		int totalKms = vehicles.stream().map(v -> v.getKms()).reduce(0,
				(runningTotal, currentValue) -> runningTotal + currentValue);
		System.out.println(totalKms);

	}

//	@PostConstruct
//	public void postContruct() {
//		System.out.println("post construct");
//	}
//	
//	@Scheduled(fixedDelay = 1000L)
//	public void doSomething() {
//		System.out.println("doSomething @Scheduled");
//	}
//	
//	@Scheduled(fixedRate = 1000L)
//	public void doSomething2() {
//		System.out.println("doSomething2 @Scheduled");
//	}
//	
//	@Scheduled(initialDelay = 1000L, fixedRate = 1000L)
//	public void doSomething3() {
//		System.out.println("doSomething2 @Scheduled");
//	}

}
