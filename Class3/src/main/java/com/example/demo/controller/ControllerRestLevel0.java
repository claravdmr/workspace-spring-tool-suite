package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.PostConstruct;

@RestController

public class ControllerRestLevel0 {

	private static final Logger log = LoggerFactory.getLogger(ControllerRestLevel0.class);

	private final Map<Integer, String> messages = new HashMap<>();

	@PostConstruct
	public void postContruct() {
		log.info("info!");
		log.debug("help debug!");
		log.error("error :(");

	}

//	@RequestMapping
//	public void test() {
//		log.info("init test");
//	}
//
//	@RequestMapping("/test1")
//	public void test1() {
//		log.info("init test 1");
//	}
//
//	@RequestMapping("/test2")
//	public String test2() {
//		log.info("init test 2");
//		return "Test string";
//	}
//
//	@RequestMapping("/test3")
//	public String test3(String message) {
//		log.info("init test 3");
//		return "Test string " + message;
//	}
//
//	@RequestMapping(value = "/addMessage", method = RequestMethod.GET)
//	public ResponseEntity<Integer> addMessage(@RequestParam String message) {
//		log.info("init addMessage");
//		Integer newId = messages.size() + 1; // this can cause problems and is not recommended it is just for illustration here
//		messages.put(newId, message);
//		log.debug("Message: {} - {}", newId, message);
//		return ResponseEntity.ok(newId);
//	}
//
//	@RequestMapping(value = "/getMessage", method = RequestMethod.GET)
//	public ResponseEntity<String> getMessage(@RequestParam Integer id) {
//		log.info("init getMessage {}", id);
//		if (messages.containsKey(id)) {
//			return ResponseEntity.ok(messages.get(id));
//		}
//		return ResponseEntity.notFound().build();
//	}
//
//	@RequestMapping(value = "/getAllMessages", method = RequestMethod.GET)
//	public ResponseEntity<Map<Integer, String>> getAllMessages() {
//		log.info("init getAllMessages");
//		return ResponseEntity.ok(messages);
//
//	}
//
//	@RequestMapping(value = "/deleteMessage", method = RequestMethod.GET)
//	public ResponseEntity<Void> deleteMessage(@RequestParam Integer id) {
//		log.info("init deleteMessage {}", id);
//		if (messages.containsKey(id)) {
//			messages.remove(id);
//			log.debug("Message deleted.");
//			return ResponseEntity.noContent().build();
//		}
//		return ResponseEntity.badRequest().build();
//	}
}
