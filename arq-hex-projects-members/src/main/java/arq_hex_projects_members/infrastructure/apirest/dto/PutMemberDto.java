package arq_hex_projects_members.infrastructure.apirest.dto;

import lombok.Data;

@Data
public class PutMemberDto {
	
	//String id; no id as the id goes in the path variable not the body
	String name;
	String dni;
	
}
