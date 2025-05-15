package arq_hex_parking_access.infrastructure.event.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import arq_hex_parking_access.application.ports.output.ParkingProducerOutputPort;
import arq_hex_parking_access.domain.model.Entry;
import arq_hex_parking_access.infrastructure.event.producer.EntryProducer;

@Component
public class EntryProducerService implements ParkingProducerOutputPort{
	
	@Autowired
	EntryProducer producer;

	@Override
	public void send(Entry entry) {
		producer.send(entry);
		
	}

}
