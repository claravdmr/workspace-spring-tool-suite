package com.mongo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories
@SpringBootApplication
public class Exercise13MongoApplication {

	public static void main(String[] args) {
		SpringApplication.run(Exercise13MongoApplication.class, args);
	}

}
