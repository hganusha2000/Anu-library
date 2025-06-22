package Hutechlibrary.Anu.Library.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import Hutechlibrary.Anu.Library.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

	Optional<Book> findByIsbn(String isbn);
	
	Page<Book> findByTitleContainingIgnoreCaseAndIsbnContainingIgnoreCaseAndAuthor_NameContainingIgnoreCase(
	        String title, String isbn, String authorName, Pageable pageable
	    );
	
}
