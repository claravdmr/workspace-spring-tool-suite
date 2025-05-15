package com.MockitoTest.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.MockitoTest.dto.PenaCarcelesRequest;
import com.MockitoTest.dto.PenaCarcelesResponse;
import com.MockitoTest.entity.CarcelEntity;
import com.MockitoTest.entity.PenaEntity;
import com.MockitoTest.entity.TipoPena;
import com.MockitoTest.entity.TipoSeguridadCarcel;
import com.MockitoTest.repository.CarcelRepository;
import com.MockitoTest.repository.PenaRepository;

@RestController
@RequestMapping("/calculos-carcelarios")
@CrossOrigin(origins = "${SECURITY_CORS_ALLOWED_ORIGINS:}")

public class ControladorRest {
	
	private static final Logger log = LoggerFactory.getLogger(ControladorRest.class);
	
	@Autowired
	private CarcelRepository carcelRepository;
	
	@Autowired
	private PenaRepository penaRepository;


	@PatchMapping
	public ResponseEntity<PenaCarcelesResponse> calcularPenaCarceles(@RequestBody PenaCarcelesRequest request) {
		
		log.debug("calcularPenaCarceles");
		
		if (!camposValidos(request)) {
			return ResponseEntity.badRequest().build();
		}
		
		List<PenaEntity> penas = penaRepository.findAllById(request.getPenas());
		
		if (penas.isEmpty()) {
			return ResponseEntity.badRequest().build();
		}
		
		TipoPena tipoPena = obtenerTipoPena(penas);
		
		List<CarcelEntity> carceles = obtenerCarceles(request, tipoPena);
		
		if (carceles.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		
		// Montamos la salida
		PenaCarcelesResponse response = new PenaCarcelesResponse();
		response.setCarceles(carceles.stream().map(CarcelEntity::getId).toList());
		
		response.setPenaTotal(
				penas.stream().map(PenaEntity::getDias).reduce(0, (acumulado, nuevo) -> acumulado + nuevo));
		
		return ResponseEntity.ok(response);
	}

	private TipoPena obtenerTipoPena(List<PenaEntity> penas) {
		// Obtenemos el tipo de pena buscando entre las penas enviadas
		
		TipoPena tipoPena = TipoPena.NO_VIOLENTA;
		
		for (PenaEntity pena : penas) {
			
			if (TipoPena.SANGRE.equals(pena.getTipo())) {
				tipoPena = TipoPena.SANGRE;
				break;
			}	
			
			if (TipoPena.VIOLENTA.equals(pena.getTipo())) {
				tipoPena = TipoPena.VIOLENTA;
			
			}
		}
		
		return tipoPena;
	}

	private List<CarcelEntity> obtenerCarceles(PenaCarcelesRequest request, TipoPena tipoPena) {
		// Buscamos la carceles posibles
		
		CarcelEntity busqueda = new CarcelEntity();
		
		busqueda.setParaMenores(request.getEdad() < 18);
		busqueda.setParaMujeres(request.getEsMujer());
		
		switch (tipoPena) {
			case SANGRE:
				busqueda.setTipoSeguridad(TipoSeguridadCarcel.MAXIMA);
				break;
			case VIOLENTA:
				busqueda.setTipoSeguridad(TipoSeguridadCarcel.MEDIA);
				break;
			default:
				busqueda.setTipoSeguridad(TipoSeguridadCarcel.BAJA);
		}
		
		return carcelRepository.findAll(Example.of(busqueda, ExampleMatcher.matchingAny()));
	}

	private boolean camposValidos(PenaCarcelesRequest request) {
		
		return request != null && request.getEsMujer() != null && request.getEdad() != null
			&& request.getPenas() != null && !request.getPenas().isEmpty();
	}

}
