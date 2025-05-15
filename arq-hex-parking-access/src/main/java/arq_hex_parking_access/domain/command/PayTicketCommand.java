package arq_hex_parking_access.domain.command;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class PayTicketCommand {
	
	String entryId;

}
