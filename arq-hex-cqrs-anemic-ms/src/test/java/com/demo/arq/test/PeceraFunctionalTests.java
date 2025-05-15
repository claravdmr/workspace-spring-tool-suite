package com.demo.arq.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.demo.arq.Application;
import com.demo.arq.application.util.Errors;
import com.demo.arq.domain.model.Pecera;
import com.demo.arq.domain.model.ValueObject;
import com.demo.arq.infrastructure.apirest.dto.common.ValueObjectDto;
import com.demo.arq.infrastructure.apirest.dto.request.PatchPeceraDto;
import com.demo.arq.infrastructure.apirest.dto.request.PostPutPeceraDto;
import com.demo.arq.infrastructure.apirest.dto.response.PeceraDto;
import com.demo.arq.infrastructure.apirest.mapper.PeceraToPeceraDtoMapper;
import com.demo.arq.infrastructure.apirest.mapper.ValueObjectToValueObjectDtoMapper;
import com.demo.arq.infrastructure.database.entity.PeceraEntity;
import com.demo.arq.infrastructure.database.mapper.PeceraToPeceraEntityMapper;
import com.demo.arq.infrastructure.database.mapper.ValueObjectToValueObjectEntityMapper;
import com.demo.arq.infrastructure.database.repository.PeceraRepository;
import com.demo.arq.infrastructure.event.dto.PeceraEventDto;
import com.demo.arq.infrastructure.event.dto.PeceraInputEventDto;
import com.demo.arq.infrastructure.event.producer.ProducerPeceraEvent;
import com.demo.arq.test.kafka.ConsumerDeletedPeceraEventTestService;
import com.demo.arq.test.kafka.ConsumerPatchedPeceraEventTestService;
import com.demo.arq.test.kafka.ConsumerPostedPeceraEventTestService;
import com.demo.arq.test.kafka.ConsumerPutPeceraEventTestService;
import com.demo.arq.test.util.TestUtils;
import com.fasterxml.jackson.core.JsonProcessingException;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureDataJpa
@AutoConfigureDataMongo
@ContextConfiguration(classes = { Application.class, AnnotationConfigContextLoader.class })
@DirtiesContext
@Execution(ExecutionMode.CONCURRENT)
@ComponentScan(basePackages = { "com.demo.arq" })
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9095", "port=9095" })
class PeceraFunctionalTests {

	private static final String PECERA_ENDPOINT = "/peceras";

	@Autowired
	MockMvc mockMvc;

	@Autowired
	PeceraRepository peceraRepository;

	@Autowired
	PeceraToPeceraEntityMapper peceraToPeceraEntityMapper;

	@Autowired
	ValueObjectToValueObjectEntityMapper valueObjectToValueObjectEntityMapper;

	@Autowired
	ValueObjectToValueObjectDtoMapper valueObjectToValueObjectDtoMapper;
	
	@Autowired
	PeceraToPeceraDtoMapper peceraToPeceraDtoMapper;

	@Autowired
	ConsumerPostedPeceraEventTestService consumerPostedPeceraEventTestService;

	@Autowired
	ConsumerPatchedPeceraEventTestService consumerPatchedPeceraEventTestService;

	@Autowired
	ConsumerPutPeceraEventTestService consumerPutPeceraEventTestService;

	@Autowired
	ConsumerDeletedPeceraEventTestService consumerDeletedPeceraEventTestService;

	@Autowired
	ProducerPeceraEvent producerPeceraEvent;

	@Value("${custom.topic.pecera.input-event}")
	private String topicInputEvent;

	@AfterAll
	public static void afterAll() throws IOException {
	}

	@AfterEach
	void afterEach() {
	}

	@BeforeAll
	public static void beforeAll() throws IOException {
	}

	@BeforeEach
	public void beforeEach() throws IOException {

		// Antes de cada test, vaciamos toda la BBDD y la rellenamos de nuevo para que
		// este limpia
		peceraRepository.deleteAll();
		List<PeceraEntity> listaDatosGuardados = TestUtils.createObjects(PeceraEntity.class, 2);
		peceraRepository.saveAll(listaDatosGuardados);

		// Reseteamos los contadores de mensajes de los consumidores Kafka
		consumerDeletedPeceraEventTestService.resetLatch();
		consumerPostedPeceraEventTestService.resetLatch();
		consumerPutPeceraEventTestService.resetLatch();
		consumerPatchedPeceraEventTestService.resetLatch();
	}

	@Test
	void testGetPecerasOk() throws Exception {
		// Aunque llamemos sin paginacion, Spring nos crea una paginacion por defecto de
		// 20 elementos

		// Recuperamos todos los datos que haya en BBDD con la paginacion por defecto
		Page<PeceraEntity> listaDatosGuardados = peceraRepository.findAll(Pageable.ofSize(20));

		// Lo pasamos a la salida de dominio
		Page<Pecera> listaDominio = peceraToPeceraEntityMapper.fromOutputToInput(listaDatosGuardados);

		// Lo pasamos a la salida del Controlador
		Page<PeceraDto> salida = peceraToPeceraDtoMapper.fromInputToOutput(listaDominio);

		mockMvc.perform(MockMvcRequestBuilders.get(PECERA_ENDPOINT).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				// Validamos que la salida es la misma que lo guardado
				.andExpect(MockMvcResultMatchers.content().json(TestUtils.writeAsString(salida)))
				// Otra forma de validar la salida:
//				.andExpect(MockMvcResultMatchers.content()
//						.string(CoreMatchers.allOf(
//								CoreMatchers.containsString("\"id\":\"" + listaDatosGuardados.getContent().get(0).getId() + "\""),
//								CoreMatchers.containsString("\"id\":\"" + listaDatosGuardados.getContent().get(1).getId() + "\""))))
				.andDo(MockMvcResultHandlers.print());
	}

	@Test
	void testGetPecerasFail() throws Exception {
		// Usamos los Query Params del Pageable con una paginacion que supera el maximo
		// de 100 que pusimos
		String paginacionExcesiva = "?size=200";

		mockMvc.perform(
				MockMvcRequestBuilders.get(PECERA_ENDPOINT + paginacionExcesiva).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				// Validamos que nos devuelve una excepcion funcional
				.andExpect(MockMvcResultMatchers.content().string(Errors.MAXIMUM_PAGINATION_EXCEEDED))
				.andDo(MockMvcResultHandlers.print());
	}

	@Test
	void testGetPeceraOk() throws Exception {
		// Recuperamos todos los datos que haya en BBDD
		List<PeceraEntity> listaDatosGuardados = peceraRepository.findAll();

		// Guardamos un id existente
		String id = listaDatosGuardados.get(0).getId();

		// Lo pasamos a la salida de Dominio
		Pecera dominio = peceraToPeceraEntityMapper.fromOutputToInput(listaDatosGuardados.get(0));

		// Lo pasamos a la salida del Controlador
		PeceraDto salida = peceraToPeceraDtoMapper.fromInputToOutput(dominio);

		mockMvc.perform(MockMvcRequestBuilders.get(PECERA_ENDPOINT + "/" + id).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				// Validamos que la salida es la misma que lo guardado
				.andExpect(MockMvcResultMatchers.content().json(TestUtils.writeAsString(salida)))
				.andDo(MockMvcResultHandlers.print());
	}

	@Test
	void testGetPeceraKoNoContent() throws Exception {
		// Nos aseguramos que no hay elementos que encontrar
		peceraRepository.deleteAll();

		// Buscamos por un id que no existe
		String id = "noExiste";

		mockMvc.perform(MockMvcRequestBuilders.get(PECERA_ENDPOINT + "/" + id).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(204)).andDo(MockMvcResultHandlers.print());
	}

	@Test
	void testPostPeceraOk() throws Exception {
		// Si queremos leer mas mensajes
		// kafkaConsumerPostedPeceraEventServiceTest.setLatch(new CountDownLatch(5));

		// Nos aseguramos que no hay elementos que encontrar
		peceraRepository.deleteAll();

		// Creamos un comando de entrada random
		PostPutPeceraDto objetoDto = TestUtils.createObject(PostPutPeceraDto.class);

		mockMvc.perform(MockMvcRequestBuilders.post(PECERA_ENDPOINT).content(TestUtils.writeAsString(objetoDto))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isCreated()).andExpect(MockMvcResultMatchers.header()
						.string("Location", CoreMatchers.containsString("http://localhost/peceras/")))
				.andDo(MockMvcResultHandlers.print());

		List<PeceraEntity> objetosAlmacenados = peceraRepository.findAll();
		assertEquals(1, objetosAlmacenados.size());
		assertEquals(objetoDto.getValue(), objetosAlmacenados.get(0).getValue());

		boolean consumido = consumerPostedPeceraEventTestService.getLatch().await(5, TimeUnit.SECONDS);
		assertTrue(consumido);
		assertNotNull(consumerPostedPeceraEventTestService.getPayload());

		PeceraEventDto eventoLanzado = consumerPostedPeceraEventTestService.getPayload();
		assertNotNull(eventoLanzado);
		assertEquals(objetoDto.getValue(), eventoLanzado.getValue());
	}

	@Test
	void testPatchPeceraOk() throws Exception {
		// Recuperamos todos los datos que haya en BBDD
		List<PeceraEntity> listaDatosGuardados = peceraRepository.findAll();

		// Guardamos un id existente
		String id = listaDatosGuardados.get(0).getId();

		// Pasamos el ValueObject Entity a Dominio
		ValueObject valueObjectDominio = valueObjectToValueObjectEntityMapper
				.fromOutputToInput(listaDatosGuardados.get(0).getValueObject());

		// Pasamos el ValueObject Dominio a Dto
		ValueObjectDto valueObjectDto = valueObjectToValueObjectDtoMapper.fromInputToOutput(valueObjectDominio);

		// Creamos el objeto de entrada
		PatchPeceraDto objetoDto = PatchPeceraDto.builder().value("NuevoValor").valueObject(valueObjectDto).build();

		mockMvc.perform(
				MockMvcRequestBuilders.patch(PECERA_ENDPOINT + "/" + id).content(TestUtils.writeAsString(objetoDto))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isNoContent())
				.andDo(MockMvcResultHandlers.print());

		Optional<PeceraEntity> objetoActualizado = peceraRepository.findById(id);
		assertTrue(objetoActualizado.isPresent());
		assertEquals(objetoDto.getValue(), objetoActualizado.get().getValue());
		assertEquals(objetoDto.getValueObject().getValue(), objetoActualizado.get().getValueObject().getValue());

		boolean consumido = consumerPatchedPeceraEventTestService.getLatch().await(5, TimeUnit.SECONDS);
		assertTrue(consumido);
		assertNotNull(consumerPatchedPeceraEventTestService.getPayload());

		PeceraEventDto eventoLanzado = consumerPatchedPeceraEventTestService.getPayload();
		assertNotNull(eventoLanzado);
		assertEquals(objetoDto.getValue(), eventoLanzado.getValue());
	}

	@Test
	void testPatchPeceraKoNotFound() throws Exception {

		// Nos aseguramos que no hay elementos que actualizar
		peceraRepository.deleteAll();

		// Buscamos por un id que no existe
		String idNoExiste = "NoExiste";

		// Creamos un comando de entrada random
		PatchPeceraDto objetoDto = TestUtils.createObject(PatchPeceraDto.class);

		mockMvc.perform(MockMvcRequestBuilders.patch(PECERA_ENDPOINT + "/" + idNoExiste)
				.content(TestUtils.writeAsString(objetoDto)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().is(400))
				.andExpect(MockMvcResultMatchers.content().string(Errors.PECERA_NOT_FOUND))
				.andDo(MockMvcResultHandlers.print());

		Optional<PeceraEntity> objetoActualizado = peceraRepository.findById(idNoExiste);
		assertFalse(objetoActualizado.isPresent());

		boolean consumido = consumerPatchedPeceraEventTestService.getLatch().await(5, TimeUnit.SECONDS);
		assertFalse(consumido);
	}

	@Test
	void testPutPeceraOk() throws Exception {
		// Recuperamos todos los datos que haya en BBDD
		List<PeceraEntity> listaDatosGuardados = peceraRepository.findAll();

		// Guardamos un id existente
		String id = listaDatosGuardados.get(0).getId();

		// Creamos el objeto de entrada
		PostPutPeceraDto objetoDto = PostPutPeceraDto.builder().value(listaDatosGuardados.get(0).getValue()).valueObject(null)
				.build();

		mockMvc.perform(
				MockMvcRequestBuilders.put(PECERA_ENDPOINT + "/" + id).content(TestUtils.writeAsString(objetoDto))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isNoContent())
				.andDo(MockMvcResultHandlers.print());

		Optional<PeceraEntity> objetoActualizado = peceraRepository.findById(id);
		assertTrue(objetoActualizado.isPresent());
		assertNull(objetoActualizado.get().getValueObject());

		boolean consumido = consumerPutPeceraEventTestService.getLatch().await(5, TimeUnit.SECONDS);
		assertTrue(consumido);
		assertNotNull(consumerPutPeceraEventTestService.getPayload());

		PeceraEventDto eventoLanzado = consumerPutPeceraEventTestService.getPayload();
		assertNotNull(eventoLanzado);
		assertNull(eventoLanzado.getValueObject());
	}

	@Test
	void testPutPeceraKoNotFound() throws Exception {
		// Nos aseguramos que no hay elementos que actualizar
		peceraRepository.deleteAll();

		// Buscamos por un id que no existe
		String idNoExiste = "NoExiste";

		// Creamos un Comando random
		PostPutPeceraDto objetoDto = TestUtils.createObject(PostPutPeceraDto.class);

		mockMvc.perform(MockMvcRequestBuilders.put(PECERA_ENDPOINT + "/" + idNoExiste)
				.content(TestUtils.writeAsString(objetoDto)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().is(400))
				.andExpect(MockMvcResultMatchers.content().string(Errors.PECERA_NOT_FOUND))
				.andDo(MockMvcResultHandlers.print());

		Optional<PeceraEntity> objetoActualizado = peceraRepository.findById(idNoExiste);
		assertFalse(objetoActualizado.isPresent());

		boolean consumido = consumerPutPeceraEventTestService.getLatch().await(5, TimeUnit.SECONDS);
		assertFalse(consumido);
	}

	@Test
	void testDeletePeceraOk() throws Exception {
		// Buscamos todos los elementos guardados en BBDD
		List<PeceraEntity> listaDatosGuardados = peceraRepository.findAll();

		// Guardamos un id existente
		String id = listaDatosGuardados.get(0).getId();

		mockMvc.perform(MockMvcRequestBuilders.delete(PECERA_ENDPOINT + "/" + id).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isNoContent()).andDo(MockMvcResultHandlers.print());

		Optional<PeceraEntity> objetoActualizado = peceraRepository.findById(id);
		assertFalse(objetoActualizado.isPresent());

		boolean consumido = consumerDeletedPeceraEventTestService.getLatch().await(5, TimeUnit.SECONDS);
		assertTrue(consumido);
		assertNotNull(consumerDeletedPeceraEventTestService.getPayload());

		PeceraEventDto eventoLanzado = consumerDeletedPeceraEventTestService.getPayload();
		assertNotNull(eventoLanzado);
		assertEquals(listaDatosGuardados.get(0).getId(), eventoLanzado.getId());
	}

	@Test
	void testInputEvent() throws JsonProcessingException, InterruptedException {
		// Buscamos todos los elementos guardados en BBDD
		List<PeceraEntity> listaDatosGuardados = peceraRepository.findAll();

		// Podemos hacerlo con Builder, crear mappers o como lo haga el Servicio de
		// Dominio
		PeceraInputEventDto objetoEvento = PeceraInputEventDto.builder().id(listaDatosGuardados.get(0).getId())
				.value("NuevoValor").build();

		producerPeceraEvent.sendMessageAsynch(topicInputEvent, objetoEvento);

		// Damos algo de tiempo para:
		// - La creacion asincrona del dato en BBDD
		// - El lanzamiento asincrono del nuevo evento
		TestUtils.waitMilliseconds(4000l);

		Optional<PeceraEntity> objetoActualizado = peceraRepository.findById(objetoEvento.getId());
		assertTrue(objetoActualizado.isPresent());
		assertEquals(objetoEvento.getValue(), objetoActualizado.get().getValue());
	}

	// Este test cubre partes del codigo que saldrian con errores del Kafka
	@Test
	void testKafkaProducerServiceFails() throws Exception {
		// Estos metodos logan una excepcion pero no hacen fallar el test
		producerPeceraEvent.handleKafkaConnectionException(TestUtils.createObject(PeceraEventDto.class),
				new Exception("prueba"));
		producerPeceraEvent.handleKafkaMsgFailure(TestUtils.createObject(PeceraEventDto.class),
				new Throwable("prueba"));
		assertTrue(true);
	}

}
