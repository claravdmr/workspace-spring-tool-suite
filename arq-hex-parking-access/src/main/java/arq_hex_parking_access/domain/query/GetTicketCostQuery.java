package arq_hex_parking_access.domain.query;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetTicketCostQuery {
	
	String entryId;

}
