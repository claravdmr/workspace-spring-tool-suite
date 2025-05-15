package com.demo.arq.application.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.demo.arq.application.port.input.PeceraServiceInputPort;
import com.demo.arq.application.port.output.PeceraProducerOutputPort;
import com.demo.arq.application.port.output.PeceraRepositoryOutputPort;
import com.demo.arq.application.util.Constants;
import com.demo.arq.application.util.Errors;
import com.demo.arq.domain.command.DeletePeceraCommand;
import com.demo.arq.domain.command.PatchPeceraCommand;
import com.demo.arq.domain.command.PostPeceraCommand;
import com.demo.arq.domain.command.PutPeceraCommand;
import com.demo.arq.domain.event.DeletedPeceraEvent;
import com.demo.arq.domain.event.PatchedPeceraEvent;
import com.demo.arq.domain.event.PostedPeceraEvent;
import com.demo.arq.domain.event.PutPeceraEvent;
import com.demo.arq.domain.exception.BusinessException;
import com.demo.arq.domain.mapper.PeceraPatchMapper;
import com.demo.arq.domain.mapper.PeceraPutMapper;
import com.demo.arq.domain.model.Pecera;
import com.demo.arq.domain.query.GetPeceraQuery;
import com.demo.arq.domain.query.GetPecerasQuery;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PeceraService implements PeceraServiceInputPort {

	@Autowired
	PeceraRepositoryOutputPort peceraRepositoryOutputPort;

	@Autowired
	PeceraProducerOutputPort peceraProducerOutputPort;

	@Autowired
	PeceraPatchMapper peceraPatchMapper;

	@Autowired
	PeceraPutMapper peceraPutMapper;

	@Override
	public Page<Pecera> obtenerPeceras(@Valid GetPecerasQuery query) throws BusinessException {
		log.debug("obtenerPeceras");

		if (query.getPageable().getPageSize() > Constants.MAXIMUM_PAGINATION) {
			throw new BusinessException(Errors.MAXIMUM_PAGINATION_EXCEEDED);
		}

		return peceraRepositoryOutputPort.obtenerPeceras(query.getPageable());
	}

	@Override
	public Optional<Pecera> obtenerPecera(@Valid GetPeceraQuery query) {
		log.debug("obtenerPecera");

		return peceraRepositoryOutputPort.obtenerPecera(query.getId());
	}

	@Override
	public String crearPecera(@Valid PostPeceraCommand command) {
		log.debug("crearPecera");

		String nuevoId = peceraRepositoryOutputPort
				.crearPecera(Pecera.builder().value(command.getValue()).valueObject(command.getValueObject()).build());

		peceraProducerOutputPort.eventoCreacionPecera(PostedPeceraEvent.builder().id(nuevoId).value(command.getValue())
				.valueObject(command.getValueObject()).build());

		return nuevoId;
	}

	@Override
	public void modificacionParcialPecera(@Valid PatchPeceraCommand command) throws BusinessException {
		log.debug("modificacionParcialPecera");
		Optional<Pecera> opt = peceraRepositoryOutputPort.obtenerPecera(command.getId());
		if (!opt.isPresent()) {
			throw new BusinessException(Errors.PECERA_NOT_FOUND);
		}
		Pecera updated = opt.get();
		peceraPatchMapper.update(updated, command);
		peceraRepositoryOutputPort.modificarPecera(updated);

		peceraProducerOutputPort.eventoModificacionParcialPecera(PatchedPeceraEvent.builder().id(command.getId())
				.value(command.getValue()).valueObject(command.getValueObject()).build());
	}

	@Override
	public void modificacionTotalPecera(@Valid PutPeceraCommand command) throws BusinessException {
		log.debug("modificacionTotalPecera");
		Optional<Pecera> opt = peceraRepositoryOutputPort.obtenerPecera(command.getId());
		if (!opt.isPresent()) {
			throw new BusinessException(Errors.PECERA_NOT_FOUND);
		}
		Pecera updated = opt.get();
		peceraPutMapper.update(updated, command);
		peceraRepositoryOutputPort.modificarPecera(updated);

		peceraProducerOutputPort.eventoModificacionTotalPecera(PutPeceraEvent.builder().id(command.getId())
				.value(command.getValue()).valueObject(command.getValueObject()).build());
	}

	@Override
	public void eliminarPecera(@Valid DeletePeceraCommand command) throws BusinessException {
		log.debug("eliminarPecera");
		Optional<Pecera> opt = peceraRepositoryOutputPort.obtenerPecera(command.getId());
		if (!opt.isPresent()) {
			throw new BusinessException(Errors.PECERA_NOT_FOUND);
		}
		peceraRepositoryOutputPort.eliminarPecera(command.getId());

		peceraProducerOutputPort.eventoEliminacionPecera(DeletedPeceraEvent.builder().id(command.getId()).build());
	}
}
