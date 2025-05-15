package com.class4.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jeasy.random.EasyRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.class4.dto.Doctor;
import com.class4.dto.Patient;
import com.class4.resttemplate.DataServicesInterface;
import com.class4.resttemplate.Dato;

import jakarta.annotation.PostConstruct;

@RestController
public class ControllerPeopleManager {

	private static final Logger log = LoggerFactory.getLogger(ControllerPeopleManager.class);
	private static final EasyRandom ER = new EasyRandom();
	List<Doctor> doctors = new ArrayList<>();
	List<Patient> patients = new ArrayList<>();

	// -----------------------------------------------------------------------------------------------------------//

	// @Qualifier("RestTemplate")
	@Autowired
	private DataServicesInterface DataServicesImpl;

	@GetMapping("/services/post")
	public ResponseEntity<String> getPost() {

		Dato dato = new Dato();
		dato.setDato1("dato1");
		dato.setDato2("dato2");

		String salida = DataServicesImpl.postDato(dato);

		if (salida != null) {
			return ResponseEntity.ok(salida);
		}

		return ResponseEntity.noContent().build();
	}

	@GetMapping("/services/get")
	public ResponseEntity<String> getGet() {

		String salida = DataServicesImpl.getDato();

		if (salida != null) {
			return ResponseEntity.ok(salida);
		}

		return ResponseEntity.noContent().build();
	}

	@GetMapping("/services/put")
	public ResponseEntity<String> getPut() {

		Dato dato = new Dato();
		dato.setDato1("dato for put");

		String salida = DataServicesImpl.putDato(dato);

		if (salida != null) {
			return ResponseEntity.ok(salida);
		}

		return ResponseEntity.noContent().build();
	}

	@GetMapping("/services/delete")
	public ResponseEntity<String> getDelete() {

		String salida = DataServicesImpl.deleteDato();

		if (salida != null) {
			return ResponseEntity.ok(salida);
		}

		return ResponseEntity.noContent().build();
	}

	@PostConstruct
	public void addInfo() {
		doctors.addAll(ER.objects(Doctor.class, 10).toList());
		patients.addAll(ER.objects(Patient.class, 2).toList());
	}

	@GetMapping("/doctors")
	public ResponseEntity<List<Doctor>> getDoctors() {
		log.debug("getDoctors");
		if (!doctors.isEmpty()) {
			return ResponseEntity.ok(doctors);
		}

		return ResponseEntity.noContent().build();
	}

	@GetMapping("/patients")
	public ResponseEntity<List<Patient>> getPatients() {
		log.debug("getPatients");
		if (!patients.isEmpty()) {
			return ResponseEntity.ok(patients);
		}

		return ResponseEntity.noContent().build();
	}

	@PostMapping("/doctors")
	public ResponseEntity<Void> registerDoctor(@RequestBody Doctor doctor) {
		log.debug("registerDoctor");

		String id = UUID.randomUUID().toString();
		doctor.setDoctorId(id);

		doctors.add(doctor);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();

		return ResponseEntity.created(uri).build();
	}

	@DeleteMapping("/patients/{id-patient}")
	public ResponseEntity<Void> deletePatient(@PathVariable("id-patient") String patientId) {
		log.debug("deletePatient");

		for (int i = 0; i < patients.size(); i++) {
			if (patients.get(i).getId().equalsIgnoreCase(patientId)) {
				patients.remove(i);
				break;
			}
		}
		return ResponseEntity.noContent().build();

	}

	@PostMapping("/patients")
	public ResponseEntity<Void> registerPatient(@RequestBody Patient patient) {
		log.debug("registerPatient");

		String id = UUID.randomUUID().toString();
		patient.setId(id);
		patient.setAppointments(new ArrayList<>());
		patient.setMedicalRecord(null);

		patients.add(patient);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();

		log.info(id);

		return ResponseEntity.created(uri).build();
	}

	@PatchMapping("/patients")
	public ResponseEntity<Void> modifyPatient(@RequestBody Patient patient) {
		log.debug("modifyPatient");

		for (Patient p : patients) {

			if (p.getId().equalsIgnoreCase(patient.getId())) {

				p.setMedicalRecord(p.getMedicalRecord());
				p.setAppointments(p.getAppointments());
				p.setName(p.getName());
			}
		}

		return ResponseEntity.noContent().build();
	}

	@GetMapping("/patients/{id-patient}/doctors")
	public ResponseEntity<List<String>> getDoctorsByPatient(@PathVariable("id-patient") String patientId) {
		log.debug("getDoctorsByPatient");

		for (int i = 0; i < patients.size(); i++) {
			if (patients.get(i).getId().equalsIgnoreCase(patientId)) {

				List<String> result = patients.get(i).getAppointments().stream()
						.map(appointment -> appointment.getDoctorId()).toList();

				return ResponseEntity.ok(result);
			}
		}
		return ResponseEntity.noContent().build();

	}

}
