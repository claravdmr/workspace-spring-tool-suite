package com.demo.arq.application.port.output;

import com.demo.arq.domain.model.Pecera;
import jakarta.validation.Valid;

public interface PeceraProducerOutputPort {
	public void eventoCreacionPecera(@Valid Pecera input);
	public void eventoModificacionParcialPecera(@Valid Pecera input);
	public void eventoModificacionTotalPecera(@Valid Pecera input);
	public void eventoEliminacionPecera(@Valid Pecera input);
}
