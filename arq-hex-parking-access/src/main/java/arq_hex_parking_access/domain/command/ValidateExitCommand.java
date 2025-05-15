package arq_hex_parking_access.domain.command;

import lombok.Data;

@Data
public class ValidateExitCommand {
	
	String registration;
	byte[] exitImage;

}
