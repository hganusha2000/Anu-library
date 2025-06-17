package Hutechlibrary.Anu.Library.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Hutechlibrary.Anu.Library.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
	
}
