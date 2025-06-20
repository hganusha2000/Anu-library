package Hutechlibrary.Anu.Library.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import Hutechlibrary.Anu.Library.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByName(String name);

	}
