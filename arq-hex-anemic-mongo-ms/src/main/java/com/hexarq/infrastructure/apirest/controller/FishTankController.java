package com.hexarq.infrastructure.apirest.controller;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.hexarq.application.port.input.FishTankServiceInputPort;
import com.hexarq.domain.exception.BusinessException;
import com.hexarq.domain.model.FishTank;
import com.hexarq.infrastructure.apirest.dto.request.PostPutFishTankDto;
import com.hexarq.infrastructure.apirest.mapper.FishTankToFishTankDtoMapper;
import com.hexarq.infrastructure.apirest.mapper.FishTankToPatchFishTankDtoMapper;
import com.hexarq.infrastructure.apirest.mapper.FishTankToPostPutFishTankDtoMapper;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

//this controller just connects and perhaps translates errors from http but there is no service logic here.

@Slf4j
@RestController
@RequestMapping("/fishtanks")
@CrossOrigin(origins = "localhost:4200")
@SuppressWarnings("rawtypes")
public class FishTankController {
	
	@Autowired
	FishTankServiceInputPort fishtankService;
	
	@Autowired
	FishTankToPostPutFishTankDtoMapper fishTankToPostPutFishTankDto;
	
	@Autowired
	FishTankToPatchFishTankDtoMapper fishTankToPatchFishTankDtoMapper;
	
	@Autowired
	FishTankToFishTankDtoMapper fishTankToFishTankDtoMapper;
	
	//in the brackets we can just put the class name and any attributes of that class can be request parameters by default.
	@GetMapping
	public ResponseEntity getFishTanks(Pageable pageable) {
		log.debug("getFishTanks");
		
		Page<FishTank> pageDomain;
		
		
		try {
			pageDomain = fishtankService.getFishTanks(pageable);
		} catch(BusinessException e) {
			log.error("Error getting fishtanks", e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
		if(pageDomain==null || pageDomain.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		
		return ResponseEntity.ok(fishTankToFishTankDtoMapper.fromDomainToDto(pageDomain));
		
	}
	
	@GetMapping("/{fishtank-id}")
	public ResponseEntity getFishTank(@Valid @PathVariable("fishtank-id") String id) {
		log.debug("getFishTank");
		
		Optional<FishTank> domain = fishtankService.getFishTank(id);
		
		if(domain.isPresent()) {
			return ResponseEntity.ok(fishTankToFishTankDtoMapper.fromDomainToDto(domain.get()));
		} else {
			return ResponseEntity.noContent().build();
		}
		
	}
	
	@PostMapping("/{fishtank-id}")
	public ResponseEntity postFishTank(@Valid @RequestBody PostPutFishTankDto dto) {
		log.debug("postFishTank");
		
		FishTank domain = fishTankToPostPutFishTankDto.fromDtoToDomain(dto);
		
		String idNewFishTank = fishtankService.createFishTank(domain);
		
		URI locationHeader = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("{id}")
				.buildAndExpand(idNewFishTank)
				.toUri();
		//  //
		
		return ResponseEntity.created(locationHeader).build();

	}
	
	@PatchMapping("/{fishtank-id}")
	public ResponseEntity patchFishTank(@Valid @PathVariable("fishtank-id") String id, @Valid @RequestBody PostPutFishTankDto dto) {
		log.debug("patchFishTank");
		
		FishTank domain = fishTankToPostPutFishTankDto.fromDtoToDomain(dto);
		domain.setId(id);
		
		try {
			fishtankService.parcialModifyFishTank(domain);
		} catch(BusinessException e) {
			log.error("Error modifying (patch) fishtank", e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
		return ResponseEntity.noContent().build();
		
	}
	
	@PutMapping("/{fishtank-id}")
	public ResponseEntity putFishTank(@Valid @PathVariable("fishtank-id") String id, @Valid @RequestBody PostPutFishTankDto dto) {
		log.debug("putFishTank");
		
		FishTank domain = fishTankToPostPutFishTankDto.fromDtoToDomain(dto);
		domain.setId(id);
		
		try {
			fishtankService.parcialModifyFishTank(domain);
		} catch(BusinessException e) {
			log.error("Error modifying (put) fishtank", e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
		return ResponseEntity.noContent().build();
		
	}
	
	@DeleteMapping("/{fishtank-id}")
	public ResponseEntity deleteFishTank(@Valid @PathVariable("fishtank-id") String id) {
		log.debug("deleteFishTank");
		
		try {
			fishtankService.deleteFishTank(id);
		} catch(BusinessException e) {
			log.error("Error deleting fishtank", e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
		return ResponseEntity.noContent().build();
	}
	
	

}
