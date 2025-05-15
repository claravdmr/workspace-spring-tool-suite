package com.demo.arq.infrastructure.event.producer;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ProducerPeceraEvent {
	
	@Value("${spring.kafka.timeout:6000}")
    private long kafkaTimeout;

    @Autowired
	KafkaTemplate<String, Object> kafkaTemplate;

	/**
	 * Envía de forma síncrona un mensaje al topico descrito en topicName. Se puede
	 * hacer un override del método si se requiere un listenableFuture diferente
	 *
	 * @param msg Mensaje a enviar
	 */
	public void sendMessage(String topicName, Object msg) {
		try {
			kafkaTemplate.send(topicName, msg).get(kafkaTimeout, TimeUnit.MILLISECONDS);
			handleSuccess(msg);
		} catch (ExecutionException e) {
			handleKafkaMsgFailure(msg, e.getCause());
		} catch (TimeoutException e) {
			handleKafkaConnectionException(msg, e);
		} catch (InterruptedException e) {
			handleKafkaConnectionException(msg, e);
			Thread.currentThread().interrupt();
		}

	}

	/**
	 * Envía a publicar un mensaje en el tópico descrito en topicName. Se realiza el
	 * envío de forma asincrona
	 *
	 * @param msg
	 */
	public void sendMessageAsynch(String topicName, Object msg) {
		CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topicName, msg);
		future.handle((result, ex) -> {
			if (ex != null) {
				if (ex.getCause() instanceof TimeoutException e) {
					handleKafkaConnectionException(msg, e);
				} else {
					handleKafkaMsgFailure(msg, ex.getCause());
				}
			} else {
				handleSuccess(msg);
			}
			return result;
		});
	}
	
	public void handleSuccess(Object msg) {
		log.debug("handleSuccess");
	}

	public void handleKafkaConnectionException(Object msg, Exception e) {
		log.error("handleKafkaConnectionException", e);
	}

	public void handleKafkaMsgFailure(Object msg, Throwable cause) {
		log.error("handleKafkaMsgFailure", cause);
	}

}
