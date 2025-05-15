package com.demo.arq.infrastructure.database.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.demo.arq.application.port.output.PeceraRepositoryOutputPort;
import com.demo.arq.domain.model.Pecera;
import com.demo.arq.infrastructure.database.entity.PeceraEntity;
import com.demo.arq.infrastructure.database.mapper.PeceraToPeceraEntityMapper;
import com.demo.arq.infrastructure.database.repository.PeceraRepository;

import jakarta.validation.Valid;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PeceraRepositoryService implements PeceraRepositoryOutputPort {

	@Autowired
	PeceraRepository peceraRepository;

	@Autowired
	PeceraToPeceraEntityMapper peceraToPeceraEntityMapper;

	@Override
	@Cacheable(value = "peceras", key = "#pageable")
	public Page<Pecera> obtenerPeceras(@Valid Pageable pageable) {
		log.debug("obtenerPeceras");

		Page<PeceraEntity> listaEntity = peceraRepository.findAll(pageable);

		return peceraToPeceraEntityMapper.fromOutputToInput(listaEntity);
	}

	@Override
	@Cacheable(value = "peceras", key = "#id")
	public Optional<Pecera> obtenerPecera(@Valid String id) {
		log.debug("obtenerPecera");

		Optional<PeceraEntity> recursoEntity = peceraRepository.findById(id);

		return peceraToPeceraEntityMapper.fromOutputToInput(recursoEntity);
	}

	@Override
	@CacheEvict(value = "peceras", allEntries = true)
	public String crearPecera(@Valid Pecera input) {
		log.debug("crearPecera");

		PeceraEntity entity = peceraToPeceraEntityMapper.fromInputToOutput(input);

		return peceraRepository.save(entity).getId();
	}

	@SneakyThrows
	@Override
	@CacheEvict(value = "peceras", allEntries = true)
	public void modificarPecera(@Valid Pecera input) {
		log.debug("updatePecera");

		PeceraEntity entity = peceraToPeceraEntityMapper.fromInputToOutput(input);

		peceraRepository.save(entity);
	}

	@Override
	@CacheEvict(value = "peceras", allEntries = true)
	public void eliminarPecera(@Valid String id) {
		log.debug("deletePecera");

		peceraRepository.deleteById(id);
	}
}
