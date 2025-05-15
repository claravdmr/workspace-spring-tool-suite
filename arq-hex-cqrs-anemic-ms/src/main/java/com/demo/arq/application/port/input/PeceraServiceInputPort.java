package com.demo.arq.application.port.input;

import java.util.Optional;

import org.springframework.data.domain.Page;

import com.demo.arq.domain.command.DeletePeceraCommand;
import com.demo.arq.domain.command.PatchPeceraCommand;
import com.demo.arq.domain.command.PostPeceraCommand;
import com.demo.arq.domain.command.PutPeceraCommand;
import com.demo.arq.domain.exception.BusinessException;
import com.demo.arq.domain.model.Pecera;
import com.demo.arq.domain.query.GetPeceraQuery;
import com.demo.arq.domain.query.GetPecerasQuery;

import jakarta.validation.Valid;

public interface PeceraServiceInputPort {

	Page<Pecera> obtenerPeceras(@Valid GetPecerasQuery query) throws BusinessException;

	Optional<Pecera> obtenerPecera(@Valid GetPeceraQuery query);

	String crearPecera(@Valid PostPeceraCommand command);

	void modificacionParcialPecera(@Valid PatchPeceraCommand command) throws BusinessException;

	void modificacionTotalPecera(@Valid PutPeceraCommand command) throws BusinessException;

	void eliminarPecera(@Valid DeletePeceraCommand command) throws BusinessException;

}