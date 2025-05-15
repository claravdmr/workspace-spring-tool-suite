package arq_hex_projects_members.infrastructure.database.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("MEMBERS")
public class MemberEntity {
	
	@Id
	String id;
	String name;
	String dni;
	String projectId;

}
