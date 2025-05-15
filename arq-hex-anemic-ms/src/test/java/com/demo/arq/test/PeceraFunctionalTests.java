package com.demo.arq.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.demo.arq.Application;
import com.demo.arq.application.util.Errors;
import com.demo.arq.infrastructure.apirest.dto.request.PatchPeceraDto;
import com.demo.arq.infrastructure.apirest.dto.request.PostPutPeceraDto;
import com.demo.arq.infrastructure.apirest.dto.response.PeceraDto;
import com.demo.arq.infrastructure.database.jpa.entity.PeceraJpaEntity;
import com.demo.arq.infrastructure.database.jpa.repository.PeceraJpaRepository;
import com.demo.arq.infrastructure.database.mongodb.entity.PeceraEntity;
import com.demo.arq.infrastructure.database.mongodb.repository.PeceraRepository;
import com.demo.arq.infrastructure.event.dto.PeceraInputEventDto;
import com.demo.arq.infrastructure.event.producer.ProducerPeceraEvent;
import com.demo.arq.test.kafka.ConsumerDeletedPeceraEventTestService;
import com.demo.arq.test.kafka.ConsumerModifiedPeceraEventTestService;
import com.demo.arq.test.kafka.ConsumerPostedPeceraEventTestService;
import com.demo.arq.test.mapper.PeceraTestMapper;
import com.demo.arq.test.util.TestUtils;
import com.fasterxml.jackson.core.JsonProcessingException;

@SpringBootTest
@AutoConfigureMockMvc
@ComponentScan(basePackages = { "com.demo.arq" })
@ContextConfiguration(classes = { Application.class, AnnotationConfigContextLoader.class })
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9095", "port=9095" })
class PeceraFunctionalTests {

	private static final String PECERA_ENDPOINT = "/peceras";

	@Value("${custom.topic.pecera.input-event}")
	private String topicInputEvent;

	@Autowired
	MockMvc mockMvc;

	@Autowired
	PeceraRepository peceraRepository;

	@Autowired
	PeceraJpaRepository peceraJpaRepository;

	@Autowired
	PeceraTestMapper peceraTestMapper;

	@Autowired
	ProducerPeceraEvent producerPeceraEvent;

	@Autowired
	ConsumerPostedPeceraEventTestService consumerPostedPeceraEventTestService;

	@Autowired
	ConsumerModifiedPeceraEventTestService consumerModifiedPeceraEventTestService;

	@Autowired
	ConsumerDeletedPeceraEventTestService consumerDeletedPeceraEventTestService;

	@BeforeEach
	public void beforeEach() throws IOException {
		// Antes de cada test, vaciamos toda la BBDD y la rellenamos de nuevo
		peceraRepository.deleteAll();
		peceraJpaRepository.deleteAll();

		List<PeceraJpaEntity> listaJpaParaGuardar = TestUtils.createObjects(PeceraJpaEntity.class, 10);
		// Si no nos aseguramos de que esten no eliminados puede que no nos cree ninguno
		// valido
		listaJpaParaGuardar.forEach(p -> {
			BigDecimal id = peceraJpaRepository.getNextValSequence();
			p.setId(id);
			p.setEliminado(false);

			PeceraEntity entidadMongo = TestUtils.createObject(PeceraEntity.class);
			entidadMongo.setEliminado(false);
			entidadMongo.setIdJpa(id);
			peceraRepository.save(entidadMongo);
		});
		peceraJpaRepository.saveAll(listaJpaParaGuardar);

		// Reseteamos los contadores de mensajes de los consumidores Kafka
		consumerDeletedPeceraEventTestService.resetLatch();
		consumerPostedPeceraEventTestService.resetLatch();
		consumerModifiedPeceraEventTestService.resetLatch();
	}

	@Test
	void testGetPecerasOk() throws Exception {
		// Aunque llamemos sin paginacion, Spring nos crea una paginacion por defecto de
		// 20 elementos

		// Recuperamos todos los datos que haya en BBDD con la paginacion por defecto
		Page<PeceraEntity> datosGuardados = peceraRepository.findByEliminado(false, Pageable.ofSize(20));

		// Lo pasamos a la salida Dto esperada
		Page<PeceraDto> salida = peceraTestMapper.fromEntityToDto(datosGuardados);

		mockMvc.perform(MockMvcRequestBuilders
				// Elegimos el verbo http que usaremos en la llamada
				.get(PECERA_ENDPOINT)
				// Añadimos las cabeceras necesarias
				.accept(MediaType.APPLICATION_JSON))
				// Validamos el codigo de respuesta
				.andExpect(MockMvcResultMatchers.status().isOk())

				// Validamos que la salida es la misma que lo guardado
				.andExpect(MockMvcResultMatchers.content().json(TestUtils.writeAsString(salida)))

				// Otra forma de validar la salida:
				.andExpect(MockMvcResultMatchers.content().string(CoreMatchers.allOf(
						CoreMatchers.containsString("\"id\":\"" + datosGuardados.getContent().get(0).getId() + "\""),
						CoreMatchers.containsString("\"id\":\"" + datosGuardados.getContent().get(1).getId() + "\""))))
				// Pintamos la salida en la consola
				.andDo(MockMvcResultHandlers.print());
	}

	@Test
	void testGetPecerasNoContent() throws Exception {
		peceraRepository.deleteAll();
		peceraJpaRepository.deleteAll();

		mockMvc.perform(MockMvcRequestBuilders
				// Elegimos el verbo http que usaremos en la llamada
				.get(PECERA_ENDPOINT)
				// Añadimos las cabeceras necesarias
				.accept(MediaType.APPLICATION_JSON))
				// Validamos el codigo de respuesta
				.andExpect(MockMvcResultMatchers.status().isNoContent())
				// Pintamos la salida en la consola
				.andDo(MockMvcResultHandlers.print());
	}

	@Test
	void testGetPecerasMaxPaginationExceeded() throws Exception {
		// Usamos los Query Params del Pageable con una paginacion que supera el maximo
		// de 100 que pusimos
		String paginacionExcesiva = "?size=200";

		mockMvc.perform(MockMvcRequestBuilders
				// Elegimos el verbo http que usaremos en la llamada
				.get(PECERA_ENDPOINT + paginacionExcesiva)
				// Añadimos las cabeceras necesarias
				.accept(MediaType.APPLICATION_JSON))
				// Validamos el codigo de respuesta
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				// Validamos que nos devuelve el error de paginacion
				.andExpect(MockMvcResultMatchers.content().string(Errors.MAXIMUM_PAGINATION_EXCEEDED))
				// Pintamos la salida en la consola
				.andDo(MockMvcResultHandlers.print());
	}

	@Test
	void testGetPeceraOk() throws Exception {
		// Recuperamos todos los datos que haya en BBDD
		Page<PeceraEntity> listaDatosGuardados = peceraRepository.findByEliminado(false, Pageable.ofSize(20));

		// Guardamos un id existente
		String id = listaDatosGuardados.getContent().get(0).getId();

		// Lo pasamos a la salida de Dto esperada
		PeceraDto salida = peceraTestMapper.fromEntityToDto(listaDatosGuardados.getContent().get(0));

		mockMvc.perform(MockMvcRequestBuilders
				// Elegimos el verbo http que usaremos en la llamada
				.get(PECERA_ENDPOINT + "/" + id)
				// Añadimos las cabeceras necesarias
				.accept(MediaType.APPLICATION_JSON))
				// Validamos el codigo de respuesta
				.andExpect(MockMvcResultMatchers.status().isOk())
				// Validamos la respuesta
				.andExpect(MockMvcResultMatchers.content().json(TestUtils.writeAsString(salida)))
				// Pintamos la salida en la consola
				.andDo(MockMvcResultHandlers.print());
	}

	@Test
	void testGetPeceraKoNoContent() throws Exception {
		// Nos aseguramos que no hay elementos que encontrar
		peceraRepository.deleteAll();
		peceraJpaRepository.deleteAll();

		// Buscamos por un id que no existe
		String id = "noExiste";

		mockMvc.perform(MockMvcRequestBuilders
				// Elegimos el verbo http que usaremos en la llamada
				.get(PECERA_ENDPOINT + "/" + id)
				// Añadimos las cabeceras necesarias
				.accept(MediaType.APPLICATION_JSON))
				// Validamos el codigo de respuesta
				.andExpect(MockMvcResultMatchers.status().isNoContent())
				// Pintamos la salida en la consola
				.andDo(MockMvcResultHandlers.print());
	}

	@Test
	void testPostPeceraOk() throws Exception {
		// Si queremos leer mas mensajes
		// kafkaConsumerPostedPeceraEventServiceTest.setLatch(new CountDownLatch(5));

		// Nos aseguramos que no hay elementos que encontrar
		peceraRepository.deleteAll();
		peceraJpaRepository.deleteAll();

		// Creamos un comando de entrada random
		PostPutPeceraDto objetoDto = TestUtils.createObject(PostPutPeceraDto.class);

		mockMvc.perform(MockMvcRequestBuilders
				// Elegimos el verbo http que usaremos en la llamada
				.post(PECERA_ENDPOINT)
				// Añadimos el Body a la peticion
				.content(TestUtils.writeAsString(objetoDto))
				// Añadimos las cabeceras necesarias
				.contentType(MediaType.APPLICATION_JSON)
				// Añadimos mas cabeceras necesarias
				.accept(MediaType.APPLICATION_JSON))
				// Validamos el codigo de respuesta
				.andExpect(MockMvcResultMatchers.status().isCreated())
				// Validamos la respuesta de la cabecera
				.andExpect(MockMvcResultMatchers.header().string("Location",
						CoreMatchers.containsString("http://localhost/peceras/")))
				// Pintamos la salida en la consola
				.andDo(MockMvcResultHandlers.print());

		// Validamos que el objeto en BBDD se ha creado correctamente
		Page<PeceraEntity> objetosAlmacenados = peceraRepository.findByEliminado(false, Pageable.ofSize(20));
		assertEquals(1, objetosAlmacenados.getNumberOfElements());
		assertEquals(objetoDto.getValue(), objetosAlmacenados.getContent().get(0).getValue());

		// Validamos que el objeto en BBDD se ha creado correctamente en Jpa
		Page<PeceraJpaEntity> objetosJpaAlmacenados = peceraJpaRepository.findByEliminado(false, Pageable.ofSize(20));
		assertEquals(1, objetosJpaAlmacenados.getNumberOfElements());
		assertEquals(objetoDto.getValue(), objetosJpaAlmacenados.getContent().get(0).getValue());

		// Validamos que el objeto se ha enviado por Evento correctamente
		boolean consumido = consumerPostedPeceraEventTestService.getLatch().await(5, TimeUnit.SECONDS);
		assertTrue(consumido);
		assertNotNull(consumerPostedPeceraEventTestService.getPayload());
		assertEquals(objetoDto.getValue(), consumerPostedPeceraEventTestService.getPayload().getValue());
	}

	@Test
	void testPatchPeceraOk() throws Exception {
		// Recuperamos todos los datos que haya en BBDD
		Page<PeceraEntity> listaDatosGuardados = peceraRepository.findByEliminado(false, Pageable.ofSize(20));

		String id = listaDatosGuardados.getContent().get(0).getId();
		BigDecimal idJpa = listaDatosGuardados.getContent().get(0).getIdJpa();

		// Lo pasamos a la salida de Dto que vamos a poner de entrada
		PatchPeceraDto objetoDto = peceraTestMapper.fromEntityToPatchDto(listaDatosGuardados.getContent().get(0));

		// Modificamos algun valor del objeto para la modificacion
		objetoDto.setValue("Nuevo Valor");

		mockMvc.perform(MockMvcRequestBuilders
				// Elegimos el verbo http que usaremos en la llamada
				.patch(PECERA_ENDPOINT + "/" + id)
				// Añadimos el Body a la peticion
				.content(TestUtils.writeAsString(objetoDto))
				// Añadimos las cabeceras necesarias
				.contentType(MediaType.APPLICATION_JSON)
				// Añadimos mas cabeceras necesarias
				.accept(MediaType.APPLICATION_JSON))
				// Validamos el codigo de respuesta
				.andExpect(MockMvcResultMatchers.status().isNoContent())
				// Pintamos la salida en la consola
				.andDo(MockMvcResultHandlers.print());

		// Validamos que el objeto en BBDD se ha modificado correctamente
		Optional<PeceraEntity> objetoActualizado = peceraRepository.findByIdAndEliminado(id, false);
		assertTrue(objetoActualizado.isPresent());
		assertEquals(objetoDto.getValue(), objetoActualizado.get().getValue());
		assertEquals(objetoDto.getValueObject().getValue(), objetoActualizado.get().getValueObject().getValue());

		// Validamos que el objeto en BBDD se ha modificado correctamente
		Optional<PeceraJpaEntity> objetoJpaActualizado = peceraJpaRepository.findByIdAndEliminado(idJpa, false);
		assertTrue(objetoJpaActualizado.isPresent());
		assertEquals(objetoDto.getValue(), objetoJpaActualizado.get().getValue());
		assertEquals(objetoDto.getValueObject().getValue(), objetoJpaActualizado.get().getValueObjectValue());

		// Validamos que el objeto se ha enviado por Evento correctamente en Jpa
		boolean consumido = consumerModifiedPeceraEventTestService.getLatch().await(5, TimeUnit.SECONDS);
		assertTrue(consumido);
		assertNotNull(consumerModifiedPeceraEventTestService.getPayload());
		assertEquals(objetoDto.getValue(), consumerModifiedPeceraEventTestService.getPayload().getValue());
	}

	@Test
	void testPatchPeceraKoNotFound() throws Exception {

		// Nos aseguramos que no hay elementos que actualizar
		peceraRepository.deleteAll();
		peceraJpaRepository.deleteAll();

		// Buscamos por un id que no existe
		String idNoExiste = "NoExiste";
		BigDecimal idNoExisteJpa = new BigDecimal(0);

		// Creamos un comando de entrada random
		PatchPeceraDto objetoDto = TestUtils.createObject(PatchPeceraDto.class);

		mockMvc.perform(MockMvcRequestBuilders
				// Elegimos el verbo http que usaremos en la llamada
				.patch(PECERA_ENDPOINT + "/" + idNoExiste)
				// Añadimos el Body a la peticion
				.content(TestUtils.writeAsString(objetoDto))
				// Añadimos las cabeceras necesarias
				.contentType(MediaType.APPLICATION_JSON)
				// Añadimos mas cabeceras necesarias
				.accept(MediaType.APPLICATION_JSON))
				// Validamos el codigo de respuesta
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				// Validamos la respuesta
				.andExpect(MockMvcResultMatchers.content().string(Errors.PECERA_NOT_FOUND))
				// Pintamos la salida en la consola
				.andDo(MockMvcResultHandlers.print());

		// Validamos que el objeto en BBDD NO se ha modificado
		Optional<PeceraEntity> objetoActualizado = peceraRepository.findByIdAndEliminado(idNoExiste, false);
		assertTrue(objetoActualizado.isEmpty());

		// Validamos que el objeto en BBDD NO se ha modificado en Jpa
		Optional<PeceraJpaEntity> objetoJpaActualizado = peceraJpaRepository.findByIdAndEliminado(idNoExisteJpa, false);
		assertTrue(objetoJpaActualizado.isEmpty());

		// Validamos que el objeto NO se ha enviado por Evento
		boolean consumido = consumerModifiedPeceraEventTestService.getLatch().await(5, TimeUnit.SECONDS);
		assertFalse(consumido);
	}

	@Test
	void testPutPeceraOk() throws Exception {
		// Recuperamos todos los datos que haya en BBDD
		Page<PeceraEntity> listaDatosGuardados = peceraRepository.findByEliminado(false, Pageable.ofSize(20));

		// Guardamos un id existente
		String id = listaDatosGuardados.getContent().get(0).getId();
		BigDecimal idJpa = listaDatosGuardados.getContent().get(0).getIdJpa();

		// Creamos el objeto de entrada
		PostPutPeceraDto objetoDto = PostPutPeceraDto.builder()
				.value(listaDatosGuardados.getContent().get(0).getValue()).valueObject(null).build();

		mockMvc.perform(MockMvcRequestBuilders
				// Elegimos el verbo http que usaremos en la llamada
				.put(PECERA_ENDPOINT + "/" + id)
				// Añadimos el Body a la peticion
				.content(TestUtils.writeAsString(objetoDto))
				// Añadimos las cabeceras necesarias
				.contentType(MediaType.APPLICATION_JSON)
				// Añadimos mas cabeceras necesarias
				.accept(MediaType.APPLICATION_JSON))
				// Validamos el codigo de respuesta
				.andExpect(MockMvcResultMatchers.status().isNoContent())
				// Pintamos la salida en la consola
				.andDo(MockMvcResultHandlers.print());

		// Validamos que el objeto en BBDD se ha modificado correctamente
		Optional<PeceraEntity> objetoActualizado = peceraRepository.findByIdAndEliminado(id, false);
		assertTrue(objetoActualizado.isPresent());
		assertNull(objetoActualizado.get().getValueObject());

		// Validamos que el objeto en BBDD se ha modificado correctamente
		Optional<PeceraJpaEntity> objetoJpaActualizado = peceraJpaRepository.findByIdAndEliminado(idJpa, false);
		assertTrue(objetoJpaActualizado.isPresent());
		assertNull(objetoJpaActualizado.get().getValueObjectValue());

		// Validamos que el objeto se ha enviado por Evento correctamente
		boolean consumido = consumerModifiedPeceraEventTestService.getLatch().await(5, TimeUnit.SECONDS);
		assertTrue(consumido);
		assertNotNull(consumerModifiedPeceraEventTestService.getPayload());
		assertNull(consumerModifiedPeceraEventTestService.getPayload().getValueObject());
	}

	@Test
	void testPutPeceraKoNotFound() throws Exception {
		// Nos aseguramos que no hay elementos que actualizar
		peceraRepository.deleteAll();
		peceraJpaRepository.deleteAll();

		// Buscamos por un id que no existe
		String idNoExiste = "NoExiste";
		BigDecimal idNoExisteJpa = new BigDecimal(0);
		// Creamos un Comando random
		PostPutPeceraDto objetoDto = TestUtils.createObject(PostPutPeceraDto.class);

		mockMvc.perform(MockMvcRequestBuilders
				// Elegimos el verbo http que usaremos en la llamada
				.put(PECERA_ENDPOINT + "/" + idNoExiste)
				// Añadimos el Body a la peticion
				.content(TestUtils.writeAsString(objetoDto))
				// Añadimos las cabeceras necesarias
				.contentType(MediaType.APPLICATION_JSON)
				// Añadimos mas cabeceras necesarias
				.accept(MediaType.APPLICATION_JSON))
				// Validamos el codigo de respuesta
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				// Validamos la respuesta
				.andExpect(MockMvcResultMatchers.content().string(Errors.PECERA_NOT_FOUND))
				// Pintamos la salida en la consola
				.andDo(MockMvcResultHandlers.print());

		// Validamos que el objeto en BBDD NO se ha modificado
		Optional<PeceraEntity> objetoActualizado = peceraRepository.findByIdAndEliminado(idNoExiste, false);
		assertTrue(objetoActualizado.isEmpty());

		// Validamos que el objeto en BBDD NO se ha modificado en Jpa
		Optional<PeceraJpaEntity> objetoJpaActualizado = peceraJpaRepository.findByIdAndEliminado(idNoExisteJpa, false);
		assertTrue(objetoJpaActualizado.isEmpty());

		// Validamos que el objeto NO se ha enviado por Evento
		boolean consumido = consumerModifiedPeceraEventTestService.getLatch().await(5, TimeUnit.SECONDS);
		assertFalse(consumido);
	}

	@Test
	void testDeletePeceraOk() throws Exception {
		// Buscamos todos los elementos guardados en BBDD
		Page<PeceraEntity> listaDatosGuardados = peceraRepository.findByEliminado(false, Pageable.ofSize(20));

		// Guardamos un id existente
		String id = listaDatosGuardados.getContent().get(0).getId();
		BigDecimal idJpa = listaDatosGuardados.getContent().get(0).getIdJpa();

		mockMvc.perform(MockMvcRequestBuilders
				// Elegimos el verbo http que usaremos en la llamada
				.delete(PECERA_ENDPOINT + "/" + id)
				// Añadimos las cabeceras necesarias
				.accept(MediaType.APPLICATION_JSON))
				// Validamos el codigo de respuesta
				.andExpect(MockMvcResultMatchers.status().isNoContent())
				// Pintamos la salida en la consola
				.andDo(MockMvcResultHandlers.print());

		// Validamos que el objeto en BBDD se ha eliminado correctamente
		Optional<PeceraEntity> objetoActualizado = peceraRepository.findByIdAndEliminado(id, false);
		assertTrue(objetoActualizado.isEmpty());

		// Validamos que el objeto en BBDD se ha eliminado correctamente en Jpa
		Optional<PeceraJpaEntity> objetoJpaActualizado = peceraJpaRepository.findByIdAndEliminado(idJpa, false);
		assertTrue(objetoJpaActualizado.isEmpty());

		// Validamos que el objeto se ha enviado por Evento correctamente
		boolean consumido = consumerDeletedPeceraEventTestService.getLatch().await(5, TimeUnit.SECONDS);
		assertTrue(consumido);
		assertNotNull(consumerDeletedPeceraEventTestService.getPayload());
		assertEquals(id, consumerDeletedPeceraEventTestService.getPayload().getId());
	}

	@Test
	void testDeletePeceraKoNotFound() throws Exception {
		// Eliminamos todos los elementos en BBDD
		peceraRepository.deleteAll();
		peceraJpaRepository.deleteAll();

		String id = "NoExiste";

		mockMvc.perform(MockMvcRequestBuilders
				// Elegimos el verbo http que usaremos en la llamada
				.delete(PECERA_ENDPOINT + "/" + id)
				// Añadimos las cabeceras necesarias
				.accept(MediaType.APPLICATION_JSON))
				// Validamos el codigo de respuesta
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				// Pintamos la salida en la consola
				.andDo(MockMvcResultHandlers.print());

		// Validamos que no se haya enviado el evento de eliminacion de Kafka
		boolean consumido = consumerDeletedPeceraEventTestService.getLatch().await(5, TimeUnit.SECONDS);
		assertFalse(consumido);
	}

	@Test
	void testInputEvent() throws JsonProcessingException, InterruptedException {
		// Buscamos todos los elementos guardados en BBDD
		Page<PeceraEntity> listaDatosGuardados = peceraRepository.findByEliminado(false, Pageable.ofSize(20));

		// Podemos hacerlo con Builder, crear mappers o como lo haga el Servicio de
		// Dominio
		PeceraInputEventDto objetoEvento = PeceraInputEventDto.builder()
				.id(listaDatosGuardados.getContent().get(0).getId()).value("NuevoValor").build();

		producerPeceraEvent.sendMessageAsynch(topicInputEvent, objetoEvento);

		// Damos algo de tiempo para:
		// - La creacion asincrona del dato en BBDD
		// - El lanzamiento asincrono del nuevo evento
		TestUtils.waitMilliseconds(4000l);

		// Validamos que el objeto en BBDD se ha creado correctamente
		Optional<PeceraEntity> objetoActualizado = peceraRepository.findByIdAndEliminado(objetoEvento.getId(), false);
		assertTrue(objetoActualizado.isPresent());
		assertEquals(objetoEvento.getValue(), objetoActualizado.get().getValue());
	}

}
