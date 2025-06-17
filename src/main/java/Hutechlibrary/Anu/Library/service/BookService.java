package Hutechlibrary.Anu.Library.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Hutechlibrary.Anu.Library.entity.Book;
import Hutechlibrary.Anu.Library.exception.ResourceNotFoundException;
import Hutechlibrary.Anu.Library.repository.BookRepository;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }

    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    public Book updateBook(Long id, Book bookDetails) {
        Book book = getBookById(id);
        book.setTitle(bookDetails.getTitle());
        book.setIsbn(bookDetails.getIsbn());
        book.setAuthor(bookDetails.getAuthor());
        book.setPublisher(bookDetails.getPublisher());
        book.setPublicationYear(bookDetails.getPublicationYear());
        book.setAvailable(bookDetails.isAvailable());
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        Book book = getBookById(id);
        bookRepository.delete(book);
    }
}
