package com.demo.arq.application.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.demo.arq.application.port.input.PeceraServiceInputPort;
import com.demo.arq.application.port.output.PeceraProducerOutputPort;
import com.demo.arq.application.port.output.PeceraRepositoryOutputPort;
import com.demo.arq.application.util.Constants;
import com.demo.arq.application.util.Errors;
import com.demo.arq.domain.exception.BusinessException;
import com.demo.arq.domain.mapper.PeceraPatchMapper;
import com.demo.arq.domain.model.Pecera;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PeceraService implements PeceraServiceInputPort {

	@Autowired
	PeceraRepositoryOutputPort peceraRepository;

	@Autowired
	PeceraProducerOutputPort peceraProducer;

	@Autowired
	PeceraPatchMapper peceraPatchMapper;

	@Override
	public Page<Pecera> obtenerPeceras(@Valid Pageable pageable) throws BusinessException {
		log.debug("obtenerPeceras");

		if (pageable.getPageSize() >= Constants.MAXIMUM_PAGINATION) {
			throw new BusinessException(Errors.MAXIMUM_PAGINATION_EXCEEDED);
		}

		return peceraRepository.obtenerPeceras(pageable);
	}

	@Override
	public Optional<Pecera> obtenerPecera(@Valid String id) {
		log.debug("obtenerPecera");

		return peceraRepository.obtenerPecera(id);
	}

	@Override
	public String crearPecera(@Valid Pecera input) {
		log.debug("crearPecera");

		String nuevoId = peceraRepository.crearPecera(input);

		input.setId(nuevoId);

		peceraProducer.eventoCreacionPecera(input);

		return nuevoId;
	}

	@Override
	public void modificacionParcialPecera(@Valid Pecera input) throws BusinessException {
		log.debug("modificacionParcialPecera");

		Optional<Pecera> opt = peceraRepository.obtenerPecera(input.getId());
		if (!opt.isPresent()) {
			throw new BusinessException(Errors.PECERA_NOT_FOUND);
		}

		peceraPatchMapper.update(opt.get(), input);

		peceraRepository.modificarPecera(opt.get());
		peceraProducer.eventoModificacionPecera(opt.get());
	}

	@Override
	public void modificacionTotalPecera(@Valid Pecera input) throws BusinessException {
		log.debug("modificacionTotalPecera");

		Optional<Pecera> opt = peceraRepository.obtenerPecera(input.getId());
		if (!opt.isPresent()) {
			throw new BusinessException(Errors.PECERA_NOT_FOUND);
		}

		peceraRepository.modificarPecera(input);
		peceraProducer.eventoModificacionPecera(input);
	}

	@Override
	public void eliminarPecera(@Valid String id) throws BusinessException {
		log.debug("eliminarPecera");

		Optional<Pecera> opt = peceraRepository.obtenerPecera(id);
		if (!opt.isPresent()) {
			throw new BusinessException(Errors.PECERA_NOT_FOUND);
		}

		peceraRepository.eliminarPecera(id);
		peceraProducer.eventoEliminacionPecera(opt.get());
	}
}
