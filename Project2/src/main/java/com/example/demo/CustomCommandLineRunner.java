package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class CustomCommandLineRunner implements CommandLineRunner{

	@Override
	public void run(String... args) throws Exception {
		System.out.println("run");
		
	}
	
	@PostConstruct
	public void postContruct() {
		System.out.println("post construct");
	}
	
//	@Scheduled(fixedDelay = 1000L)
//	public void doSomething() {
//		System.out.println("doSomething @Scheduled");
//	}
//	
//	@Scheduled(fixedRate = 1000L)
//	public void doSomething2() {
//		System.out.println("doSomething2 @Scheduled");
//	}
//	
//	@Scheduled(initialDelay = 1000L, fixedRate = 1000L)
//	public void doSomething3() {
//		System.out.println("doSomething2 @Scheduled");
//	}

}