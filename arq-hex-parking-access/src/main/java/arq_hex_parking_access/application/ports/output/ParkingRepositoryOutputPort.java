package arq_hex_parking_access.application.ports.output;

import java.util.List;
import java.util.Optional;

import arq_hex_parking_access.domain.model.Entry;
import arq_hex_parking_access.domain.query.GetCarsQuery;

public interface ParkingRepositoryOutputPort {

	String createEntry(Entry entry);

	List<String> getCars(GetCarsQuery query);

	Optional<Entry> getEntry(String entryId);

	Optional<Entry> getEntryByRegistration(String registration);

	void updateEntry(Entry entry);

}
