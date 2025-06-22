package Hutechlibrary.Anu.Library.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import Hutechlibrary.Anu.Library.dto.DataResponse;
import Hutechlibrary.Anu.Library.entity.Book;
import Hutechlibrary.Anu.Library.repository.BookRepository;
import Hutechlibrary.Anu.Library.exception.ResourceNotFoundException;


@Service

public class BookServiceIMPL implements BookService {
	
	@Autowired
    private BookRepository bookRepository;
	
	 @Override
	    public DataResponse createBook(Book book) {
	        Optional<Book> existingBook = bookRepository.findByIsbn(book.getIsbn());
	        if (existingBook.isPresent()) {
	            return new DataResponse(
	                HttpStatus.CONFLICT.value(),
	                "Book with this ISBN already exists",
	                null
	            );
	        }

	        Book savedBook = bookRepository.save(book);
	        return new DataResponse(
	            HttpStatus.CREATED.value(),
	            "Book created successfully",
	            savedBook
	        );
	    }

	 @Override
	 public Page<Book> getAllBooksPaginated(Pageable pageable) {
	     return bookRepository.findAll(pageable);
	 }

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }
    
    @Override
    public Page<Book> searchBooks(String title, String isbn, String authorName, Pageable pageable) {
        title = title != null ? title : "";
        isbn = isbn != null ? isbn : "";
        authorName = authorName != null ? authorName : "";

        return bookRepository.findByTitleContainingIgnoreCaseAndIsbnContainingIgnoreCaseAndAuthor_NameContainingIgnoreCase(
                title, isbn, authorName, pageable
        );
    }


    public Book updateBook(Long id, Book bookDetails) {
        Book book = getBookById(id);
        book.setTitle(bookDetails.getTitle());
        book.setIsbn(bookDetails.getIsbn());
        book.setAuthor(bookDetails.getAuthor());
        book.setPublisher(bookDetails.getPublisher());
        book.setPublicationYear(bookDetails.getPublicationYear());
        book.setTotalCopies(bookDetails.getTotalCopies());
        book.setAvailableCopies(bookDetails.getAvailableCopies());
        return bookRepository.save(book);
    }
    public void deleteBook(Long id) {
        Book book = getBookById(id);
        bookRepository.delete(book);
    }
}

