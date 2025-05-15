package arq_hex_parking_access.infrastructure.database.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import arq_hex_parking_access.infrastructure.database.entity.EntryEntity;

@Repository
public interface EntryRepository extends MongoRepository<EntryEntity, String>{
	
	List<EntryEntity> findByExitDateTime(LocalDateTime exitDateTime);
	
	List<EntryEntity> findByRegistrationAndExitDateTime(String registration, LocalDateTime ExitDateTime); //the exitdatetime ensures the car is still there and it wont show us all the times the car has ever been there even if it has left.
	// this has to be a list in the event that more than one car is found and it breaks the process. in service where this is implemented the method will return a singular optional and we should really have checked in the service method if more than 1 is returned. 

}
