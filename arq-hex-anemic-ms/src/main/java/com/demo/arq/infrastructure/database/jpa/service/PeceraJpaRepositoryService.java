package com.demo.arq.infrastructure.database.jpa.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.demo.arq.application.port.output.PeceraJpaRepositoryOutputPort;
import com.demo.arq.domain.model.Pecera;
import com.demo.arq.infrastructure.database.jpa.entity.PeceraJpaEntity;
import com.demo.arq.infrastructure.database.jpa.mapper.PeceraJpaToPeceraJpaEntityMapper;
import com.demo.arq.infrastructure.database.jpa.repository.PeceraJpaRepository;

import jakarta.validation.Valid;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PeceraJpaRepositoryService implements PeceraJpaRepositoryOutputPort {

	@Autowired
	PeceraJpaRepository peceraRepository;

	@Autowired
	PeceraJpaToPeceraJpaEntityMapper peceraToPeceraEntityMapper;

	@Override
	@Cacheable(value = "peceras", key = "#pageable")
	public Page<Pecera> obtenerPeceras(@Valid Pageable pageable) {
		log.debug("obtenerPeceras");

		Page<PeceraJpaEntity> listaEntity = peceraRepository.findByEliminado(false, pageable);

		return peceraToPeceraEntityMapper.fromOutputToInput(listaEntity);
	}

	@Override
	@Cacheable(value = "peceras", key = "#id")
	public Optional<Pecera> obtenerPecera(@Valid BigDecimal id) {
		log.debug("obtenerPecera");

		Optional<PeceraJpaEntity> recursoEntity = peceraRepository.findByIdAndEliminado(id, false);

		return peceraToPeceraEntityMapper.fromOutputToInput(recursoEntity);
	}

	@Override
	@CacheEvict(value = "peceras", allEntries = true)
	public BigDecimal crearPecera(@Valid Pecera input) {
		log.debug("crearPecera");

		PeceraJpaEntity entity = peceraToPeceraEntityMapper.fromInputToOutput(input);

		BigDecimal nuevoId = peceraRepository.getNextValSequence();
		entity.setId(nuevoId);
		entity.setEliminado(false);

		peceraRepository.save(entity);

		return nuevoId;
	}

	@SneakyThrows
	@Override
	@CacheEvict(value = "peceras", allEntries = true)
	public void modificarPecera(@Valid Pecera input) {
		log.debug("modificarPecera");

		PeceraJpaEntity entity = peceraToPeceraEntityMapper.fromInputToOutput(input);

		peceraRepository.save(entity);
	}

	@Override
	@CacheEvict(value = "peceras", allEntries = true)
	public void eliminarPecera(@Valid BigDecimal id) {
		log.debug("eliminarPecera");

		Optional<PeceraJpaEntity> opt = peceraRepository.findByIdAndEliminado(id, false);
		if (opt.isPresent()) {
			opt.get().setEliminado(true);
			peceraRepository.save(opt.get());
		}
	}
}
