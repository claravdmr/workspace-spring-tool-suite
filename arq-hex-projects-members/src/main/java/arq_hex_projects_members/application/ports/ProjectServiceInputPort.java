package arq_hex_projects_members.application.ports;

import arq_hex_projects_members.domain.model.Member;
import arq_hex_projects_members.domain.model.Project;

//here are all the methods we want to be able to do with the app, and everything will pass through here in order to access it, using adapters.

public interface ProjectServiceInputPort {

	Project createProject(String projectName);
		
	Member addMember(String projectId, String memberid) throws Exception;
	
	

}
