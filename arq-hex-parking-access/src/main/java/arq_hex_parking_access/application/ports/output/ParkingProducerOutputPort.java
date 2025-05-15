package arq_hex_parking_access.application.ports.output;

import arq_hex_parking_access.domain.model.Entry;

public interface ParkingProducerOutputPort {

	void send(Entry entry);

}
