package com.example.mechanical_workshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class MechanicalWorkshopApplication {

	public static void main(String[] args) {
		SpringApplication.run(MechanicalWorkshopApplication.class, args);
	}

}
