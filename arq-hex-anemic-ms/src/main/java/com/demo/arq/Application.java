package com.demo.arq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@EnableCaching
@SpringBootApplication
@ComponentScan({"com.demo.arq"})

// Hay que usar una de las tres opciones de aqui abajo:

//Si vamos a usar Oracle y Mongo a la vez, Necesario descomentar esta linea:
@EnableAutoConfiguration

//Si solo usamos Mongo, Necesario descomentar esta linea:
//@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})

//Si solo usamos Oracle, Necesario descomentar esta linea:
//@EnableAutoConfiguration(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})

public class Application {
	public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
