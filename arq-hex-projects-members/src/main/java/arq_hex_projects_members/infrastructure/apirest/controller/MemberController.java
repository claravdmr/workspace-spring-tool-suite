package arq_hex_projects_members.infrastructure.apirest.controller;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mongo.utils.Utilities;

import arq_hex_projects_members.application.ports.MemberServiceInputPort;
import arq_hex_projects_members.domain.model.Member;
import arq_hex_projects_members.infrastructure.apirest.dto.PostMemberDto;
import arq_hex_projects_members.infrastructure.apirest.dto.PutMemberDto;
import arq_hex_projects_members.infrastructure.apirest.mapper.MemberToMemberDto;
import arq_hex_projects_members.infrastructure.apirest.mapper.MemberToPutMemberDto;

@RestController
@RequestMapping("/members")
public class MemberController {
	
	@Autowired
	MemberServiceInputPort service;
	
	@Autowired
	MemberToPutMemberDto memberToPutMemberDtoMapper;
	
	@Autowired
	MemberToMemberDto memberToMemberDtoMapper;
	
	@PostMapping
	public ResponseEntity postMember(@RequestBody PostMemberDto memberDto) {
		
		//map if necessary

		Member member = service.createMember(memberDto.getName(), memberDto.getDni());

		URI uri = Utilities.createUri(member.getId());

		return ResponseEntity.created(uri).build();

	}
	
	@PutMapping("/{member-id}")
	public ResponseEntity putMember(
			@PathVariable("member-id") String memberId,
			@RequestBody PutMemberDto memberDto) {
		//
		
		Member memberDomain = memberToPutMemberDtoMapper.map(memberDto);
		memberDomain.setId(memberId);

		try {

			service.modifyMember(memberDomain); // this is the name of the variable but it is to be of type Member - the name is just for ease of reference

		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}

		return ResponseEntity.noContent().build();

	}
	
	@GetMapping("/{member-id}")
	public ResponseEntity getMember(@PathVariable("member-id") String memberId) {
		
		Optional<Member> memberDomain = service.getMember(memberId);
		
		if (memberDomain.isPresent()) {
			return ResponseEntity.ok(memberToMemberDtoMapper.map(memberDomain.get()));
		}
		
		return ResponseEntity.noContent().build();
		
	}
	
	

}
