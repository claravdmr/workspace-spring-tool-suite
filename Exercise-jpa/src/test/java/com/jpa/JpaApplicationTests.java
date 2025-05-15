package com.jpa;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.jpa.repository.AccessRepository;


@SpringBootTest
class JpaApplicationTests {

	@Autowired
	AccessRepository accessRepository;
	
	@BeforeEach
	void beforeEachTest() {
		accessRepository.deleteAll();
	}
	
	@Test
	void contextLoads() {
	}

}

