package com.hexarq.application.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hexarq.application.port.input.FishTankServiceInputPort;
import com.hexarq.application.port.output.FishTankProducerOutputPort;
import com.hexarq.application.port.output.FishTankRepositoryOutputPort;
import com.hexarq.application.util.Constants;
import com.hexarq.application.util.Errors;
import com.hexarq.domain.exception.BusinessException;
import com.hexarq.domain.mapper.FishTankPatchMapper;
import com.hexarq.domain.model.FishTank;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j //auto generates log variable.
@Service
//this class contains validation of data 
public class FishTankService implements FishTankServiceInputPort {
	
	@Autowired
	FishTankRepositoryOutputPort fishtankRepository;
	
	@Autowired
	FishTankProducerOutputPort fishtankProducer;
	
	@Autowired
	FishTankPatchMapper fishTankPatchMapper;
	
	@Override
	public Page<FishTank> getFishTanks(@Valid Pageable pageable) throws BusinessException {
		log.debug("getFishTanks");
		
		if (pageable.getPageSize() >= Constants.MAXIMUM_PAGINATION) {
			throw new BusinessException(Errors.MAXIMUM_PAGINATION_EXCEEDED);
		}
		
		return fishtankRepository.getFishTanks(pageable);
	}

	@Override
	public Optional<FishTank> getFishTank(@Valid String id) {
		log.debug("getFishTank");
		return fishtankRepository.getFishTank(id);
	}

	@Override
	public String createFishTank(@Valid FishTank input) {
		log.debug("createFishTank");
		
		String newId = fishtankRepository.createFishTank(input);
		
		input.setId(newId);
		
		fishtankProducer.eventCreationFishTank(input);
		
		return newId;
	}

	@Override
	public void parcialModifyFishTank(@Valid FishTank input) throws BusinessException {
		log.debug("parcialModifyFishTank");
		
		Optional<FishTank> opt = fishtankRepository.getFishTank(input.getId());
		if (opt.isEmpty()) {
			throw new BusinessException(Errors.FISHTANK_NOT_FOUND);
		}
		
		
		
//		if(input.getValue() != null) {
//			opt.get().setValue(input.getValue());
//		}
//		
//		if (input.getValueObject() != null) {
//			if (input.getValueObject().getValue() != null) {
//				opt.get().setValueObject(ValueObject.builder().value(input.getValueObject().getValue()).build());
//			} else {
//				opt.get().setValueObject(ValueObject.builder().build());
//			}
//		}
		
		fishTankPatchMapper.update(opt.get(), input);
		
		fishtankRepository.modifyFishTank(opt.get());
		fishtankProducer.eventModifyFishTank(opt.get());
		
	}

	@Override
	public void totalModifyFishTank(@Valid FishTank input) throws BusinessException {
		
		log.debug("totalModifyFishTank");
		
		Optional<FishTank> opt = fishtankRepository.getFishTank(input.getId());
		if (opt.isEmpty()) {
			throw new BusinessException(Errors.FISHTANK_NOT_FOUND);
		}
		
		fishtankRepository.modifyFishTank(input);
		fishtankProducer.eventModifyFishTank(input);		
	}

	@Override
	public void deleteFishTank(@Valid String id) throws BusinessException {
		
		log.debug("deleteFishTank");
		
		Optional<FishTank> opt = fishtankRepository.getFishTank(id);
		if (opt.isEmpty()) {
			throw new BusinessException(Errors.FISHTANK_NOT_FOUND);
		}
		
		fishtankRepository.deleteFishTank(id);
		fishtankProducer.eventDeleteFishTank(opt.get());
		
	}

}
