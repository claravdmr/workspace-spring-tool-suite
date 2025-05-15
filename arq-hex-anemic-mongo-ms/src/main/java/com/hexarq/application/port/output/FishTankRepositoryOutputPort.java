package com.hexarq.application.port.output;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hexarq.domain.model.FishTank;

import jakarta.validation.Valid;

public interface FishTankRepositoryOutputPort {

	public Page<FishTank> getFishTanks(@Valid Pageable pageable);

	public Optional<FishTank> getFishTank(@Valid String id);
	
	public String createFishTank(@Valid FishTank input);
	
	public void modifyFishTank(@Valid FishTank input);
	
	public void deleteFishTank(@Valid String id) ;
}
