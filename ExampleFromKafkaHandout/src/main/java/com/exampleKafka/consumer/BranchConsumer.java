package com.exampleKafka.consumer;



import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.exampleKafka.dto.NewCarInputEventDto;
import com.exampleKafka.service.BranchService;

@Component
public class BranchConsumer {
    private static final Logger Log = LoggerFactory.getLogger(BranchConsumer.class);

    @Autowired
    BranchService branchService;

    @KafkaListener(
        topics = "${custom.topic.new-car}",
        groupId = "${spring.kafka.consumer.group-id}",
        containerFactory = "containerFactory" // this is the name of the method in the kafka configuration.
    )
    public void receive(ConsumerRecord<String, NewCarInputEventDto> consumerRecord) {
        Log.debug("MESSAGE RECEIVED");
        branchService.newCar(consumerRecord.value().getInfoNewCar());
    }
}
