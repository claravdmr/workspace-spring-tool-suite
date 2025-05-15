package arq_hex_projects_members.infrastructure.apirest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
	
	String id;
	String name;
	String dni;
	String projectId;

}
