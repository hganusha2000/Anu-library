package Hutechlibrary.Anu.Library.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import Hutechlibrary.Anu.Library.dto.AuthorResponseDTO;
import Hutechlibrary.Anu.Library.dto.DataResponse;
import Hutechlibrary.Anu.Library.entity.Author;
import jakarta.validation.Valid;

public interface AuthorService {


    Author createAuthorEntity(Author author);
    
    Page<Author> getAllAuthorsPaginated(Pageable pageable);
    
    Author getAuthorById(Long id);
    
    Page<Author> searchAuthors(String name, String biography, Pageable pageable);
    
    Author updateAuthor(Long id, Author authorDetails);
    
    void deleteAuthor(Long id);
    
    AuthorResponseDTO convertToDto(Author author);
	 
	    
	}

