package com.demo.arq.application.port.input;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.demo.arq.domain.exception.BusinessException;
import com.demo.arq.domain.model.Pecera;

import jakarta.validation.Valid;

public interface PeceraServiceInputPort {

	Page<Pecera> obtenerPeceras(@Valid Pageable pageable) throws BusinessException;

	Optional<Pecera> obtenerPecera(@Valid String id);

	String crearPecera(@Valid Pecera input);

	void modificacionParcialPecera(@Valid Pecera input) throws BusinessException;

	void modificacionTotalPecera(@Valid Pecera input) throws BusinessException;

	void eliminarPecera(@Valid String id) throws BusinessException;

}