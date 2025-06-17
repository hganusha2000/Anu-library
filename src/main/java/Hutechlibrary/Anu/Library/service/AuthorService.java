package Hutechlibrary.Anu.Library.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import Hutechlibrary.Anu.Library.dto.DataResponse;
import Hutechlibrary.Anu.Library.entity.Author;
import jakarta.validation.Valid;

public interface AuthorService {
	

	DataResponse createAuthor(@Valid Author author);

	Author updateAuthor(Long id, Author authorDetails);

	Author getAuthorById(Long id);

	void deleteAuthor(Long id);

	Page<Author> getAllAuthorsPaginated(Pageable pageable);
	 
	    
	}

