package com.demo.arq.infrastructure.event.producer;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ProducerPeceraEvent {

	@Autowired
	KafkaTemplate<String, Object> kafkaTemplate;

	public void sendMessageAsynch(String topicName, Object msg) {
		CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topicName, msg);
		future.handle((result, ex) -> {
			if (ex != null) {
				if (ex instanceof TimeoutException e) {
					handleKafkaTimeoutException(msg, e);
				} else if (ex instanceof InterruptedException e) {
					handleKafkaInterruptedException(msg, e);
				} else if (ex instanceof ExecutionException e) {
					handleKafkaExecutionException(msg, e);
				} else {
					handleKafkaMsgFailure(msg, ex.getCause());
				}
			} else {
				handleSuccess(msg);
			}
			return result;
		});
	}

	private void handleKafkaTimeoutException(Object msg, TimeoutException e) {
		log.error("handleKafkaTimeoutException: {}", msg, e);
	}

	private void handleKafkaInterruptedException(Object msg, InterruptedException e) {
		log.error("handleKafkaInterruptedException: {}", msg, e);
	}

	private void handleKafkaExecutionException(Object msg, ExecutionException e) {
		log.error("handleKafkaExecutionException: {}", msg, e);
	}

	private void handleKafkaMsgFailure(Object msg, Throwable cause) {
		log.error("handleKafkaMsgFailure: {}", msg, cause);
	}

	private void handleSuccess(Object msg) {
		log.debug("handleSuccess: {}", msg);
	}

}
