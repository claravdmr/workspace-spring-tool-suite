package arq_hex_projects_members.infrastructure.database.mapper;

import org.mapstruct.Mapper;

import arq_hex_projects_members.domain.model.Project;
import arq_hex_projects_members.infrastructure.database.entity.ProjectEntity;

@Mapper(componentModel = "spring")
public interface ProjectToProjectEntityMapper {

	ProjectEntity map(Project project);

}
