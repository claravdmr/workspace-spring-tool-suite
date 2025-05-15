package com.mongo.apirest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mongo.apirest.dto.MemberDto;
import com.mongo.database.entity.MemberEntity;
import com.mongo.database.repository.MemberRepository;
import com.mongo.database.repository.ProjectRepository;
import com.mongo.database.repository.TaskRepository;
import com.mongo.utils.Utilities;

@RestController
@RequestMapping("projects/{project-id}/tasks/{task-id}/members")
public class MemberController {

	@Autowired
	MemberRepository memberRepository;
	
	@Autowired
	ProjectRepository projectRepository;
	
	@Autowired
	TaskRepository taskRepository;
	
	
	
	@GetMapping
	public ResponseEntity<List<MemberDto>> getMembers(@PathVariable("project-id") String projectId, @PathVariable("task-id") String taskId){
		
		if (!projectRepository.existsById(projectId) || !taskRepository.existsById(taskId)) {
			return ResponseEntity.badRequest().build();     //returns bad request if either project or task associated does not exist. returns boolean.
		}
		
		List<MemberEntity> savedList = memberRepository.findByProjectIdAndTaskId(projectId, taskId);
		
		List<MemberDto> returnList = savedList.stream().map(member -> {
			MemberDto returnMember = new MemberDto();
			returnMember.setId(member.getId());
			returnMember.setName(member.getName());
			return returnMember;
		}).toList();
		
		return ResponseEntity.ok(returnList);
		
	}
	
	@PostMapping
	public ResponseEntity<Void> createMember(@RequestBody MemberDto member, @PathVariable("project-id") String projectId, @PathVariable("task-id") String taskId){
		
		if (!projectRepository.existsById(projectId) || !taskRepository.existsById(taskId)) {
			return ResponseEntity.badRequest().build();     //returns bad request if either project or task associated does not exist. returns boolean.
		}
		
		MemberEntity saveMember = new MemberEntity();
		
		saveMember.setName(member.getName());
		saveMember.setProjectId(projectId);
		saveMember.setTaskId(taskId);
		
		MemberEntity savedMember = memberRepository.save(saveMember);
		
		return ResponseEntity.created(Utilities.createUri(savedMember.getId())).build();

		
	}
}
