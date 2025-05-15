package arq_hex_projects_members.infrastructure.database.service;

import org.springframework.beans.factory.annotation.Autowired;

import arq_hex_projects_members.application.ports.ProjectRepositoryOutputPort;
import arq_hex_projects_members.domain.model.Project;
import arq_hex_projects_members.infrastructure.database.entity.ProjectEntity;
import arq_hex_projects_members.infrastructure.database.mapper.ProjectToProjectEntityMapper;
import arq_hex_projects_members.infrastructure.database.repository.ProjectRepository;

public class ProjectRepositoryService implements ProjectRepositoryOutputPort{

	@Autowired
	ProjectRepository projectRepository;
	
	@Autowired
	ProjectToProjectEntityMapper projectToProjectEntityMapper;
	
	@Override
	public String createProject(Project project) {
		
		ProjectEntity pe = projectToProjectEntityMapper.map(project);
		
		ProjectEntity newPe = projectRepository.save(pe);
		
		// we get the id as we will need this for the other api rest methods e.g. post
		return newPe.getId();
		
	}

}
