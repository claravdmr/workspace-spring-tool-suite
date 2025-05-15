package com.demo.arq.infrastructure.apirest.controller;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.demo.arq.application.port.input.PeceraServiceInputPort;
import com.demo.arq.domain.command.DeletePeceraCommand;
import com.demo.arq.domain.command.PatchPeceraCommand;
import com.demo.arq.domain.command.PostPeceraCommand;
import com.demo.arq.domain.command.PutPeceraCommand;
import com.demo.arq.domain.exception.BusinessException;
import com.demo.arq.domain.model.Pecera;
import com.demo.arq.domain.query.GetPeceraQuery;
import com.demo.arq.domain.query.GetPecerasQuery;
import com.demo.arq.infrastructure.apirest.dto.request.PatchPeceraDto;
import com.demo.arq.infrastructure.apirest.dto.request.PostPutPeceraDto;
import com.demo.arq.infrastructure.apirest.mapper.PeceraToPeceraDtoMapper;
import com.demo.arq.infrastructure.apirest.mapper.ValueObjectToValueObjectDtoMapper;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("rawtypes")
@Slf4j
@RestController
@RequestMapping("/peceras")
public class PeceraController {

	@Autowired
	PeceraServiceInputPort peceraService;

	@Autowired
	ValueObjectToValueObjectDtoMapper valueObjectToValueObjectDtoMapper;

	@Autowired
	PeceraToPeceraDtoMapper peceraToPeceraDtoMapper;

	@GetMapping
	public ResponseEntity getPeceras(Pageable pageable) {
		log.debug("getPeceras");

		Page<Pecera> listDomain;
		try {
			listDomain = peceraService.obtenerPeceras(GetPecerasQuery.builder().pageable(pageable).build());
		} catch (BusinessException e) {
			log.error("Error Obteniendo Peceras", e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}

		return ResponseEntity.ok(peceraToPeceraDtoMapper.fromInputToOutput(listDomain));
	}

	@GetMapping("/{pecera-id}")
	public ResponseEntity getPecera(@Valid @PathVariable("pecera-id") String id) {
		log.debug("getPecera");

		Optional<Pecera> domain = peceraService.obtenerPecera(GetPeceraQuery.builder().id(id).build());

		if (domain.isPresent()) {
			return ResponseEntity.ok(peceraToPeceraDtoMapper.fromInputToOutput(domain.get()));
		} else {
			return ResponseEntity.noContent().build();
		}
	}

	@PostMapping
	public ResponseEntity postPecera(@Valid @RequestBody PostPutPeceraDto dto) {
		log.debug("postPecera");

		String idNewPecera = peceraService.crearPecera(PostPeceraCommand.builder().value(dto.getValue())
				.valueObject(valueObjectToValueObjectDtoMapper.fromOutputToInput(dto.getValueObject())).build());

		URI locationHeader = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(idNewPecera)
				.toUri();

		return ResponseEntity.created(locationHeader).build();
	}

	@PatchMapping("/{pecera-id}")
	public ResponseEntity patchPecera(@Valid @PathVariable("pecera-id") String id,
			@Valid @RequestBody PatchPeceraDto dto) {
		log.debug("patchPecera");

		try {
			peceraService.modificacionParcialPecera(PatchPeceraCommand.builder().id(id).value(dto.getValue())
					.valueObject(valueObjectToValueObjectDtoMapper.fromOutputToInput(dto.getValueObject())).build());
		} catch (BusinessException e) {
			log.error("Error Modificando Pecera", e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}

		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{pecera-id}")
	public ResponseEntity putPecera(@Valid @PathVariable("pecera-id") String id,
			@Valid @RequestBody PostPutPeceraDto dto) {
		log.debug("putPecera");

		try {
			peceraService.modificacionTotalPecera(PutPeceraCommand.builder()
					.id(id)
					.value(dto.getValue())
					.valueObject(valueObjectToValueObjectDtoMapper.fromOutputToInput(dto.getValueObject()))
					.build());
		} catch (BusinessException e) {
			log.error("Error Modificando Pecera", e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{pecera-id}")
	public ResponseEntity deletePecera(@Valid @PathVariable("pecera-id") String id) {
		log.debug("deletePecera");

		try {
			peceraService.eliminarPecera(DeletePeceraCommand.builder().id(id).build());
		} catch (BusinessException e) {
			log.error("Error Eliminando Pecera", e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}

		return ResponseEntity.noContent().build();
	}

}
