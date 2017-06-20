package org.luizlopes.service;

import org.luizlopes.domain.UserRequest;
import org.luizlopes.entities.Role;
import org.luizlopes.entities.User;
import org.luizlopes.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	public User create(UserRequest newUser){
		User user = new User();
		Role role = new Role();
		role.setName("ADMIN");
		user.getRoles().add(role);
		user.setUsername(newUser.getUsername());
		user.setPassword(encoder.encode(newUser.getPassword()));
		userRepository.save(user);
		return user;
	}
	
}
