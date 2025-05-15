package arq_hex_parking_access.infrastructure.apirest.controller;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import arq_hex_parking_access.application.ports.input.ParkingServiceInputPort;
import arq_hex_parking_access.domain.command.PayTicketCommand;
import arq_hex_parking_access.domain.query.GetCarsQuery;
import arq_hex_parking_access.domain.query.GetTicketCostQuery;
import arq_hex_parking_access.infrastructure.Utilities;
import arq_hex_parking_access.infrastructure.apirest.dto.PostEntryDto;
import arq_hex_parking_access.infrastructure.apirest.dto.ValidateExitDto;
import arq_hex_parking_access.infrastructure.apirest.mapper.ParkingDtoMapper;

@RestController
@RequestMapping("/parkings")
public class ParkingController {
	
	@Autowired
	ParkingServiceInputPort service;
	
	@Autowired
	ParkingDtoMapper parkingDtoMapper;
	
	
	@PostMapping
	public ResponseEntity createEntry(@RequestBody PostEntryDto dto) {
		
		String entryId = service.createEntry(parkingDtoMapper.map(dto));
		
		URI uri = Utilities.createUri(entryId);
		
		return ResponseEntity.created(uri).build();
	}
	
	@GetMapping("/cars")
	public ResponseEntity getCars(
		@RequestParam("registration") String registration,
		@RequestParam("entryDate") LocalDateTime entryDateTime) {
		
		GetCarsQuery query = GetCarsQuery.builder()
			.entryDateTime(entryDateTime)
			.registration(registration)	
			.build();
		//
		
		List<String> result = service.getCars(query);
		if (result.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		
		return ResponseEntity.ok(result);
		
	}
	
	@PutMapping("/{entry-id}")
	public ResponseEntity payTicket(
		@PathVariable("entry-id") String entryId){
		
		try {
			
			service.payTicket(PayTicketCommand.builder()
				.entryId(entryId)
				.build());
			
		} catch (Exception e) {
			
			return ResponseEntity.noContent().build(); //this should be specialised log error
		}
		
		return ResponseEntity.noContent().build();
		
	}
	
	@GetMapping("/{entry-id}/fees")
	public ResponseEntity getTicketCost(
		@PathVariable("entry-id") String entryId) {
		
		float fee = 0.0f;
		
		try {
			
			fee = service.getTicketCost(GetTicketCostQuery.builder()
				.entryId(entryId)
				.build());
			
		} catch (Exception e) {
			
			return ResponseEntity.noContent().build(); //this should be specialised log error
		}
		
		
		return ResponseEntity.ok(fee);
	}
	
	@PostMapping("/validations")
	public ResponseEntity validateExit(@RequestBody ValidateExitDto dto)  {
		
		boolean exit = false;
		
		try {
			
			service.validateExit(parkingDtoMapper.map(dto));
			
		} catch (Exception e) {
			
			return ResponseEntity.noContent().build(); //this should be specialised log error

		}
		
		return ResponseEntity.ok(exit);
		
	}

}
