package arq_hex_parking_access.domain.query;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetCarsQuery {
	
	String registration;
	LocalDateTime entryDateTime;

}
