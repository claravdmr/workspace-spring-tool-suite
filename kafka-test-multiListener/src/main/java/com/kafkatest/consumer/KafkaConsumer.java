package com.kafkatest.consumer;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

import com.kafkatest.dto.MessageDto;
import com.kafkatest.dto.NewMessageDto;

@Component
@KafkaListener(																									//partition 0 as there is only 1, offset means from the 50th message
		topicPartitions = { @TopicPartition(topic = "${custom.topics.one}", partitionOffsets = { @PartitionOffset(partition = "0", initialOffset = "50") }) }, 
		groupId = "${custom.groups.one}", 
		containerFactory = "messageContainerFactory")

		//partitions are rarely used, this was as there was some error for unknown reasons.

public class KafkaConsumer {
	
	@KafkaHandler
	public void messageListener(NewMessageDto message) {
		System.out.println("Message received (NEWdto): " + message.getMessage());
	}
	
	@KafkaHandler
	public void messageListener(MessageDto message) {
		System.out.println("Message received (dto): " + message.getMessage());
	}
	
	@KafkaHandler
	public void messageListener(String message) {
		System.out.println("Message 2 received (string): " + message);
	}

}
