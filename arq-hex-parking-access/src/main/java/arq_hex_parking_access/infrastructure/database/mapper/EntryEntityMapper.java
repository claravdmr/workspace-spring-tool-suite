package arq_hex_parking_access.infrastructure.database.mapper;

import java.util.Optional;

import org.mapstruct.Mapper;

import arq_hex_parking_access.domain.model.Entry;
import arq_hex_parking_access.infrastructure.database.entity.EntryEntity;

@Mapper(componentModel = "spring")
public interface EntryEntityMapper {
	
	EntryEntity map(Entry domain);
	
	Entry map(EntryEntity entity);

	default Optional<Entry> map(Optional<EntryEntity> domain) {
		return domain.isEmpty()
		?
		Optional.empty() // this is the way of creating an optional in the absence of new constructor
		:
		Optional.ofNullable(map(domain.get())); // as above this is a 'constructor' and needs to be ofNullable as this is the one that allows a null object and as it is optional it could be null.
	}

	

}
