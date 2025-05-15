package arq_hex_parking_access.infrastructure.apirest.dto;

import lombok.Data;

@Data
public class ValidateExitDto {
	
	String registration;
	byte[] exitImage;

}
