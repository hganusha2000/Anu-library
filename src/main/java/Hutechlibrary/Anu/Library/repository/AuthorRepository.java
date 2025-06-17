package Hutechlibrary.Anu.Library.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Hutechlibrary.Anu.Library.entity.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}