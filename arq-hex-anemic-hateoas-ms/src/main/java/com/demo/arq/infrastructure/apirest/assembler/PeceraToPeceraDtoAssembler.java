package com.demo.arq.infrastructure.apirest.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.demo.arq.domain.model.Pecera;
import com.demo.arq.infrastructure.apirest.controller.PeceraController;
import com.demo.arq.infrastructure.apirest.dto.response.PeceraDto;
import com.demo.arq.infrastructure.apirest.mapper.PeceraToPeceraDtoMapper;

@Component
public class PeceraToPeceraDtoAssembler extends RepresentationModelAssemblerSupport<Pecera, PeceraDto> {

	@Autowired
	PeceraToPeceraDtoMapper peceraToPeceraDto;

	public PeceraToPeceraDtoAssembler() {
		super(PeceraController.class, PeceraDto.class);
	}

	@Override
	public PeceraDto toModel(Pecera entity) {
		PeceraDto dto = peceraToPeceraDto.fromInputToOutput(entity);
		// Añadimos el link al propio recurso
		dto.add(linkTo(methodOn(PeceraController.class).getPecera(entity.getId())).withSelfRel());
		// Añadimos el link a la coleccion a la que pertenecemos
		dto.add(linkTo(methodOn(PeceraController.class).getPeceras(null)).withRel(IanaLinkRelations.COLLECTION));
		// Se pueden añadir links de accions o diferentes recursos disponibles,
		// lo necesario para informar de todo lo que se puede hacer con el recurso
		return dto;
	}

	@Override
	public CollectionModel<PeceraDto> toCollectionModel(Iterable<? extends Pecera> entities) {
		CollectionModel<PeceraDto> dtos = super.toCollectionModel(entities);
		// Añadimos el link al propio recurso de la coleccion
		dtos.add(linkTo(methodOn(PeceraController.class).getPeceras(null)).withSelfRel());
		return dtos;
	}
}
