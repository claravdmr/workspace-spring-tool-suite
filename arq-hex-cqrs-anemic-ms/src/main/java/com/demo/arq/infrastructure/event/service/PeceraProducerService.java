package com.demo.arq.infrastructure.event.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.demo.arq.application.port.output.PeceraProducerOutputPort;
import com.demo.arq.domain.event.DeletedPeceraEvent;
import com.demo.arq.domain.event.PatchedPeceraEvent;
import com.demo.arq.domain.event.PostedPeceraEvent;
import com.demo.arq.domain.event.PutPeceraEvent;
import com.demo.arq.infrastructure.event.dto.PeceraEventDto;
import com.demo.arq.infrastructure.event.mapper.DeletedPeceraEventToPeceraEventDtoMapper;
import com.demo.arq.infrastructure.event.mapper.PatchedPeceraEventToPeceraEventDtoMapper;
import com.demo.arq.infrastructure.event.mapper.PostedPeceraEventToPeceraEventDtoMapper;
import com.demo.arq.infrastructure.event.mapper.PutPeceraEventToPeceraEventDtoMapper;
import com.demo.arq.infrastructure.event.producer.ProducerPeceraEvent;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PeceraProducerService implements PeceraProducerOutputPort {

	@Value("${custom.topic.pecera.posted}")
	private String topicPostedPecera;

	@Value("${custom.topic.pecera.patched}")
	private String topicPatchedPecera;

	@Value("${custom.topic.pecera.put}")
	private String topicPutPecera;

	@Value("${custom.topic.pecera.deleted}")
	private String topicDeletedPecera;

	@Autowired
	ProducerPeceraEvent producerPeceraEvent;

	@Autowired
	PostedPeceraEventToPeceraEventDtoMapper postedPeceraEventToPeceraEventDtoMapper;

	@Autowired
	PatchedPeceraEventToPeceraEventDtoMapper patchedPeceraEventToPeceraEventDtoMapper;

	@Autowired
	PutPeceraEventToPeceraEventDtoMapper putPeceraEventToPeceraEventDtoMapper;

	@Autowired
	DeletedPeceraEventToPeceraEventDtoMapper deletedPeceraEventToPeceraEventDtoMapper;

	@Override
	public void eventoCreacionPecera(@Valid PostedPeceraEvent event) {
		log.debug("eventoCreacionPecera");

		PeceraEventDto eventoSalida = postedPeceraEventToPeceraEventDtoMapper.fromInputToOutput(event);

		producerPeceraEvent.sendMessageAsynch(topicPostedPecera, eventoSalida);
	}

	@Override
	public void eventoModificacionParcialPecera(@Valid PatchedPeceraEvent event) {
		log.debug("eventoModificacionParcialPecera");

		PeceraEventDto eventoSalida = patchedPeceraEventToPeceraEventDtoMapper.fromInputToOutput(event);

		producerPeceraEvent.sendMessageAsynch(topicPatchedPecera, eventoSalida);

	}

	@Override
	public void eventoModificacionTotalPecera(@Valid PutPeceraEvent event) {
		log.debug("eventoModificacionTotalPecera");

		PeceraEventDto eventoSalida = putPeceraEventToPeceraEventDtoMapper.fromInputToOutput(event);

		producerPeceraEvent.sendMessageAsynch(topicPutPecera, eventoSalida);
	}

	@Override
	public void eventoEliminacionPecera(@Valid DeletedPeceraEvent event) {
		log.debug("eventoEliminacionPecera");

		PeceraEventDto eventoSalida = deletedPeceraEventToPeceraEventDtoMapper.fromInputToOutput(event);

		producerPeceraEvent.sendMessageAsynch(topicDeletedPecera, eventoSalida);
	}

}
