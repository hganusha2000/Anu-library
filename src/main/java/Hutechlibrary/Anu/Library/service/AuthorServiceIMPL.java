package Hutechlibrary.Anu.Library.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import Hutechlibrary.Anu.Library.dto.DataResponse;
import Hutechlibrary.Anu.Library.entity.Author;
import Hutechlibrary.Anu.Library.exception.ResourceNotFoundException;
import Hutechlibrary.Anu.Library.repository.AuthorRepository;



@Service
public class AuthorServiceIMPL implements AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public DataResponse createAuthor(Author author) {
        if (authorRepository.existsByName(author.getName())) {
            return new DataResponse(HttpStatus.CONFLICT.value(), "Author already exists", null);
        }

        author.setRecordedAt(LocalDateTime.now()); // Set timestamp manually
        Author savedAuthor = authorRepository.save(author);
        return new DataResponse(HttpStatus.CREATED.value(), "Author created successfully", savedAuthor);
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
    public Author updateAuthor(Long id, Author authorDetails) {
        Author author = getAuthorById(id);
        author.setName(authorDetails.getName());
        author.setBiography(authorDetails.getBiography());
        return authorRepository.save(author);
    }

    @Override
    public void deleteAuthor(Long id) {
        Author author = getAuthorById(id);
        author.setDeleted(true);
        authorRepository.save(author); //soft delete
    }
}