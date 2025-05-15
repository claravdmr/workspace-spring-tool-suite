package com.demo2;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//anything that accepts client request or exposes end points is the controller

@RestController
@RequestMapping("/v1/software-engineers")

public class SoftwareEngineerController {
	
	private final SoftwareEngineerService softwareEngineerService;
	private static final Logger log = LoggerFactory.getLogger(SoftwareEngineerController.class);
	private SoftwareEngineerRepository softwareEngineerRepository;

	

	public SoftwareEngineerController(SoftwareEngineerService softwareEngineerService) {
		this.softwareEngineerService = softwareEngineerService;
	}



	@GetMapping
	public List<SoftwareEngineer> getEngineers() {
		log.debug("getEngineer");
		
		//return List.of(new SoftwareEngineer("Clara", "java"), new SoftwareEngineer("Gianluca", "python"));
		
		return softwareEngineerService.getAllSoftwareEngineers();
	}
	
	@PostMapping
	public ResponseEntity<Void> postSoftwareEngineer(@RequestBody SoftwareEngineer se){
		
		log.debug("postSoftwareEngineer");
		
		//se.setId(null);
//		se.setName("Gianluca");
//		se.setTechStack("Python");
		
		SoftwareEngineer seSave = softwareEngineerRepository.save(se);
		
		return ResponseEntity.noContent().build();
		
	}

}
