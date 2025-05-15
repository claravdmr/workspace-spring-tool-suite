package com.MockitoTest;

import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import com.MockitoTest.controller.ControladorRest;
import com.MockitoTest.dto.PenaCarcelesRequest;
import com.MockitoTest.dto.PenaCarcelesResponse;
import com.MockitoTest.entity.CarcelEntity;
import com.MockitoTest.entity.PenaEntity;
import com.MockitoTest.entity.TipoPena;
import com.MockitoTest.entity.TipoSeguridadCarcel;
import com.MockitoTest.repository.CarcelRepository;
import com.MockitoTest.repository.PenaRepository;

@SuppressWarnings("unchecked") // this is to avoid a sonar warning that was appearing on some classes.
@SpringBootTest

class MockitoTestApplicationTests {

	private static final Long PENA_ID = 11l;
	private static final Long PENA_2_ID = 22l;
	private static final Long CARCEL_ID = 33l;

	@InjectMocks
	ControladorRest controlador;

	@Mock
	CarcelRepository carcelRepository;

	@Mock
	PenaRepository penaRepository;

	@BeforeEach
	void beforeAll() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testSinEntrada() {
	
		PenaCarcelesRequest entrada = null;
		
		// launch test
		ResponseEntity<PenaCarcelesResponse> response = controlador.calcularPenaCarceles(entrada);
		
		// validate response
		Assert.assertTrue(response.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(400)));
	}

	@Test
	void testSinPenas() {
		
		PenaCarcelesRequest entrada = new PenaCarcelesRequest();
		entrada.setEdad(1);
		entrada.setEsMujer(true);
		entrada.setPenas(List.of(PENA_ID));
		
		// launch test
		ResponseEntity<PenaCarcelesResponse> response = controlador.calcularPenaCarceles(entrada);
		
		// validate response
		Assert.assertTrue(response.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(400)));

	}

	@Test
	void testSinCarceles() {

		PenaEntity penaEntity = new PenaEntity();
		penaEntity.setId(PENA_ID);
		penaEntity.setDias(1);
		penaEntity.setTipo(TipoPena.NO_VIOLENTA);
		Mockito.when(penaRepository.findAllById(Mockito.any())).thenReturn(List.of(penaEntity));
		
		PenaCarcelesRequest entrada = new PenaCarcelesRequest();
		entrada.setEdad(1);
		entrada.setEsMujer(true);
		entrada.setPenas(List.of(PENA_ID));
		
		// launch test
		ResponseEntity<PenaCarcelesResponse> response = controlador.calcularPenaCarceles(entrada);
		
		// validate response
		Assert.assertTrue(response.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(204)));

	}

	
	@Test
	void testPenaNoViolentaMujerAdulta() {

		PenaEntity penaEntity = new PenaEntity();
		penaEntity.setId(PENA_ID);
		penaEntity.setDias(1);
		penaEntity.setTipo(TipoPena.NO_VIOLENTA);
		Mockito.when(penaRepository.findAllById(Mockito.any())).thenReturn(List.of(penaEntity));
		
		CarcelEntity carcelEntity = new CarcelEntity();
		carcelEntity.setId(CARCEL_ID);
		carcelEntity.setParaMenores(false);
		carcelEntity.setParaMujeres(true);
		carcelEntity.setTipoSeguridad(TipoSeguridadCarcel.BAJA);
		Mockito.when(carcelRepository.findAll(Mockito.any(Example.class))).thenReturn(List.of(carcelEntity));
		
		PenaCarcelesRequest entrada = new PenaCarcelesRequest();
		entrada.setEdad(20);
		entrada.setEsMujer(true);
		entrada.setPenas(List.of(PENA_ID));
		
		// launch test
		ResponseEntity<PenaCarcelesResponse> response = controlador.calcularPenaCarceles(entrada);
		
		// validate response
		Assert.assertTrue(response.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(200)));
		Assert.assertTrue(response.hasBody());
		Assert.assertNotNull(response.getBody().getCarceles());
		Assert.assertFalse(response.getBody().getCarceles().isEmpty());
		Assert.assertTrue(response.getBody().getCarceles().stream().anyMatch(carcel -> carcel.equals(CARCEL_ID)));
		
		
	}

	@Test
	void testPenaNoViolentaMujerMenor() {
		
		PenaEntity penaEntity = new PenaEntity();
		penaEntity.setId(PENA_ID);
		penaEntity.setDias(1);
		penaEntity.setTipo(TipoPena.NO_VIOLENTA);
		Mockito.when(penaRepository.findAllById(Mockito.any())).thenReturn(List.of(penaEntity));
		
		CarcelEntity carcelEntity = new CarcelEntity();
		carcelEntity.setId(CARCEL_ID);
		carcelEntity.setParaMenores(true);
		carcelEntity.setParaMujeres(true);
		carcelEntity.setTipoSeguridad(TipoSeguridadCarcel.BAJA);
		Mockito.when(carcelRepository.findAll(Mockito.any(Example.class))).thenReturn(List.of(carcelEntity));
		
		PenaCarcelesRequest entrada = new PenaCarcelesRequest();
		entrada.setEdad(1);
		entrada.setEsMujer(true);
		entrada.setPenas(List.of(PENA_ID));
		
		// launch test
		ResponseEntity<PenaCarcelesResponse> response = controlador.calcularPenaCarceles(entrada);
		
		// validate response
		Assert.assertTrue(response.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(200)));
		Assert.assertTrue(response.hasBody());
		Assert.assertNotNull(response.getBody().getCarceles());
		Assert.assertFalse(response.getBody().getCarceles().isEmpty());
		Assert.assertTrue(response.getBody().getCarceles().stream().anyMatch(carcel -> carcel.equals(CARCEL_ID)));

	}

	@Test
	void testPenaNoViolentaHombreAdulto() {
		
		PenaEntity penaEntity = new PenaEntity();
		penaEntity.setId(PENA_ID);
		penaEntity.setDias(1);
		penaEntity.setTipo(TipoPena.NO_VIOLENTA);
		Mockito.when(penaRepository.findAllById(Mockito.any())).thenReturn(List.of(penaEntity));
		
		CarcelEntity carcelEntity = new CarcelEntity();
		carcelEntity.setId(CARCEL_ID);
		carcelEntity.setParaMenores(false);
		carcelEntity.setParaMujeres(false);
		carcelEntity.setTipoSeguridad(TipoSeguridadCarcel.BAJA);
		Mockito.when(carcelRepository.findAll(Mockito.any(Example.class))).thenReturn(List.of(carcelEntity));
		
		PenaCarcelesRequest entrada = new PenaCarcelesRequest();
		entrada.setEdad(60);
		entrada.setEsMujer(false);
		entrada.setPenas(List.of(PENA_ID));
		
		// launch test
		ResponseEntity<PenaCarcelesResponse> response = controlador.calcularPenaCarceles(entrada);
		
		// validate response
		Assert.assertTrue(response.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(200)));
		Assert.assertTrue(response.hasBody());
		Assert.assertNotNull(response.getBody().getCarceles());
		Assert.assertFalse(response.getBody().getCarceles().isEmpty());
		Assert.assertTrue(response.getBody().getCarceles().stream().anyMatch(carcel -> carcel.equals(CARCEL_ID)));

	}

	@Test
	void testPenaNoViolentaHombreMenor() {
		
		PenaEntity penaEntity = new PenaEntity();
		penaEntity.setId(PENA_ID);
		penaEntity.setDias(1);
		penaEntity.setTipo(TipoPena.NO_VIOLENTA);
		Mockito.when(penaRepository.findAllById(Mockito.any())).thenReturn(List.of(penaEntity));
		
		CarcelEntity carcelEntity = new CarcelEntity();
		carcelEntity.setId(CARCEL_ID);
		carcelEntity.setParaMenores(true);
		carcelEntity.setParaMujeres(false);
		carcelEntity.setTipoSeguridad(TipoSeguridadCarcel.BAJA);
		Mockito.when(carcelRepository.findAll(Mockito.any(Example.class))).thenReturn(List.of(carcelEntity));
		
		PenaCarcelesRequest entrada = new PenaCarcelesRequest();
		entrada.setEdad(6);
		entrada.setEsMujer(false);
		entrada.setPenas(List.of(PENA_ID));
		
		// launch test
		ResponseEntity<PenaCarcelesResponse> response = controlador.calcularPenaCarceles(entrada);
		
		// validate response
		Assert.assertTrue(response.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(200)));
		Assert.assertTrue(response.hasBody());
		Assert.assertNotNull(response.getBody().getCarceles());
		Assert.assertFalse(response.getBody().getCarceles().isEmpty());
		Assert.assertTrue(response.getBody().getCarceles().stream().anyMatch(carcel -> carcel.equals(CARCEL_ID)));


	}

	@Test
	void testPenaViolentaMujerAdulta() {
		
		PenaEntity penaEntity = new PenaEntity();
		penaEntity.setId(PENA_ID);
		penaEntity.setDias(1);
		penaEntity.setTipo(TipoPena.VIOLENTA);
		Mockito.when(penaRepository.findAllById(Mockito.any())).thenReturn(List.of(penaEntity));
		
		CarcelEntity carcelEntity = new CarcelEntity();
		carcelEntity.setId(CARCEL_ID);
		carcelEntity.setParaMenores(false);
		carcelEntity.setParaMujeres(true);
		carcelEntity.setTipoSeguridad(TipoSeguridadCarcel.MEDIA);
		Mockito.when(carcelRepository.findAll(Mockito.any(Example.class))).thenReturn(List.of(carcelEntity));
		
		PenaCarcelesRequest entrada = new PenaCarcelesRequest();
		entrada.setEdad(20);
		entrada.setEsMujer(true);
		entrada.setPenas(List.of(PENA_ID));
		
		// launch test
		ResponseEntity<PenaCarcelesResponse> response = controlador.calcularPenaCarceles(entrada);
		
		// validate response
		Assert.assertTrue(response.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(200)));
		Assert.assertTrue(response.hasBody());
		Assert.assertNotNull(response.getBody().getCarceles());
		Assert.assertFalse(response.getBody().getCarceles().isEmpty());
		Assert.assertTrue(response.getBody().getCarceles().stream().anyMatch(carcel -> carcel.equals(CARCEL_ID)));

	}

	@Test
	void testPenaViolentaMujerMenor() {
		
		PenaEntity penaEntity = new PenaEntity();
		penaEntity.setId(PENA_ID);
		penaEntity.setDias(1);
		penaEntity.setTipo(TipoPena.VIOLENTA);
		Mockito.when(penaRepository.findAllById(Mockito.any())).thenReturn(List.of(penaEntity));
		
		CarcelEntity carcelEntity = new CarcelEntity();
		carcelEntity.setId(CARCEL_ID);
		carcelEntity.setParaMenores(true);
		carcelEntity.setParaMujeres(true);
		carcelEntity.setTipoSeguridad(TipoSeguridadCarcel.MEDIA);
		Mockito.when(carcelRepository.findAll(Mockito.any(Example.class))).thenReturn(List.of(carcelEntity));
		
		PenaCarcelesRequest entrada = new PenaCarcelesRequest();
		entrada.setEdad(2);
		entrada.setEsMujer(true);
		entrada.setPenas(List.of(PENA_ID));
		
		// launch test
		ResponseEntity<PenaCarcelesResponse> response = controlador.calcularPenaCarceles(entrada);
		
		// validate response
		Assert.assertTrue(response.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(200)));
		Assert.assertTrue(response.hasBody());
		Assert.assertNotNull(response.getBody().getCarceles());
		Assert.assertFalse(response.getBody().getCarceles().isEmpty());
		Assert.assertTrue(response.getBody().getCarceles().stream().anyMatch(carcel -> carcel.equals(CARCEL_ID)));

	}

	@Test
	void testPenaViolentaHombreAdulto() {
		
		PenaEntity penaEntity = new PenaEntity();
		penaEntity.setId(PENA_ID);
		penaEntity.setDias(1);
		penaEntity.setTipo(TipoPena.VIOLENTA);
		Mockito.when(penaRepository.findAllById(Mockito.any())).thenReturn(List.of(penaEntity));
		
		CarcelEntity carcelEntity = new CarcelEntity();
		carcelEntity.setId(CARCEL_ID);
		carcelEntity.setParaMenores(false);
		carcelEntity.setParaMujeres(false);
		carcelEntity.setTipoSeguridad(TipoSeguridadCarcel.MEDIA);
		Mockito.when(carcelRepository.findAll(Mockito.any(Example.class))).thenReturn(List.of(carcelEntity));
		
		PenaCarcelesRequest entrada = new PenaCarcelesRequest();
		entrada.setEdad(20);
		entrada.setEsMujer(false);
		entrada.setPenas(List.of(PENA_ID));
		
		// launch test
		ResponseEntity<PenaCarcelesResponse> response = controlador.calcularPenaCarceles(entrada);
		
		// validate response
		Assert.assertTrue(response.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(200)));
		Assert.assertTrue(response.hasBody());
		Assert.assertNotNull(response.getBody().getCarceles());
		Assert.assertFalse(response.getBody().getCarceles().isEmpty());
		Assert.assertTrue(response.getBody().getCarceles().stream().anyMatch(carcel -> carcel.equals(CARCEL_ID)));

	}

	@Test
	void testPenaViolentaHombreMenor() {
		
		PenaEntity penaEntity = new PenaEntity();
		penaEntity.setId(PENA_ID);
		penaEntity.setDias(1);
		penaEntity.setTipo(TipoPena.VIOLENTA);
		Mockito.when(penaRepository.findAllById(Mockito.any())).thenReturn(List.of(penaEntity));
		
		CarcelEntity carcelEntity = new CarcelEntity();
		carcelEntity.setId(CARCEL_ID);
		carcelEntity.setParaMenores(true);
		carcelEntity.setParaMujeres(false);
		carcelEntity.setTipoSeguridad(TipoSeguridadCarcel.MEDIA);
		Mockito.when(carcelRepository.findAll(Mockito.any(Example.class))).thenReturn(List.of(carcelEntity));
		
		PenaCarcelesRequest entrada = new PenaCarcelesRequest();
		entrada.setEdad(2);
		entrada.setEsMujer(false);
		entrada.setPenas(List.of(PENA_ID));
		
		// launch test
		ResponseEntity<PenaCarcelesResponse> response = controlador.calcularPenaCarceles(entrada);
		
		// validate response
		Assert.assertTrue(response.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(200)));
		Assert.assertTrue(response.hasBody());
		Assert.assertNotNull(response.getBody().getCarceles());
		Assert.assertFalse(response.getBody().getCarceles().isEmpty());
		Assert.assertTrue(response.getBody().getCarceles().stream().anyMatch(carcel -> carcel.equals(CARCEL_ID)));

	}

	@Test
	void testPenaSangreMujerAdulta() {
		
		PenaEntity penaEntity = new PenaEntity();
		penaEntity.setId(PENA_ID);
		penaEntity.setDias(1);
		penaEntity.setTipo(TipoPena.SANGRE);
		Mockito.when(penaRepository.findAllById(Mockito.any())).thenReturn(List.of(penaEntity));
		
		CarcelEntity carcelEntity = new CarcelEntity();
		carcelEntity.setId(CARCEL_ID);
		carcelEntity.setParaMenores(false);
		carcelEntity.setParaMujeres(true);
		carcelEntity.setTipoSeguridad(TipoSeguridadCarcel.MAXIMA);
		Mockito.when(carcelRepository.findAll(Mockito.any(Example.class))).thenReturn(List.of(carcelEntity));
		
		PenaCarcelesRequest entrada = new PenaCarcelesRequest();
		entrada.setEdad(20);
		entrada.setEsMujer(true);
		entrada.setPenas(List.of(PENA_ID));
		
		// launch test
		ResponseEntity<PenaCarcelesResponse> response = controlador.calcularPenaCarceles(entrada);
		
		// validate response
		Assert.assertTrue(response.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(200)));
		Assert.assertTrue(response.hasBody());
		Assert.assertNotNull(response.getBody().getCarceles());
		Assert.assertFalse(response.getBody().getCarceles().isEmpty());
		Assert.assertTrue(response.getBody().getCarceles().stream().anyMatch(carcel -> carcel.equals(CARCEL_ID)));


	}

	@Test
	void testPenaSangreMujerMenor() {
		
		PenaEntity penaEntity = new PenaEntity();
		penaEntity.setId(PENA_ID);
		penaEntity.setDias(1);
		penaEntity.setTipo(TipoPena.SANGRE);
		Mockito.when(penaRepository.findAllById(Mockito.any())).thenReturn(List.of(penaEntity));
		
		CarcelEntity carcelEntity = new CarcelEntity();
		carcelEntity.setId(CARCEL_ID);
		carcelEntity.setParaMenores(true);
		carcelEntity.setParaMujeres(true);
		carcelEntity.setTipoSeguridad(TipoSeguridadCarcel.MAXIMA);
		Mockito.when(carcelRepository.findAll(Mockito.any(Example.class))).thenReturn(List.of(carcelEntity));
		
		PenaCarcelesRequest entrada = new PenaCarcelesRequest();
		entrada.setEdad(2);
		entrada.setEsMujer(true);
		entrada.setPenas(List.of(PENA_ID));
		
		// launch test
		ResponseEntity<PenaCarcelesResponse> response = controlador.calcularPenaCarceles(entrada);
		
		// validate response
		Assert.assertTrue(response.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(200)));
		Assert.assertTrue(response.hasBody());
		Assert.assertNotNull(response.getBody().getCarceles());
		Assert.assertFalse(response.getBody().getCarceles().isEmpty());
		Assert.assertTrue(response.getBody().getCarceles().stream().anyMatch(carcel -> carcel.equals(CARCEL_ID)));

	}

	@Test
	void testPenaSangreHombreAdulto() {
		
		PenaEntity penaEntity = new PenaEntity();
		penaEntity.setId(PENA_ID);
		penaEntity.setDias(1);
		penaEntity.setTipo(TipoPena.SANGRE);
		Mockito.when(penaRepository.findAllById(Mockito.any())).thenReturn(List.of(penaEntity));
		
		CarcelEntity carcelEntity = new CarcelEntity();
		carcelEntity.setId(CARCEL_ID);
		carcelEntity.setParaMenores(false);
		carcelEntity.setParaMujeres(false);
		carcelEntity.setTipoSeguridad(TipoSeguridadCarcel.MAXIMA);
		Mockito.when(carcelRepository.findAll(Mockito.any(Example.class))).thenReturn(List.of(carcelEntity));
		
		PenaCarcelesRequest entrada = new PenaCarcelesRequest();
		entrada.setEdad(20);
		entrada.setEsMujer(false);
		entrada.setPenas(List.of(PENA_ID));
		
		// launch test
		ResponseEntity<PenaCarcelesResponse> response = controlador.calcularPenaCarceles(entrada);
		
		// validate response
		Assert.assertTrue(response.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(200)));
		Assert.assertTrue(response.hasBody());
		Assert.assertNotNull(response.getBody().getCarceles());
		Assert.assertFalse(response.getBody().getCarceles().isEmpty());
		Assert.assertTrue(response.getBody().getCarceles().stream().anyMatch(carcel -> carcel.equals(CARCEL_ID)));

	}

	@Test
	void testPenaSangreHombreMenor() {
		
		PenaEntity penaEntity = new PenaEntity();
		penaEntity.setId(PENA_ID);
		penaEntity.setDias(1);
		penaEntity.setTipo(TipoPena.SANGRE);
		Mockito.when(penaRepository.findAllById(Mockito.any())).thenReturn(List.of(penaEntity));
		
		CarcelEntity carcelEntity = new CarcelEntity();
		carcelEntity.setId(CARCEL_ID);
		carcelEntity.setParaMenores(true);
		carcelEntity.setParaMujeres(false);
		carcelEntity.setTipoSeguridad(TipoSeguridadCarcel.MAXIMA);
		Mockito.when(carcelRepository.findAll(Mockito.any(Example.class))).thenReturn(List.of(carcelEntity));
		
		PenaCarcelesRequest entrada = new PenaCarcelesRequest();
		entrada.setEdad(2);
		entrada.setEsMujer(false);
		entrada.setPenas(List.of(PENA_ID));
		
		// launch test
		ResponseEntity<PenaCarcelesResponse> response = controlador.calcularPenaCarceles(entrada);
		
		// validate response
		Assert.assertTrue(response.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(200)));
		Assert.assertTrue(response.hasBody());
		Assert.assertNotNull(response.getBody().getCarceles());
		Assert.assertFalse(response.getBody().getCarceles().isEmpty());
		Assert.assertTrue(response.getBody().getCarceles().stream().anyMatch(carcel -> carcel.equals(CARCEL_ID)));

	}

	@Test
	void testSumaPenas() {
		
		int diasPena1 = 30;
		int diasPena2 = 10;
		PenaEntity penaEntity = new PenaEntity();
		penaEntity.setId(PENA_ID);
		penaEntity.setDias(diasPena1);
		penaEntity.setTipo(TipoPena.SANGRE);
		PenaEntity penaEntity2 = new PenaEntity();
		penaEntity2.setId(PENA_2_ID);
		penaEntity2.setDias(diasPena2);
		penaEntity2.setTipo(TipoPena.NO_VIOLENTA);
		Mockito.when(penaRepository.findAllById(Mockito.any())).thenReturn(List.of(penaEntity, penaEntity2));
		
		CarcelEntity carcelEntity = new CarcelEntity();
		carcelEntity.setId(CARCEL_ID);
		carcelEntity.setParaMenores(true);
		carcelEntity.setParaMujeres(false);
		carcelEntity.setTipoSeguridad(TipoSeguridadCarcel.MAXIMA);
		Mockito.when(carcelRepository.findAll(Mockito.any(Example.class))).thenReturn(List.of(carcelEntity));
		
		PenaCarcelesRequest entrada = new PenaCarcelesRequest();
		entrada.setEdad(2);
		entrada.setEsMujer(false);
		entrada.setPenas(List.of(PENA_ID, PENA_2_ID));
		
		// launch test
		ResponseEntity<PenaCarcelesResponse> response = controlador.calcularPenaCarceles(entrada);
		
		// validate response
		Assert.assertTrue(response.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(200)));
		Assert.assertTrue(response.hasBody());
		Assert.assertNotNull(response.getBody().getPenaTotal());
		Assert.assertEquals(diasPena1 + diasPena2, response.getBody().getPenaTotal().intValue());
		//Assert.assertTrue(response.getBody().getPenaTotal().intValue() ==  (diasPena1 + diasPena2));

	}

}
