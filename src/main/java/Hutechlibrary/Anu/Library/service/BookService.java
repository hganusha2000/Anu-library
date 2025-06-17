package Hutechlibrary.Anu.Library.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import Hutechlibrary.Anu.Library.dto.DataResponse;
import Hutechlibrary.Anu.Library.entity.Author;
import Hutechlibrary.Anu.Library.entity.Book;
import jakarta.validation.Valid;

public interface BookService {

	DataResponse createBook(Book book);

	Book getBookById(Long id);

	Book updateBook(Long id, Book bookDetails);

	void deleteBook(Long id);

	Page<Book> getAllBooksPaginated(Pageable pageable);


}
