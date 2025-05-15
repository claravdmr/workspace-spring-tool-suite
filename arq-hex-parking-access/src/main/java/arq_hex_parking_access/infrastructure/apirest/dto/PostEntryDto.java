package arq_hex_parking_access.infrastructure.apirest.dto;

import lombok.Data;

@Data
public class PostEntryDto {

	byte[] image;
	String registration;
	
}
