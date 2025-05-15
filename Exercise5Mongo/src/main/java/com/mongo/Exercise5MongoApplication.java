package com.mongo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class Exercise5MongoApplication {

	public static void main(String[] args) {
		SpringApplication.run(Exercise5MongoApplication.class, args);
	}

}
