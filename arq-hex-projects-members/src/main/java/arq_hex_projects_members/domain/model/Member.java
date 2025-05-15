package arq_hex_projects_members.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member {
	
	String id;
	String name;
	String dni;
	String projectId;

}
