package com.demo.arq.application.port.output;

import com.demo.arq.domain.event.DeletedPeceraEvent;
import com.demo.arq.domain.event.PatchedPeceraEvent;
import com.demo.arq.domain.event.PostedPeceraEvent;
import com.demo.arq.domain.event.PutPeceraEvent;

import jakarta.validation.Valid;

public interface PeceraProducerOutputPort {
	public void eventoCreacionPecera(@Valid PostedPeceraEvent event);
	public void eventoModificacionParcialPecera(@Valid PatchedPeceraEvent event);
	public void eventoModificacionTotalPecera(@Valid PutPeceraEvent event);
	public void eventoEliminacionPecera(@Valid DeletedPeceraEvent event);
}
