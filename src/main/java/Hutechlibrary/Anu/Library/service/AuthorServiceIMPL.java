package Hutechlibrary.Anu.Library.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import Hutechlibrary.Anu.Library.dto.AuthorResponseDTO;
import Hutechlibrary.Anu.Library.dto.DataResponse;
import Hutechlibrary.Anu.Library.entity.Author;
import Hutechlibrary.Anu.Library.exception.ResourceNotFoundException;
import Hutechlibrary.Anu.Library.repository.AuthorRepository;
import jakarta.validation.Valid;



@Service
public class AuthorServiceIMPL implements AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

 
    @Override
    public Author createAuthorEntity(Author author) {
        if (authorRepository.existsByName(author.getName())) {
            throw new RuntimeException("Author already exists with name: " + author.getName());
        }
        author.setRecordedAt(LocalDateTime.now());
        return authorRepository.save(author);
    }


    @Override
    public Page<Author> getAllAuthorsPaginated(Pageable pageable) {
        return authorRepository.findAll(pageable);
    }

   
    @Override
    public Author getAuthorById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));
    }

    @Override
    public Page<Author> searchAuthors(String name, String biography, Pageable pageable) {
        return authorRepository.findByNameContainingIgnoreCaseAndBiographyContainingIgnoreCase(
                name != null ? name : "",
                biography != null ? biography : "",
                pageable
        );
    }

    
    @Override
    public Author updateAuthor(Long id, Author authorDetails) {
        Author author = getAuthorById(id);
        author.setName(authorDetails.getName());
        author.setBiography(authorDetails.getBiography());
        return authorRepository.save(author);
    }

 
    @Override
    public void deleteAuthor(Long id) {
        Author author = getAuthorById(id);
        authorRepository.delete(author);
    }

 
    @Override
    public AuthorResponseDTO convertToDto(Author author) {
        return new AuthorResponseDTO(
                author.getId(),
                author.getName(),
                author.getBiography(),
                author.getBooks(),
                author.getRecordedAt()
        );
    }

}