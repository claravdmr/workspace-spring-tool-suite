
package com.mongo.apirest.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mongo.apirest.dto.TaskDto;
import com.mongo.apirest.dto.TaskState;
import com.mongo.database.entity.ProjectEntity;
import com.mongo.database.entity.TaskEntity;
import com.mongo.database.repository.ProjectRepository;
import com.mongo.database.repository.TaskRepository;
import com.mongo.utils.Utilities;

@RestController
@RequestMapping("/projects/{project-id}/tasks")
public class TaskController {

	@Autowired
	TaskRepository taskRepository;

	@Autowired
	ProjectRepository projectRepository;

	// see ProjectController description
	@GetMapping
	public ResponseEntity<List<TaskDto>> getTasks(@PathVariable("project-id") String projectId) {

		List<TaskEntity> dbList = taskRepository.findByProjectId(projectId); // this and the params ensures that the method only shows us the tasks associated to a specific project.
		List<TaskDto> saveList = new ArrayList<>();

		for (TaskEntity entity : dbList) {

			TaskDto saveTask = new TaskDto();

			saveTask.setId(entity.getId());
			saveTask.setName(entity.getName());
			// saveTask.setState(entity.getState()); --> this fails as taskEntity has state as string and taskDto has state as an enum.

			if (TaskState.validateValue(entity.getState())) {    // if the state received is not a state in the enum it will be set to null.
				saveTask.setState(TaskState.valueOf(entity.getState()));
			} else {
				saveTask.setState(null);
			}

			saveList.add(saveTask);
		}

		if (!saveList.isEmpty()) {
			return ResponseEntity.ok(saveList);
		}

		return ResponseEntity.noContent().build();

	}


	@PostMapping                           // path variable as above in request mapping
	public ResponseEntity<Void> createTask(@PathVariable("project-id") String projectId, @RequestBody TaskDto tdto) {

		Optional<ProjectEntity> savedProject = projectRepository.findById(projectId);

		if (savedProject.isEmpty()) {
			return ResponseEntity.badRequest().build();
		}


		TaskEntity saveTask = new TaskEntity();

		saveTask.setName(tdto.getName());
		saveTask.setState(tdto.getState().name()); // the .name() creates the enum to a string so it can be saved to the TaskEntity which has state as a string.
		saveTask.setProjectId(projectId);

		TaskEntity savedTask = taskRepository.save(saveTask); // we need a new variable for the saved task which will have 'filled in / generated' any additional attributes such as the id.

		return ResponseEntity.created(Utilities.createUri(savedTask.getId())).build();

	}


}
