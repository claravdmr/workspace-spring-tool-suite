package com.class5;

import java.util.Random;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@ConditionalOnExpression("${property1} == true")
public class CallsService {

	private final RestTemplate restTemplate = new RestTemplate();

	@Scheduled(fixedDelay = 1000L)
	public void call() {

		Random r = new Random();

		String otherServiceEntered;

		if (r.nextBoolean()) {
			otherServiceEntered = "task1";
		} else {
			otherServiceEntered = "taskx";
		}

		String host = "http://localhost:8080/internal-call?entry=" + otherServiceEntered;

		restTemplate.exchange(host, HttpMethod.GET, null, String.class);
		// the above is string.class as response entity in controller returns string.
	}

}
