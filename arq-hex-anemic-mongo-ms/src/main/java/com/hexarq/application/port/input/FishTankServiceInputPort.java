package com.hexarq.application.port.input;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hexarq.domain.exception.BusinessException;
import com.hexarq.domain.model.FishTank;

import jakarta.validation.Valid;

public interface FishTankServiceInputPort {
	
	//sometimes there is too much info if you use lists and can cause memory full exceptions, and the page acts as a list but enables larger volumes of data. 
	Page<FishTank> getFishTanks(@Valid Pageable pageable) throws BusinessException;

	Optional<FishTank> getFishTank(@Valid String id);
	
	String createFishTank(@Valid FishTank input);
	
	void parcialModifyFishTank(@Valid FishTank input) throws BusinessException;
	
	void totalModifyFishTank(@Valid FishTank input) throws BusinessException;

	void deleteFishTank (@Valid String id) throws BusinessException;

}
