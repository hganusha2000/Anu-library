package Hutechlibrary.Anu.Library.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import Hutechlibrary.Anu.Library.entity.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {

	boolean existsByName(String name);
	Page<Author> findByNameContainingIgnoreCaseAndBiographyContainingIgnoreCase(
	        String name, String biography, Pageable pageable
	    );
}