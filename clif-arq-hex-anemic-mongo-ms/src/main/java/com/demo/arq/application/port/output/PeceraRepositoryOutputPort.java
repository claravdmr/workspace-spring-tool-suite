package com.demo.arq.application.port.output;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.demo.arq.domain.model.Pecera;
import jakarta.validation.Valid;

public interface PeceraRepositoryOutputPort {

	public Page<Pecera> obtenerPeceras(@Valid Pageable pageable);

	public Optional<Pecera> obtenerPecera(@Valid String id);

	public String crearPecera(@Valid Pecera input);

	public void modificarPecera(@Valid Pecera input);

	public void eliminarPecera(@Valid String id);

}
