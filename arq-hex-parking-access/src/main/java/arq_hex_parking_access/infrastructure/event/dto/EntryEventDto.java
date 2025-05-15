package arq_hex_parking_access.infrastructure.event.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EntryEventDto {
	
	String id;
	String registration;
	String contentManagerImageId;
	boolean paid;
	LocalDateTime paidDateTime;
	LocalDateTime entryDateTime;
	LocalDateTime exitDateTime;

}
