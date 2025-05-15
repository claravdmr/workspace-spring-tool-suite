package com.class6.controller;

import java.net.URI;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.class6.dto.Car;

@RestController
@RequestMapping("/cars")
public class Controller {

	@Value("${fee}")
	private Float feePerSecond;

	private List<Car> cars = new ArrayList<>();

	public List<Car> getCars() {
		return cars;
	}

	public void setCars(List<Car> cars) {
		this.cars = cars;
	}

	@GetMapping
	public ResponseEntity<List<Car>> getCars2() {
		return ResponseEntity.ok(cars);
	}

	@GetMapping("{car-id}")
	public ResponseEntity<Car> getCar(@PathVariable("car-id") String carId) {

		for (Car car : cars) {
			if (car.getId().equalsIgnoreCase(carId)) {
				return ResponseEntity.ok(car);
			}
		}

		return ResponseEntity.noContent().build();
	}

	@PostMapping
	public ResponseEntity<Void> carEntry(@RequestBody Car car) {

		String carId = UUID.randomUUID().toString();

		car.setId(carId);
		car.setEntryDate(LocalDateTime.now());
		cars.add(car);

		return ResponseEntity.created(createUri(carId)).build();

	}

	@GetMapping("/{id-car}/fee")
	public ResponseEntity<Float> calculateFee(@PathVariable("id-car") String carId) {

		Float fee = 0.0F;

		for (Car car : cars) {
			if (car.getId().equalsIgnoreCase(carId)) {
				long milisecs = Timestamp.valueOf(car.getEntryDate()).getTime();
				long milisecs2 = Timestamp.valueOf(LocalDateTime.now()).getTime();

				long timeParked = milisecs2 - milisecs;
				fee = (timeParked / 1000) * feePerSecond;
			}
		}

		return ResponseEntity.ok(fee);
	}

	@PatchMapping
	public ResponseEntity<Void> carExit(@RequestBody Car car) {

		for (Car c : cars) {
			if (c.getId().equalsIgnoreCase(car.getId())) {
				c.setExitDate(LocalDateTime.now());
				break;
			}
		}

		return ResponseEntity.noContent().build();

	}

	@DeleteMapping("/{id-car}")
	public ResponseEntity<Void> removeCar(@PathVariable("id-car") String carId) {

		for (Car c : cars) {
			if (c.getId().equalsIgnoreCase(carId)) {
				c.setDeleted(true);

				break;
			}
		}

		return ResponseEntity.noContent().build();

	}

	private URI createUri(String id) {
		return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
	}

}
