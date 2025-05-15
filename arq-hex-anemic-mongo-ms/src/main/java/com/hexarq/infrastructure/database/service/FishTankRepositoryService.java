package com.hexarq.infrastructure.database.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.hexarq.application.port.output.FishTankRepositoryOutputPort;
import com.hexarq.domain.model.FishTank;
import com.hexarq.infrastructure.database.entity.FishTankEntity;
import com.hexarq.infrastructure.database.mapper.FishTankToFishTankEntityMapper;
import com.hexarq.infrastructure.database.repository.FishTankRepository;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j

//this is the service that will adapt to the port - give all the necessary info so that data may be drawn from the database aka the adapter between the service (in application) and the database.
public class FishTankRepositoryService implements FishTankRepositoryOutputPort {

	
	@Autowired
	FishTankRepository fishtankRepository;
	
	@Autowired
	FishTankToFishTankEntityMapper fishTankToFishTankEntityMapper;
	
	@Override
	public Page<FishTank> getFishTanks(@Valid Pageable pageable) {
		log.debug("getFishTanks");
		
		Page<FishTankEntity> pageEntity = fishtankRepository.findByDeleted(false, pageable);
				
		return fishTankToFishTankEntityMapper.fromEntityToDomain(pageEntity);
	}

	@Override
	public Optional<FishTank> getFishTank(@Valid String id) {
		
		log.debug("getFishTank");
		
		Optional<FishTankEntity> entity = fishtankRepository.findByIdAndDeleted(id, false);
		
		return fishTankToFishTankEntityMapper.fromEntityToDomain(entity);
	}

	@Override
	public String createFishTank(@Valid FishTank input) {
		log.debug("createFishTank");
		
		FishTankEntity entity = fishTankToFishTankEntityMapper.fromDomainToEntity(input);		
		entity.setId(null);
		entity.setDeleted(false);
		
		return fishtankRepository.save(entity).getId();
	}

	@Override
	public void modifyFishTank(@Valid FishTank input) {
		log.debug("modifyFishTank");
		
		FishTankEntity entity = fishTankToFishTankEntityMapper.fromDomainToEntity(input);
		
		fishtankRepository.save(entity);
		
	}

	@Override
	public void deleteFishTank(@Valid String id) {
		
		log.debug("deleteFishTank");
		
		Optional<FishTankEntity> opt = fishtankRepository.findByIdAndDeleted(id, false);
		
		if(opt.isPresent()) {
			opt.get().setDeleted(true);
			fishtankRepository.save(opt.get());
		}
		
	}

}
