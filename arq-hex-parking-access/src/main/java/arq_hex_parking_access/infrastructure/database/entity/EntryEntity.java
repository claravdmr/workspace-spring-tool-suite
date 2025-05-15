package arq_hex_parking_access.infrastructure.database.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document("ENTRIES")
public class EntryEntity {
	
	@Id
	String id;
	String registration;
	String contentManagerImageId;
	boolean paid;
	LocalDateTime paidDateTime;
	LocalDateTime entryDateTime;
	LocalDateTime exitDateTime;

}
