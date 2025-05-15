package arq_hex_projects_members.infrastructure.database.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import arq_hex_projects_members.application.ports.MemberRepositoryOutputPort;
import arq_hex_projects_members.domain.model.Member;
import arq_hex_projects_members.infrastructure.database.entity.MemberEntity;
import arq_hex_projects_members.infrastructure.database.mapper.MemberToMemberEntityMapper;
import arq_hex_projects_members.infrastructure.database.repository.MemberRepository;

//this can be considered the adapter to the port

@Component
public class MemberRepositoryService implements MemberRepositoryOutputPort{
	
	@Autowired
	MemberRepository memberRepository;
	
	@Autowired
	MemberToMemberEntityMapper memberToMemberEntityMapper;

	@Override
	public String createMember(Member member) {
		
		MemberEntity me = memberToMemberEntityMapper.map(member);
		
		MemberEntity newMe = memberRepository.save(me);
		
		// we get the id as we will need this for the other api rest methods e.g. post
		return newMe.getId();
	}

	@Override
	public Optional<Member> getMember(String memberId) {
		
		//we find the memberEntity with the id in the database (if exists) and assign this to a memberEntity variable.
		//we then transofrm it to a member domain which the domain reads via the port.
		Optional<MemberEntity> me = memberRepository.findById(memberId);
		
		return memberToMemberEntityMapper.map(me);
	}

	@Override
	public void modifyMember(Member member) {
		//the method before this already checks if it exists or not.
		memberRepository.save(memberToMemberEntityMapper.map(member));
		
	}

}
