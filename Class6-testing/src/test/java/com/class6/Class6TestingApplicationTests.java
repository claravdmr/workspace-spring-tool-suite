
package com.class6;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.class6.controller.Controller;
import com.class6.dto.Car;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = { Class6TestingApplication.class, AnnotationConfigContextLoader.class })
@ComponentScan(basePackages = { "com.class6" })
class Class6TestingApplicationTests {

	@Value("${fee}")
	private Float fee;
	
	@Autowired
	private MockMvc mockMvc;

	@Autowired // this is like a 'new'
	Controller controller;

	ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules()
			.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
			.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false);
	//
	
	
	@BeforeEach
	public void beforeEach() {
		controller.setCars(new ArrayList<>());
	}

	@Test
	void getCarsNoContentTest() throws Exception {
		
		mockMvc.perform(
				MockMvcRequestBuilders
				.get("/cars")
				.accept(MediaType.APPLICATION_JSON)
				.with(SecurityMockMvcRequestPostProcessors.
				csrf())
				)
			.andExpect(MockMvcResultMatchers.status().is(200))
			.andDo(MockMvcResultHandlers.print());
	}

	@Test
	void getCarsTest() throws Exception {

		Car car = new Car();
		car.setId("666");
		car.setRegistration("11111");
		controller.getCars().add(car);

		mockMvc.perform(
				MockMvcRequestBuilders
				.get("/cars")
				.accept(MediaType.APPLICATION_JSON)
				.with(SecurityMockMvcRequestPostProcessors
				.csrf())
				)
			.andExpect(MockMvcResultMatchers.status().is(200))
			.andExpect(MockMvcResultMatchers.content().string(CoreMatchers.containsString("\"id\":\"666\"")))
			.andDo(MockMvcResultHandlers.print());
	}

	@Test
	void getCarTest() throws Exception {

		Car car = new Car();
		car.setId("666");
		car.setRegistration("11111");
		controller.getCars().add(car);

		mockMvc.perform(
				MockMvcRequestBuilders
				.get("/cars/666")
				.accept(MediaType.APPLICATION_JSON)
				.with(SecurityMockMvcRequestPostProcessors
				.csrf())
				)
			.andExpect(MockMvcResultMatchers.status().is(200))
			.andExpect(MockMvcResultMatchers.content().string(CoreMatchers.containsString("\"id\":\"666\"")))
			.andDo(MockMvcResultHandlers.print());
	}

	@Test
	void getCarTestNoContent() throws Exception {

		mockMvc.perform(
				MockMvcRequestBuilders
				.get("/cars/xxx")
				.accept(MediaType.APPLICATION_JSON)
				.with(SecurityMockMvcRequestPostProcessors
				.csrf())
				)
			.andExpect(MockMvcResultMatchers.status().is(204))
			.andDo(MockMvcResultHandlers.print());
	}

	@Test
	void carEntryTest() throws Exception {

		Car carEnter = new Car();
		carEnter.setRegistration("1111");
		carEnter.setEntryDate(LocalDateTime.now());

		mockMvc.perform(
				MockMvcRequestBuilders.post("/cars")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(carEnter))
				.accept(MediaType.APPLICATION_JSON)
				.with(SecurityMockMvcRequestPostProcessors
				.csrf())
				)
			
			.andExpect(MockMvcResultMatchers.status().is(201))
			.andDo(MockMvcResultHandlers.print());
		
		Assertions.assertFalse(controller.getCars().isEmpty());
		Assertions.assertTrue(controller.getCars().get(0).getRegistration().equalsIgnoreCase("1111"));
	}
	
	@Test
	void calculateFeeTest() throws Exception {

		Car carEnter = new Car();
		carEnter.setId("666");
		carEnter.setRegistration("1111");
		carEnter.setEntryDate(LocalDateTime.now().minusSeconds(100));
		controller.getCars().add(carEnter);

		Float a = 100 * fee;
		
		mockMvc.perform(
				MockMvcRequestBuilders.get("/cars/666/fee")
				.accept(MediaType.APPLICATION_JSON)
				.with(SecurityMockMvcRequestPostProcessors
				.csrf())
				)
			
			.andExpect(MockMvcResultMatchers.status().is(200))
			.andExpect(MockMvcResultMatchers.content().string(CoreMatchers.containsString(a.toString())))
			.andDo(MockMvcResultHandlers.print());
		
	}
	
	@Test
	void carExitTest() throws Exception {

		Car car = new Car();
		car.setId("666");
		car.setRegistration("1111");
		controller.getCars().add(car);
		
		mockMvc.perform(
				MockMvcRequestBuilders.patch("/cars")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(car))
				.accept(MediaType.APPLICATION_JSON)
				.with(SecurityMockMvcRequestPostProcessors
				.csrf())
				)
			
			.andExpect(MockMvcResultMatchers.status().is(204))
			.andDo(MockMvcResultHandlers.print());
		
		Assertions.assertFalse(controller.getCars().isEmpty());
		Assertions.assertNotNull(controller.getCars().get(0).getExitDate());
		
	}
	
	@Test
	void deleteCarTest() throws Exception {

		Car car = new Car();
		car.setId("666");
		car.setRegistration("1111");
		controller.getCars().add(car);
		
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/cars/666")
				.accept(MediaType.APPLICATION_JSON)
				.with(SecurityMockMvcRequestPostProcessors
				.csrf())
				)
			
			.andExpect(MockMvcResultMatchers.status().is(204))
			.andDo(MockMvcResultHandlers.print());
		
		Assertions.assertFalse(controller.getCars().isEmpty());
		Assertions.assertTrue(controller.getCars().get(0).isDeleted());

		
		
	}
	

}
