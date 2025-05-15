package com.jpa.database.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jpa.database.entity.Role;
import com.jpa.database.entity.User;
import com.jpa.database.repository.PermissionRepository;
import com.jpa.database.repository.RoleRepository;
import com.jpa.database.repository.UserRepository;

@Service
public class UserManagerService {
	
	//access to all the repositories
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	PermissionRepository permissionRepository;
	
	public Long createUser(User user) {
		User savedUser = userRepository.save(user);
		System.out.println(savedUser.getId());
		return savedUser.getId(); // this is so we can access the user later using the id.
	}
	
	public boolean updateUser(User user) {
		
		
		Optional<User>userOpt = getUser(user.getId()); //this checks that the user id exists.
		
		if (userOpt.isEmpty()) {
			return false;
		}
		
		if (user.getName() == null) {
			user.setName(userOpt.get().getName());
		}
		
		if (user.getEmail() == null) {
			user.setEmail(userOpt.get().getEmail());
		}
		
		userRepository.save(user);
		return true;
	}
	
	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}
	
	public Optional<User> getUser(Long id){ //optional means it can come back void.
		return userRepository.findById(id);
	}

	public List<User> getUsers() {
		return userRepository.findAll();
	}

	public void createRole(Role newRole) {
		
		Optional<User>userOpt = getUser(newRole.getUserId()); //this checks that the user id exists.
		
		if (userOpt.isEmpty()) {
			return;
		}
		
		roleRepository.save(newRole);
	}
	
	public List<Role> getRoles(){
		return roleRepository.findAll();
	}
	
	public List<Role> getRolesByUser(Long userId){
		return roleRepository.findByUserId(userId);
	}
}
