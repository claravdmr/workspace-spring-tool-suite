package arq_hex_parking_access.infrastructure.database.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import arq_hex_parking_access.application.ports.output.ParkingRepositoryOutputPort;
import arq_hex_parking_access.domain.model.Entry;
import arq_hex_parking_access.domain.query.GetCarsQuery;
import arq_hex_parking_access.infrastructure.database.entity.EntryEntity;
import arq_hex_parking_access.infrastructure.database.mapper.EntryEntityMapper;
import arq_hex_parking_access.infrastructure.database.repository.EntryRepository;

@Component
public class ParkingRepositoryService implements ParkingRepositoryOutputPort{
	
	@Autowired
	EntryRepository repository;
	
	@Autowired
	EntryEntityMapper mapper;

	@Override
	public String createEntry(Entry entry) {
		
		EntryEntity savedEntry = repository.save(mapper.map(entry));
		
		return savedEntry.getId();
	}

	@Override
	public List<String> getCars(GetCarsQuery query) {
		
		List<EntryEntity> result = repository.findByExitDateTime(null); //null as we want the cars that are still in the parking
		
		return result.stream().map(
			car -> car.getRegistration())
			.toList(); // converts list of EntryEntitys above in line 36 into a list of the registration of each EntryEntity
	}

	@Override
	public Optional<Entry> getEntry(String entryId) {
		
		Optional<EntryEntity> result = repository.findById(entryId);
		
		if (result.isEmpty()) {
			return Optional.empty();
		}
		
		return mapper.map(result);
	}

	@Override
	public Optional<Entry> getEntryByRegistration(String registration) {
		
		
		List<EntryEntity> result = repository.findByRegistrationAndExitDateTime(registration, null);
		
		if (result.isEmpty()) {
			return Optional.empty();
		}
		
		return Optional.ofNullable(mapper.map(result.get(0)));
	}

	@Override
	public void updateEntry(Entry entry) {
		
		repository.save(mapper.map(entry));
;	}

}
