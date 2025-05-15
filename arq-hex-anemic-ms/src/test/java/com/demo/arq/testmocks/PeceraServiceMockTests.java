package com.demo.arq.testmocks;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.demo.arq.application.port.output.PeceraJpaRepositoryOutputPort;
import com.demo.arq.application.port.output.PeceraProducerOutputPort;
import com.demo.arq.application.port.output.PeceraRepositoryOutputPort;
import com.demo.arq.application.service.PeceraService;
import com.demo.arq.domain.exception.BusinessException;
import com.demo.arq.domain.mapper.PeceraPatchMapper;
import com.demo.arq.domain.mapper.PeceraPatchMapperImpl;
import com.demo.arq.domain.model.Pecera;
import com.demo.arq.infrastructure.database.jpa.service.PeceraJpaRepositoryService;
import com.demo.arq.infrastructure.database.mongodb.service.PeceraRepositoryService;
import com.demo.arq.infrastructure.event.service.PeceraProducerService;
import com.demo.arq.test.util.TestUtils;

@ExtendWith(MockitoExtension.class)
class PeceraServiceMockTests {

	// Aqui no funcionan los autowired, porque no estamos activando Spring

	@InjectMocks
	PeceraService peceraServiceInputPort = new PeceraService();

	@Mock
	PeceraRepositoryOutputPort peceraRepositoryOutputPort = new PeceraRepositoryService();

	@Mock
	PeceraJpaRepositoryOutputPort peceraJoaRepositoryOutputPort = new PeceraJpaRepositoryService();

	@Mock
	PeceraProducerOutputPort peceraProducerOutputPort = new PeceraProducerService();

	@Mock
	PeceraPatchMapper peceraPatchMapper = new PeceraPatchMapperImpl();

	@Test
	void testObtenerPeceras() throws BusinessException {
		// Preparamos los Datos
		Pageable pageable = PageRequest.of(0, 10);
		List<Pecera> elementoEnBbdd = TestUtils.createObjects(Pecera.class, 10);
		Page<Pecera> paginaRespuesta = new PageImpl<>(elementoEnBbdd, pageable, elementoEnBbdd.size());
		Mockito.when(peceraRepositoryOutputPort.obtenerPeceras(pageable)).thenReturn(paginaRespuesta);

		// Testeo del metodo
		Page<Pecera> salida = peceraServiceInputPort.obtenerPeceras(pageable);

		// Validamos los Datos
		Assertions.assertNotNull(salida);
		Assertions.assertEquals(paginaRespuesta, salida);
		Mockito.verify(peceraRepositoryOutputPort, Mockito.times(1)).obtenerPeceras(Mockito.any());
	}

	@Test
	void testObtenerPecerasWithMaximumPaginationError() throws BusinessException {
		Assertions.assertThrows(BusinessException.class,
				() -> peceraServiceInputPort.obtenerPeceras(PageRequest.of(0, 101)));

		Mockito.verify(peceraRepositoryOutputPort, Mockito.times(0)).obtenerPeceras(Mockito.any());
	}

	@Test
	void testObtenerPecera() throws BusinessException {
		// Preparamos los Datos
		Optional<Pecera> peceraRespuesta = Optional.of(TestUtils.createObject(Pecera.class));
		Mockito.when(peceraRepositoryOutputPort.obtenerPecera(Mockito.any())).thenReturn(peceraRespuesta);

		// Testeo del metodo
		Optional<Pecera> salida = peceraServiceInputPort.obtenerPecera("loquequeramos");

		// Validamos los Datos
		Assertions.assertTrue(salida.isPresent());
		Assertions.assertEquals(peceraRespuesta.get(), salida.get());
		Mockito.verify(peceraRepositoryOutputPort, Mockito.times(1)).obtenerPecera(Mockito.any());
	}

	@Test
	void testCrearPecera() throws BusinessException {
		// Preparamos los Datos
		String idPecera = "idPecera";
		Mockito.when(peceraRepositoryOutputPort.crearPecera(Mockito.any())).thenReturn(idPecera);

		// Testeo del metodo
		String salida = peceraServiceInputPort.crearPecera(TestUtils.createObject(Pecera.class));

		// Validamos los Datos
		Assertions.assertNotNull(salida);
		Assertions.assertEquals(idPecera, salida);
		Mockito.verify(peceraRepositoryOutputPort, Mockito.times(1)).crearPecera(Mockito.any());
		Mockito.verify(peceraProducerOutputPort, Mockito.times(1)).eventoCreacionPecera(Mockito.any());
	}

	@Test
	void testModificacionParcialPecera() throws BusinessException {
		// Preparamos los Datos
		Pecera pecera = TestUtils.createObject(Pecera.class);
		Mockito.when(peceraRepositoryOutputPort.obtenerPecera(Mockito.any())).thenReturn(Optional.of(pecera));

		// Testeo del metodo
		peceraServiceInputPort.modificacionParcialPecera(pecera);

		// Validamos los Datos
		Mockito.verify(peceraPatchMapper, Mockito.times(1)).update(Mockito.any(Pecera.class),
				Mockito.any(Pecera.class));
		Mockito.verify(peceraRepositoryOutputPort, Mockito.times(1)).obtenerPecera(Mockito.any());
		Mockito.verify(peceraRepositoryOutputPort, Mockito.times(1)).modificarPecera(Mockito.any());
		Mockito.verify(peceraProducerOutputPort, Mockito.times(1)).eventoModificacionPecera(pecera);
	}

	@Test
	void testModificacionParcialPeceraWithErrorPeceraNotFound() throws BusinessException {
		Mockito.when(peceraRepositoryOutputPort.obtenerPecera(Mockito.any())).thenReturn(Optional.ofNullable(null));

		Pecera peceraSalida = TestUtils.createObject(Pecera.class);
		Assertions.assertThrows(BusinessException.class,
				() -> peceraServiceInputPort.modificacionParcialPecera(peceraSalida));

		// Validamos los Datos
		Mockito.verify(peceraRepositoryOutputPort, Mockito.times(1)).obtenerPecera(Mockito.any());
		Mockito.verify(peceraPatchMapper, Mockito.times(0)).update(Mockito.any(Pecera.class),
				Mockito.any(Pecera.class));
		Mockito.verify(peceraRepositoryOutputPort, Mockito.times(0)).modificarPecera(Mockito.any());
		Mockito.verify(peceraProducerOutputPort, Mockito.times(0)).eventoModificacionPecera(Mockito.any());
	}

	@Test
	void testModificacionTotalPecera() throws BusinessException {
		// Preparamos los Datos
		Pecera pecera = TestUtils.createObject(Pecera.class);
		Mockito.when(peceraRepositoryOutputPort.obtenerPecera(Mockito.any())).thenReturn(Optional.of(pecera));

		// Testeo del metodo
		peceraServiceInputPort.modificacionTotalPecera(pecera);

		// Validamos los Datos
		Mockito.verify(peceraRepositoryOutputPort, Mockito.times(1)).obtenerPecera(Mockito.any());
		Mockito.verify(peceraRepositoryOutputPort, Mockito.times(1)).modificarPecera(Mockito.any());
		Mockito.verify(peceraProducerOutputPort, Mockito.times(1)).eventoModificacionPecera(pecera);
	}

	@Test
	void testModificacionTotalPeceraWithErrorPeceraNotFound() throws BusinessException {
		Mockito.when(peceraRepositoryOutputPort.obtenerPecera(Mockito.any())).thenReturn(Optional.ofNullable(null));

		Pecera peceraSalida = TestUtils.createObject(Pecera.class);
		Assertions.assertThrows(BusinessException.class,
				() -> peceraServiceInputPort.modificacionTotalPecera(peceraSalida));

		// Validamos los Datos
		Mockito.verify(peceraRepositoryOutputPort, Mockito.times(1)).obtenerPecera(Mockito.any());
		Mockito.verify(peceraRepositoryOutputPort, Mockito.times(0)).modificarPecera(Mockito.any());
		Mockito.verify(peceraProducerOutputPort, Mockito.times(0)).eventoModificacionPecera(Mockito.any());
	}

	@Test
	void testEliminacionPecera() throws BusinessException {
		// Preparamos los Datos
		Pecera pecera = TestUtils.createObject(Pecera.class);
		Mockito.when(peceraRepositoryOutputPort.obtenerPecera(Mockito.any())).thenReturn(Optional.of(pecera));

		// Testeo del metodo
		peceraServiceInputPort.eliminarPecera(pecera.getId());

		// Validamos los Datos
		Mockito.verify(peceraRepositoryOutputPort, Mockito.times(1)).obtenerPecera(Mockito.any());
		Mockito.verify(peceraRepositoryOutputPort, Mockito.times(1)).eliminarPecera(Mockito.any());
		Mockito.verify(peceraProducerOutputPort, Mockito.times(1)).eventoEliminacionPecera(pecera);
	}

	@Test
	void testEliminacionPeceraWithErrorPeceraNotFound() throws BusinessException {
		Mockito.when(peceraRepositoryOutputPort.obtenerPecera(Mockito.any())).thenReturn(Optional.ofNullable(null));

		Pecera peceraSalida = TestUtils.createObject(Pecera.class);
		Assertions.assertThrows(BusinessException.class,
				() -> peceraServiceInputPort.eliminarPecera(peceraSalida.getId()));

		// Validamos los Datos
		Mockito.verify(peceraRepositoryOutputPort, Mockito.times(1)).obtenerPecera(Mockito.any());
		Mockito.verify(peceraRepositoryOutputPort, Mockito.times(0)).eliminarPecera(Mockito.any());
		Mockito.verify(peceraProducerOutputPort, Mockito.times(0)).eventoEliminacionPecera(Mockito.any());
	}
}
