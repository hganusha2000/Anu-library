package Hutechlibrary.Anu.Library.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import Hutechlibrary.Anu.Library.dto.BookResponseDTO;
import Hutechlibrary.Anu.Library.dto.DataResponse;
import Hutechlibrary.Anu.Library.entity.Author;
import Hutechlibrary.Anu.Library.entity.Book;
import jakarta.validation.Valid;

public interface BookService {

	Book createBook(Book book);
    Page<Book> getAllBooksPaginated(Pageable pageable);
    Book getBookById(Long id);
    Page<Book> searchBooks(String title, String isbn, String authorName, Pageable pageable);
    Book updateBook(Long id, Book bookDetails);
    void deleteBook(Long id);
    BookResponseDTO convertToDto(Book book);
    List<Book> createMultipleBooks(List<Book> books);


}
