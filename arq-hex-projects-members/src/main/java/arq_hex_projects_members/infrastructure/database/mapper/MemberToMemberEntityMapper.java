package arq_hex_projects_members.infrastructure.database.mapper;

import java.util.Optional;

import org.mapstruct.Mapper;

import arq_hex_projects_members.domain.model.Member;
import arq_hex_projects_members.infrastructure.database.entity.MemberEntity;

@Mapper(componentModel = "spring")
public interface MemberToMemberEntityMapper {

	MemberEntity map(Member member);
	
	Member map(MemberEntity memberEntity);

	default Optional<Member> map(Optional<MemberEntity> member){
		
//		return member.isEmpty() 
//		? 
//		Optional.empty() 
//		:
//		Optional.ofNullable(map(member.get()));
		
		return member.map(this::map);
		//this is the same as the above
	}



	



}
