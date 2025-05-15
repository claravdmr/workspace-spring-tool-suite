package arq_hex_projects_members.application.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import arq_hex_projects_members.application.ports.MemberRepositoryOutputPort;
import arq_hex_projects_members.application.ports.MemberServiceInputPort;
import arq_hex_projects_members.domain.model.Member;

@Service
public class MemberService implements MemberServiceInputPort{
	
	@Autowired
	MemberRepositoryOutputPort memberRepository;

	@Override
	public Member createMember(String name, String dni) {
		
		Member m = new Member();
		m.setName(name);
		m.setDni(dni);
		// if we had any further default info such as date creation this would be added here.
		
		String id = memberRepository.createMember(m);
		m.setId(id);
		
		return m;

	}

	@Override
	public Optional<Member> getMember(String memberId) {
		return memberRepository.getMember(memberId);
	}

	@Override
	public void modifyMember(Member member) throws Exception {
		
		// this new variable below is created in this case purely to check if the member passed in the parameters exists.
		// it could also have been done through if(!memberRepository.memberExists(member.getId()) for example and the method in the repository would have been a boolean
		// the above would have removed the need for the new variable
		
		Optional<Member> m = getMember(member.getId());
		
		if(m.isEmpty()) {
			throw new Exception("Member does not exist");
		}
		
		memberRepository.modifyMember(member);
		
	}
	
	
	
	
	
}
