package Hutechlibrary.Anu.Library.repository;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;

import Hutechlibrary.Anu.Library.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
	Optional<User> findByEmail(String email);
	Optional<User> findByActivationToken(String token);
	
}