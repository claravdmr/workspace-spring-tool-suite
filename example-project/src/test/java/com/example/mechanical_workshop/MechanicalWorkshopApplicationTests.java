package com.example.mechanical_workshop;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;
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

import com.example.mechanical_workshop.application.util.Errors;
import com.example.mechanical_workshop.domain.model.Customer;
import com.example.mechanical_workshop.infrastructure.apirest.dto.customer.RequestPostPutCustomerDto;
import com.example.mechanical_workshop.infrastructure.apirest.dto.customer.ResponseCustomerDto;
import com.example.mechanical_workshop.infrastructure.apirest.mapper.customer.CustomerToCustomerDtoMapper;
import com.example.mechanical_workshop.infrastructure.apirest.mapper.customer.CustomerToPatchCustomerDtoMapper;
import com.example.mechanical_workshop.infrastructure.apirest.mapper.customer.CustomerToPostPutCustomerDtoMapper;
import com.example.mechanical_workshop.infrastructure.integrationevents.producer.KafkaProducer;
import com.example.mechanical_workshop.infrastructure.repository.mongodb.entity.CustomerEntity;
import com.example.mechanical_workshop.infrastructure.repository.mongodb.mapper.CustomerToCustomerEntityMapper;
import com.example.mechanical_workshop.infrastructure.repository.mongodb.service.CustomerRepository;
import com.example.mechanical_workshop.test.kafka.ConsumerDeletedCustomerEventTestService;
import com.example.mechanical_workshop.test.kafka.ConsumerModifiedCustomerEventTestService;
import com.example.mechanical_workshop.test.kafka.ConsumerPostedCustomerEventTestService;
import com.example.mechanical_workshop.test.util.TestUtils;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureDataJpa
@AutoConfigureDataMongo
@ContextConfiguration(classes = { MechanicalWorkshopApplication.class, AnnotationConfigContextLoader.class })
@DirtiesContext
@Execution(ExecutionMode.CONCURRENT)
@ComponentScan(basePackages = { "com.example.mechanical_workshop" })
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9095", "port=9095" })
class MechanicalWorkshopApplicationTests {

	private static final String CUSTOMER_ENDPOINT = "/customers";

	@Autowired
	MockMvc mockMvc;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	CustomerToCustomerEntityMapper customerToCustomerEntityMapper;

	@Autowired
	CustomerToCustomerDtoMapper customerToCustomerDtoMapper;

	@Autowired
	CustomerToPatchCustomerDtoMapper customerToPatchCustomerDtoMapper;

	@Autowired
	CustomerToPostPutCustomerDtoMapper customerToPostPutCustomerDtoMapper;

	@Autowired
	ConsumerPostedCustomerEventTestService consumerPostedCustomerEventTestServices;

	@Autowired
	ConsumerModifiedCustomerEventTestService consumerModifiedCustomerEventTestService;

	@Autowired
	ConsumerDeletedCustomerEventTestService consumerDeletedCustomerEventTestService;

	@Autowired
	KafkaProducer kafkaProducer;

	@Value("${custom.topic.customer.input-event}")
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
		customerRepository.deleteAll();
		List<CustomerEntity> listCustomersToSave = TestUtils.createObjects(CustomerEntity.class, 10);
		listCustomersToSave.forEach(p -> p.setEliminado(false));
		customerRepository.saveAll(listCustomersToSave);
		consumerPostedCustomerEventTestServices.resetLatch();
		consumerModifiedCustomerEventTestService.resetLatch();
		consumerDeletedCustomerEventTestService.resetLatch();
	}

	@Test
	void testGetCustomersOk() throws Exception {
		Page<CustomerEntity> customersSaved = customerRepository.findByEliminado(false, Pageable.ofSize(20));

		Page<Customer> listCustomers = customerToCustomerEntityMapper.fromOutputToInput(customersSaved);

		Page<ResponseCustomerDto> customersDto = customerToCustomerDtoMapper.fromInputToOutput(listCustomers);

		mockMvc.perform(MockMvcRequestBuilders.get(CUSTOMER_ENDPOINT).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().json(TestUtils.writeAsString(customersDto)))
				.andDo(MockMvcResultHandlers.print());
	}

	@Test
	void testGetCustomersFail() throws Exception {
		String exceedPagination = "?size=200";

		mockMvc.perform(
				MockMvcRequestBuilders.get(CUSTOMER_ENDPOINT + exceedPagination).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.content().string(Errors.MAXIMUM_PAGINATION_EXCEEDED))
				.andDo(MockMvcResultHandlers.print());
	}

	@Test
	void testGetCustomerOk() throws Exception {

		Page<CustomerEntity> customersSaved = customerRepository.findByEliminado(false, Pageable.ofSize(20));

		String id = customersSaved.getContent().get(0).getId();

		Customer customer = customerToCustomerEntityMapper.fromOutputToInput(customersSaved.getContent().get(0));

		ResponseCustomerDto customerDto = customerToCustomerDtoMapper.fromInputToOutput(customer);

		mockMvc.perform(MockMvcRequestBuilders.get(CUSTOMER_ENDPOINT + "/" + id).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().json(TestUtils.writeAsString(customerDto)))
				.andDo(MockMvcResultHandlers.print());

	}

	@Test
	void testGetCustomerKoNoContent() throws Exception {
		customerRepository.deleteAll();

		String id = "randomId";

		mockMvc.perform(MockMvcRequestBuilders.get(CUSTOMER_ENDPOINT + "/" + id).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isNoContent()).andDo(MockMvcResultHandlers.print());
	}

	@Test
	void testPostCustomerOk() throws Exception {
		customerRepository.deleteAll();

		RequestPostPutCustomerDto customerDto = TestUtils.createObject(RequestPostPutCustomerDto.class);

		mockMvc.perform(MockMvcRequestBuilders.post(CUSTOMER_ENDPOINT).content(TestUtils.writeAsString(customerDto))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.header().string("Location",
						CoreMatchers.containsString("http://localhost/customers/")))
				.andDo(MockMvcResultHandlers.print());

		Page<CustomerEntity> customersSaved = customerRepository.findByEliminado(false, Pageable.ofSize(20));
		assertEquals(1, customersSaved.getNumberOfElements());
		assertEquals(customerDto.getDocumentNumber(), customersSaved.getContent().get(0).getDocumentNumber());

		boolean consumed = consumerPostedCustomerEventTestServices.getLatch().await(10, TimeUnit.SECONDS);
		assertTrue(consumed);
		assertNotNull(consumerPostedCustomerEventTestServices.getPayload());

		assertEquals(customerDto.getDocumentNumber(),
				consumerPostedCustomerEventTestServices.getPayload().getDocumentNumber());
	}

	@Test
	void testPutCustomerOk() throws Exception {
		CustomerEntity existingCustomer = customerRepository.findByEliminado(false, Pageable.ofSize(1)).getContent()
				.get(0);

		RequestPostPutCustomerDto customerDto = TestUtils.createObject(RequestPostPutCustomerDto.class);

		Customer customerDomain = customerToPostPutCustomerDtoMapper.fromOutputToInput(customerDto);
		customerDomain.setId(existingCustomer.getId());

		mockMvc.perform(MockMvcRequestBuilders.put(CUSTOMER_ENDPOINT + "/" + existingCustomer.getId())
				.content(TestUtils.writeAsString(customerDomain)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isNoContent())
				.andDo(MockMvcResultHandlers.print());

		CustomerEntity updatedCustomer = customerRepository.findById(existingCustomer.getId()).orElse(null);
		assertNotNull(updatedCustomer);
		assertEquals(customerDomain.getDocumentNumber(), updatedCustomer.getDocumentNumber());

		boolean consumed = consumerModifiedCustomerEventTestService.getLatch().await(10, TimeUnit.SECONDS);
		assertTrue(consumed);
		assertNotNull(consumerModifiedCustomerEventTestService.getPayload());
		assertEquals(customerDomain.getDocumentNumber(),
				consumerModifiedCustomerEventTestService.getPayload().getDocumentNumber());
	}

	@Test
	void testDeleteCustomerOk() throws Exception {
		CustomerEntity existingCustomer = customerRepository.findByEliminado(false, Pageable.ofSize(1)).getContent()
				.get(0);

		mockMvc.perform(MockMvcRequestBuilders.delete(CUSTOMER_ENDPOINT + "/" + existingCustomer.getId())
				.accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isNoContent())
				.andDo(MockMvcResultHandlers.print());

		CustomerEntity deletedCustomer = customerRepository.findById(existingCustomer.getId()).orElse(null);
		assertNotNull(deletedCustomer);
		assertTrue(deletedCustomer.isEliminado());

		boolean consumed = consumerDeletedCustomerEventTestService.getLatch().await(10, TimeUnit.SECONDS);
		assertTrue(consumed);
		assertEquals(existingCustomer.getId(), consumerDeletedCustomerEventTestService.getPayload().getId());
	}
}
