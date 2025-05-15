package com.jpa.apirest.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jpa.apirest.dto.PostRoleDto;
import com.jpa.apirest.dto.PostUserDto;
import com.jpa.apirest.dto.RoleReponseDto;
import com.jpa.apirest.dto.UserReponseDto;
import com.jpa.database.entity.Role;
import com.jpa.database.entity.User;
import com.jpa.database.service.UserManagerService;

@RestController
public class Controller {
	
	@Autowired
	UserManagerService service;
	
	@PostMapping("/users")
	public ResponseEntity<Void> createUser(@RequestBody PostUserDto userEntry){ 
		
		
		//this limits the attributes that the user can send through the front
		//and then here we can check their info and assign the remaining info that we want.
		
		User userRepo = new User();
		userRepo.setName(userEntry.getName());
		userRepo.setEmail(userEntry.getEmail());
		Long userId = service.createUser(userRepo);
		return ResponseEntity.noContent().build();
	}
	
	
	
	@PutMapping("/users/{user-id}")
	public ResponseEntity<Void> updateUser(
			@PathVariable("user-id") Long userId,
			@RequestBody PostUserDto userEntry){ 
		
		
		
		User userRepo = new User();			// we create a new user and when the user is saved through the service, if there is an id it will update this user, otherwise it will recognise it as new user.
		
		userRepo.setId(userId);
		userRepo.setName(userEntry.getName());
		userRepo.setEmail(userEntry.getEmail());
		
		boolean hasUpdated = service.updateUser(userRepo);
		
		if (hasUpdated) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
		
	}
	
	
	
	@PatchMapping("/users/{user-id}")
	public ResponseEntity<Void> updatePatialUser(
			@PathVariable("user-id") Long userId,
			@RequestBody PostUserDto userEntry){ 
		
		
		User userRepo = new User();
		
		userRepo.setId(userId);
		
		if (userEntry.getName() != null) {
			userRepo.setName(userEntry.getName());
		}
		
		if (userEntry.getEmail() != null) {
			userRepo.setEmail(userEntry.getEmail());
		}
		
		boolean hasUpdated = service.updateUser(userRepo); // the updateUser method checks the existence of the user 
		
		if (hasUpdated) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	
	
	//below, when we show a user to the client we will show just the attributes we want them to see, hence the creation of the UserResponseDto class

	@GetMapping("/users/{user-id}")
	public ResponseEntity<UserReponseDto> getUser(@PathVariable("user-id") Long userId){ 
	
		Optional<User> userOpt = service.getUser(userId);
		
		if (userOpt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		UserReponseDto userToShow = new UserReponseDto();
		userToShow.setId(userId);
		userToShow.setEmail(userOpt.get().getEmail()); //the first get returns what is encapsulated in the Optional, in this case a UserReponseDto, and from here we can access the UserReponseDto methods.
		userToShow.setName(userOpt.get().getName());
		
		return ResponseEntity.ok(userToShow);
		
		
		
	}
	
	
	
	@GetMapping("/users")
	public ResponseEntity<List<UserReponseDto>> getUsers(){
		
		List<User> savedList = service.getUsers();
		
		List<UserReponseDto> exitList = savedList.stream()
				.map(user -> {
					UserReponseDto newUser = new UserReponseDto();
					newUser.setId(user.getId());
					newUser.setName(user.getName());
					newUser.setEmail(user.getEmail());
					return newUser;
				}).toList();
		
		return ResponseEntity.ok(exitList);
	}
	
	
	
	@DeleteMapping("/users/{user-id}")
	public ResponseEntity<Void> deleteUser(@PathVariable("user-id") Long userId){ 
		
		service.deleteUser(userId);
		
		return ResponseEntity.noContent().build();
	}
	
	
	
	@PostMapping("/users/{user-id}/roles")
	public ResponseEntity<Void> createRole(
			@PathVariable("user-id") Long userId,
			@RequestBody PostRoleDto postRoleDto){
		
		Role newRole = new Role();
		newRole.setName(postRoleDto.getName());
		newRole.setDescription(postRoleDto.getDescription());
		newRole.setUserId(userId);
		service.createRole(newRole);
		
		return ResponseEntity.noContent().build();
	}
	
	
	
	@GetMapping("/roles")
	public ResponseEntity<List<RoleReponseDto>> getRoles(){
		
		List<Role> savedList = service.getRoles();
		List<RoleReponseDto> result = savedList.stream()
				.map(role -> {
					RoleReponseDto newObj = new RoleReponseDto();
					newObj.setId(role.getId());
					newObj.setName(role.getName());
					newObj.setDescription(role.getDescription());
					return newObj;
				}).toList();
		
		return ResponseEntity.ok(result);
	}
	
	
	
	@GetMapping("/users/{user-id}/roles")
	public ResponseEntity<List<RoleReponseDto>> getRolesByUser(@PathVariable ("user-id") Long userId){
		
		List<Role> savedList = service.getRolesByUser(userId);
		List<RoleReponseDto> result = savedList.stream()
				.map(role -> {
					RoleReponseDto newObj = new RoleReponseDto();
					newObj.setId(role.getId());
					newObj.setName(role.getName());
					newObj.setDescription(role.getDescription());
					return newObj;
				}).toList();
		
		return ResponseEntity.ok(result);
	}

}
















