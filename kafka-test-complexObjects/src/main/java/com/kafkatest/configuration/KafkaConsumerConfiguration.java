package com.kafkatest.configuration;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.kafkatest.dto.MessageDto;

@Configuration
@EnableKafka
public class KafkaConsumerConfiguration {
	
	@Value("${spring.kafka.host}")
	private String bootstrapAddress;
	
	@Bean
	ConsumerFactory<String, MessageDto> messageConsumerFactory(){
		Map<String,Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.kafkatest.dto");
		return new DefaultKafkaConsumerFactory<>(props);
	}
	
	@Bean
	ConcurrentKafkaListenerContainerFactory<String,MessageDto> messageContainerFactory(){
		ConcurrentKafkaListenerContainerFactory<String,MessageDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(messageConsumerFactory());
		return factory;
	}


}
