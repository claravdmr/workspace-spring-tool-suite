package arq_hex_projects_members.application.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import arq_hex_projects_members.application.ports.ProjectRepositoryOutputPort;
import arq_hex_projects_members.application.ports.ProjectServiceInputPort;
import arq_hex_projects_members.domain.model.Member;
import arq_hex_projects_members.domain.model.Project;

@Service
public class ProjectService implements ProjectServiceInputPort{
	
	@Autowired
	ProjectRepositoryOutputPort projectRepository;
	
	@Autowired
	MemberService memberService;
	
	@Override
	//receives project name, creates project and assigns the name, sends the project to be created in x database, takes the id the database generates, 
	//sets it to the project, and returns the project (though create methods don't always have to return something per se). 
	public Project createProject(String projectName) {
		
		Project p = new Project();
		p.setName(projectName);
		// if we had any further default info such as date creation this would be added here.
		
		String id = projectRepository.createProject(p);
		p.setId(id);
		
		return p;
	}

	@Override
	public Member addMember(String projectId, String memberId) throws Exception {
		
		//we use optional when there is a chance that it will return null
		Optional<Member> member = memberService.getMember(memberId);
		if (member.isEmpty()) {
			throw new Exception("Member does not exist.");
		}
		
		member.get().setProjectId(projectId);
		
		memberService.modifyMember(member.get());

		return null;
	}

}
