package org.luizlopes.repositories;

import org.luizlopes.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

	User findByUsername(String username);

}
