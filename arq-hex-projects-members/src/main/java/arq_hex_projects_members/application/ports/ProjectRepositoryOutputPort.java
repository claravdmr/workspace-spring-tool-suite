package arq_hex_projects_members.application.ports;

import arq_hex_projects_members.domain.model.Project;

public interface ProjectRepositoryOutputPort {

	String createProject(Project project);

}
