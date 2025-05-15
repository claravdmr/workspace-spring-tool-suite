package com.example.mechanical_workshop.test.util;

import java.util.concurrent.CountDownLatch;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class ConsumerLatch<T> {

	private CountDownLatch latch = new CountDownLatch(1);

	private T payload;

	public void consumeMsg(ConsumerRecord<String, T> consumerRecord) {
		payload = consumerRecord.value();
		latch.countDown();
	}

	public T getPayload() {
		return payload;
	}

	public CountDownLatch getLatch() {
		return latch;
	}
	
	public void setLatch(CountDownLatch latch) {
		this.latch = latch;
	}

	public void resetLatch() {
		payload = null;
		latch = new CountDownLatch(1);
	}
}
