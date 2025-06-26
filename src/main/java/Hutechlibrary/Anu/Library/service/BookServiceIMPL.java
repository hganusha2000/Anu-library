package Hutechlibrary.Anu.Library.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import Hutechlibrary.Anu.Library.dto.BookResponseDTO;
import Hutechlibrary.Anu.Library.dto.DataResponse;
import Hutechlibrary.Anu.Library.entity.Author;
import Hutechlibrary.Anu.Library.entity.Book;
import Hutechlibrary.Anu.Library.entity.Library;
import Hutechlibrary.Anu.Library.entity.Publisher;
import Hutechlibrary.Anu.Library.repository.AuthorRepository;
import Hutechlibrary.Anu.Library.repository.BookRepository;
import Hutechlibrary.Anu.Library.repository.LibraryRepository;
import Hutechlibrary.Anu.Library.repository.PublisherRepository;
import jakarta.persistence.criteria.Predicate;
import Hutechlibrary.Anu.Library.exception.ResourceNotFoundException;



@Service
public class BookServiceIMPL implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private LibraryRepository libraryRepository;

    @Override
    public Book createBook(Book book) {
        Optional<Book> existing = bookRepository.findByIsbn(book.getIsbn());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Book with this ISBN already exists");
        }

        // Fetch and set the full Author, Publisher, Library entities by ID
        if (book.getAuthor() != null && book.getAuthor().getId() != null) {
            Author author = authorRepository.findById(book.getAuthor().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Author not found with ID: " + book.getAuthor().getId()));
            book.setAuthor(author);
        }

        if (book.getPublisher() != null && book.getPublisher().getId() != null) {
            Publisher publisher = publisherRepository.findById(book.getPublisher().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Publisher not found with ID: " + book.getPublisher().getId()));
            book.setPublisher(publisher);
        }

        if (book.getLibrary() != null && book.getLibrary().getId() != null) {
            Library library = libraryRepository.findById(book.getLibrary().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Library not found with ID: " + book.getLibrary().getId()));
            book.setLibrary(library);
        }

        return bookRepository.save(book);
    }

    @Override
    public Page<Book> getAllBooksPaginated(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }

    @Override
    public Page<Book> searchBooks(String title, String isbn, String authorName, Pageable pageable) {
        return bookRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (title != null && !title.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
            }

            if (isbn != null && !isbn.isEmpty()) {
                predicates.add(cb.equal(root.get("isbn"), isbn));
            }

            if (authorName != null && !authorName.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("author").get("name")), "%" + authorName.toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);
    }

    @Override
    public Book updateBook(Long id, Book bookDetails) {
        Book book = getBookById(id);

        book.setTitle(bookDetails.getTitle());
        book.setIsbn(bookDetails.getIsbn());
        book.setPublicationYear(bookDetails.getPublicationYear());
        book.setTotalCopies(bookDetails.getTotalCopies());
        book.setAvailableCopies(bookDetails.getAvailableCopies());

        // Safely set related entities
        if (bookDetails.getAuthor() != null && bookDetails.getAuthor().getId() != null) {
            Author author = authorRepository.findById(bookDetails.getAuthor().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Author not found"));
            book.setAuthor(author);
        }

        if (bookDetails.getPublisher() != null && bookDetails.getPublisher().getId() != null) {
            Publisher publisher = publisherRepository.findById(bookDetails.getPublisher().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Publisher not found"));
            book.setPublisher(publisher);
        }

        if (bookDetails.getLibrary() != null && bookDetails.getLibrary().getId() != null) {
            Library library = libraryRepository.findById(bookDetails.getLibrary().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Library not found"));
            book.setLibrary(library);
        }

        return bookRepository.save(book);
    }

    @Override
    public void deleteBook(Long id) {
        Book book = getBookById(id);
        bookRepository.delete(book);
    }

    @Override
    public BookResponseDTO convertToDto(Book book) {
        return new BookResponseDTO(
            book.getId(),
            book.getTitle(),
            book.getAuthor() != null ? book.getAuthor().getName() : null,
            book.getIsbn(),
            book.getAvailableCopies() != null && book.getAvailableCopies() > 0,
            book.getCreatedAt() // ✅ This now works because it's a LocalDateTime
        );
    }

    @Override
    public List<Book> createMultipleBooks(List<Book> books) {
        return bookRepository.saveAll(books);
    }
	
}