package arq_hex_projects_members.infrastructure.apirest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import arq_hex_projects_members.domain.model.Member;
import arq_hex_projects_members.infrastructure.apirest.dto.PutMemberDto;

//the unmapped target policy is so that if there are any unmapped properties, in the first below the id and the project id, then it will ignore the warning. 

@Mapper (componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberToPutMemberDto {
	
	Member map(PutMemberDto dto);

}
