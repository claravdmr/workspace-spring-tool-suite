package arq_hex_projects_members.infrastructure.apirest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mongo.utils.Utilities;

import arq_hex_projects_members.application.ports.ProjectServiceInputPort;
import arq_hex_projects_members.domain.model.Project;
import arq_hex_projects_members.infrastructure.apirest.dto.PostProjectDto;
import arq_hex_projects_members.infrastructure.apirest.dto.PutProjectDto;

@RestController
@RequestMapping("/projects")
public class ProjectController {

	@Autowired
	ProjectServiceInputPort service;

	@PostMapping
	public ResponseEntity postProject(@RequestBody PostProjectDto projectDto) {

		Project project = service.createProject(projectDto.getName());

		Utilities.createUri(project.getId());

		return ResponseEntity.noContent().build();

	}

	@PutMapping("/{project-id}")

	public ResponseEntity putProject(
			@PathVariable("project-id") String projectId,
			@RequestBody PutProjectDto projectDto) {

		try {

			service.addMember(projectId, projectDto.getMemberId());

		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}

		return ResponseEntity.noContent().build();

	}

}
