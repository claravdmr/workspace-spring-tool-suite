package com.class5;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

	@GetMapping("/internal-call")
	public ResponseEntity<String> internalCall(@RequestParam String entry) {
		System.out.println("We have reached the call.");

		String result;

		if (entry != null && entry.equalsIgnoreCase("task1")) {
			System.out.println("Task 1");
			result = "Task1 complete.";
		} else {
			System.out.println("No task");
			result = "No tasks to complete.";
		}

		return ResponseEntity.ok(result);
	}

}
