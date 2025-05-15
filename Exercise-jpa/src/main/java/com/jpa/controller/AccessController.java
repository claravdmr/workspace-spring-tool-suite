package com.jpa.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jpa.entity.Access;
import com.jpa.entity.Action;
import com.jpa.repository.AccessRepository;

@RequestMapping("/access")
@RestController
public class AccessController {
	
	@Autowired
	private AccessRepository accessRepository;
	
	@GetMapping
	public ResponseEntity<List<Access>> getAllAccess(){
		List<Access> result = accessRepository.findAll();
		if(result.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(result);
	}
	
	@PostMapping
	public ResponseEntity<Void> postAccess(@RequestBody Access access){
		
		access.setId(null); // this avoids ids being sent in the body and messing up the system.
		
		access.setDate(LocalDateTime.now());

		Access accessResult = accessRepository.save(access);
		
		//this needs an URI
		
		System.out.println(accessResult.getId());
		
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/vehicle")
	public ResponseEntity<String> getVehiclesInside(@RequestParam("reg") String reg){
		List<Access> accessEnter = accessRepository.findByRegAndActionOrderByDateDesc(reg, Action.ENTER); 
		List<Access> accessExit = accessRepository.findByRegAndActionOrderByDateDesc(reg, Action.EXIT); 
		
		if (accessExit.isEmpty() && accessEnter.isEmpty()) {
			return ResponseEntity.noContent().build();
			
		} else if (!accessEnter.isEmpty() && accessExit.isEmpty()) {
			return ResponseEntity.ok(reg);
			
		} else if ( ! accessEnter.isEmpty() && ! accessExit.isEmpty()) {
			
			if (accessEnter.get(0).getDate().isAfter(accessExit.get(0).getDate())) {
				return ResponseEntity.ok(reg);
			}
		}
		return ResponseEntity.noContent().build();
	}
}
