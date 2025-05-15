package com.hexarq.application.port.output;

import com.hexarq.domain.model.FishTank;

import jakarta.validation.Valid;

public interface FishTankProducerOutputPort {
	
	public void eventCreationFishTank(@Valid FishTank input);
	
	public void eventModifyFishTank(@Valid FishTank input);
	
	public void eventDeleteFishTank(@Valid FishTank input);

}
