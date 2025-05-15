package com.demo.arq.infrastructure.event.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.demo.arq.application.port.input.PeceraServiceInputPort;
import com.demo.arq.domain.exception.BusinessException;
import com.demo.arq.infrastructure.event.dto.PeceraInputEventDto;
import com.demo.arq.infrastructure.event.mapper.PeceraToPeceraInputEventDtoMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PeceraConsumerService {

	@Autowired
	PeceraServiceInputPort peceraService;

	@Autowired
	PeceraToPeceraInputEventDtoMapper peceraToPeceraInputEventDtoMapper;

	@KafkaListener(
			topics = "${custom.topic.pecera.input-event}", 
			groupId = "${spring.kafka.consumer.group-id}", 
			containerFactory = "containerFactory")
	public void receive(ConsumerRecord<String, PeceraInputEventDto> consumerRecord) {
		log.debug("receive");

		try {
			peceraService.modificacionParcialPecera(
					peceraToPeceraInputEventDtoMapper.fromOutputToInput(consumerRecord.value()));
		} catch (BusinessException e) {
			log.error("Error Leyendo Mensaje Kafka", e);
			// Aqui podemos guardar el mensaje en BBDD para su posterior analisis
		}
	}

}
