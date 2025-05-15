package com.lombokTest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@Slf4j //this implements a log to the class
@SpringBootTest
class LombokTestApplicationTests {

	@Test
	void contextLoads() {
		
		TestObject obj = new TestObject();
		TestObject obj2 = new TestObject("Hi!", 10.0f, 5, true);
		
		//the below is the same as obj2
		TestObject objBuilder = TestObject.builder()
				.attribute1("Hi!")
				.attribute2(10.0f)
				.attribute3(5)
				.attribute4(true)
				.build();
		//
		
		System.out.println(obj.equals(obj2));
		
		System.out.println(obj2);
		System.out.println(objBuilder);
		
		log.error("Done!");
		log.warn("Warning!");


	}

}
