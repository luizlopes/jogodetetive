package org.luizlopes.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.luizlopes.entities.User;
import org.luizlopes.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> userOp = Optional.ofNullable(userRepository.findByUsername(username));
		User user = userOp.orElseThrow(() -> new UsernameNotFoundException("Usuario não encontrado"));
		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
		user.getRoles().stream().forEach(role->{
			grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
		});
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
	}

}
