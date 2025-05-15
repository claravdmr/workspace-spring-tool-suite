package com.exampleKafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.exampleKafka.dto.BranchEventDto;

@Service
public class ConsumerBranchEventTestService extends ConsumerLatch<BranchEventDto> {
   
	@KafkaListener(
		topics = "${custom.topic.branch}",
        groupId = "${spring.kafka.consumer.group-id}-consumer-latch",
        containerFactory = "containerFactory" // this is the method in the configuration
    )
    
	public void receive(ConsumerRecord<String, BranchEventDto> consumerRecord) {
        this.consumeMsg(consumerRecord);
    }
}
