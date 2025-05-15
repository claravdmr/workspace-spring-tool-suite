package arq_hex_parking_access.domain.command;

import lombok.Data;

@Data
public class CreateEntryCommand {
	
	byte[] image; // this will soon be saved to external location and the id saved within the database i.e. why the Entry class only has the image storage id.
	String registation;

}