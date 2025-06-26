package Hutechlibrary.Anu.Library.repository;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import Hutechlibrary.Anu.Library.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
	Optional<User> findByEmail(String email);
	Optional<User> findByActivationToken(String token);
	@Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.id = :id")
	Optional<User> findByIdWithRoles(@Param("id") Long id);
	Optional<User> findByResetToken(String token);
	
}