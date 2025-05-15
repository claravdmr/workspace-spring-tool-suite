package arq_hex_projects_members.application.ports;

import java.util.Optional;

import arq_hex_projects_members.domain.model.Member;

//here are all the methods we want to be able to do with the app, and everything will pass through here in order to access it, using adapters.

public interface MemberServiceInputPort {
	
	Member createMember(String name, String dni);

	Optional<Member> getMember(String memberId);

	void modifyMember(Member member) throws Exception;

}
