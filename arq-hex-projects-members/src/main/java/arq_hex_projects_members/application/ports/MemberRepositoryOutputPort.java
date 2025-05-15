package arq_hex_projects_members.application.ports;

import java.util.Optional;

import arq_hex_projects_members.domain.model.Member;

public interface MemberRepositoryOutputPort {

	String createMember(Member member);

	Optional<Member> getMember(String memberId);

	void modifyMember(Member member);

	

}
