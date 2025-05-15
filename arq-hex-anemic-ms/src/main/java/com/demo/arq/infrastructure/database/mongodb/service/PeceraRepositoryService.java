package com.demo.arq.infrastructure.database.mongodb.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.demo.arq.application.port.output.PeceraRepositoryOutputPort;
import com.demo.arq.domain.model.Pecera;
import com.demo.arq.infrastructure.database.mongodb.entity.PeceraEntity;
import com.demo.arq.infrastructure.database.mongodb.mapper.PeceraToPeceraEntityMapper;
import com.demo.arq.infrastructure.database.mongodb.repository.PeceraRepository;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PeceraRepositoryService implements PeceraRepositoryOutputPort {

	@Autowired
	PeceraRepository peceraRepository;

	@Autowired
	PeceraToPeceraEntityMapper peceraToPeceraEntityMapper;

	@Override
	public Page<Pecera> obtenerPeceras(@Valid Pageable pageable) {
		log.debug("obtenerPeceras");

		Page<PeceraEntity> pageEntity = peceraRepository.findByEliminado(false, pageable);

		return peceraToPeceraEntityMapper.fromOutputToInput(pageEntity);
	}

	@Override
	public Optional<Pecera> obtenerPecera(@Valid String id) {
		log.debug("obtenerPecera");

		Optional<PeceraEntity> recursoEntity = peceraRepository.findByIdAndEliminado(id, false);

		return peceraToPeceraEntityMapper.fromOutputToInput(recursoEntity);
	}

	@Override
	public String crearPecera(@Valid Pecera input) {
		log.debug("crearPecera");

		PeceraEntity entity = peceraToPeceraEntityMapper.fromInputToOutput(input);
		entity.setId(null);
		entity.setEliminado(false);

		return peceraRepository.save(entity).getId();
	}

	@Override
	public void modificarPecera(@Valid Pecera input) {
		log.debug("modificarPecera");

		PeceraEntity entity = peceraToPeceraEntityMapper.fromInputToOutput(input);

		peceraRepository.save(entity);
	}

	@Override
	public void eliminarPecera(@Valid String id) {
		log.debug("eliminarPecera");

		Optional<PeceraEntity> opt = peceraRepository.findByIdAndEliminado(id, false);
		if (opt.isPresent()) {
			opt.get().setEliminado(true);
			peceraRepository.save(opt.get());
		}
	}
}
