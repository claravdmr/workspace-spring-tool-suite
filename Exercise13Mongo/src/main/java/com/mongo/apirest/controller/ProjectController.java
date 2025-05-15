
package com.mongo.apirest.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mongo.apirest.dto.ProjectDto;
import com.mongo.database.entity.ProjectEntity;
import com.mongo.database.repository.ProjectRepository;
import com.mongo.utils.Utilities;

@RestController
@RequestMapping("/projects")
public class ProjectController {


	@Autowired   // so you don't have to use a new repeatedly. It creates 1 new in the background and applies this new to all the instances of @Autowired.
	ProjectRepository projectRepository;

	
	// the below creates a new ProjectDto for each entity in the dbList and maps the database ProjectEntities onto it to retrieve the data and add it to
	// the return list but return it in a way that hides the full attributes of the database projects.
	// could be done quicker with stream mapping feature
	@GetMapping
	public ResponseEntity<List<ProjectDto>> getProjects() {

		List<ProjectEntity> dbList = projectRepository.findAll();
		List<ProjectDto> returnList = new ArrayList<>();

		for (ProjectEntity entity : dbList) {

			ProjectDto returnProject = new ProjectDto();

			returnProject.setId(entity.getId());
			returnProject.setName(entity.getName());
			returnProject.setDescription(entity.getDescription());
			returnProject.setStartDate(entity.getStartDate());
			returnProject.setEndDate(entity.getEndDate());

			returnList.add(returnProject);
		}

		if (!dbList.isEmpty()) {
			return ResponseEntity.ok(returnList);
		}

		return ResponseEntity.noContent().build();
	}
	
	// the below maps the projectDto provided onto a ProjectEntity which is saved to the database and fills in the missing attributes not provided by Dto.
	@PostMapping
	public ResponseEntity<Void> createProject(@RequestBody ProjectDto projectDto){
		
		ProjectEntity dbProject = new ProjectEntity();
		
		dbProject.setName(projectDto.getName());
		dbProject.setDescription(projectDto.getDescription());
		dbProject.setStartDate(projectDto.getStartDate());
		dbProject.setEndDate(projectDto.getEndDate());
		
		// id set to null for insertion in the database to ensure this isn't determined by ProjectDto sent in but rather the database id creation method.
		dbProject.setId(null);   
		dbProject.setCreation(LocalDateTime.now());
		dbProject.setMembers(new ArrayList<>());
		dbProject.setTasks(new ArrayList<>());
		
		ProjectEntity savedProject = projectRepository.save(dbProject);
		
		
		return ResponseEntity.created(Utilities.createUri(savedProject.getId())).build();
	}
	
	
	@PutMapping("/{id}")
	public ResponseEntity<Void> amendProject(@PathVariable("id") String id, @RequestBody ProjectDto pdto){
		
		Optional<ProjectEntity> savedProject = projectRepository.findById(id); // checks project exists in database as projectEntity
		
		if (savedProject.isPresent()) { // opposite of isEmpty
			
			// if project is in database then pdto is mapped onto it and saved, hence amending values within the database. 
			
			savedProject.get().setName(pdto.getName());                    //the first.get is to get the object within the optional.
			savedProject.get().setDescription(pdto.getDescription());
			savedProject.get().setStartDate(pdto.getStartDate());
			savedProject.get().setEndDate(pdto.getEndDate());

			projectRepository.save(savedProject.get());
		}
		
		return ResponseEntity.noContent().build();
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<Void> amendProjectPartial(@PathVariable("id") String id, @RequestBody ProjectDto pdto){
		
		Optional<ProjectEntity> savedProject = projectRepository.findById(id); 
		
		if (savedProject.isPresent()) {
			
			if (pdto.getName() != null) {
				savedProject.get().setName(pdto.getName());   
			}
			
			if (pdto.getDescription() != null) {
				savedProject.get().setDescription(pdto.getDescription());
			}
			
			if (pdto.getStartDate() != null) {
				savedProject.get().setStartDate(pdto.getStartDate());
			}
			
			if (pdto.getEndDate() != null) {
				savedProject.get().setEndDate(pdto.getEndDate());
			}

			projectRepository.save(savedProject.get());

		}

		return ResponseEntity.noContent().build();
	}
	
	
	

}
