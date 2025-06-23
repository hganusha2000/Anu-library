package Hutechlibrary.Anu.Library.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Hutechlibrary.Anu.Library.dto.ApiResponse;
import Hutechlibrary.Anu.Library.dto.ApiResponseBook;
import Hutechlibrary.Anu.Library.dto.BookDetails;
import Hutechlibrary.Anu.Library.dto.BookResponseDTO;
import Hutechlibrary.Anu.Library.dto.DataResponse;
import Hutechlibrary.Anu.Library.entity.Book;
import Hutechlibrary.Anu.Library.service.BookService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class BookController {

	@Autowired
	private BookService bookService;

	@PostMapping("/librarian/books")
	@PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
	public ResponseEntity<BookDetails> createBook(@RequestBody Book book) {
		Book savedBook = bookService.createBook(book);

		BookDetails response = new BookDetails(HttpStatus.CREATED.value(), "Book created successfully",
				bookService.convertToDto(savedBook));
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping(value = "/user/books", produces = "application/json")
	public ResponseEntity<ApiResponseBook> getAllBooksPaginated(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Page<Book> bookPage = bookService.getAllBooksPaginated(PageRequest.of(page, size));
		List<BookResponseDTO> dtoList = bookPage.getContent().stream().map(bookService::convertToDto).toList();

		ApiResponseBook response = new ApiResponseBook(HttpStatus.OK.value(), "Book fetched successfully", dtoList,
				bookPage.getTotalPages(), bookPage.getTotalElements());
		return ResponseEntity.ok(response);
	}

	@GetMapping("/user/books/{id}")
	public ResponseEntity<BookDetails> getBookById(@PathVariable Long id) {
		Book book = bookService.getBookById(id);
		BookResponseDTO dto = bookService.convertToDto(book);
		BookDetails response = new BookDetails(HttpStatus.OK.value(), "Book fetched successfully", dto);
		return ResponseEntity.ok(response);
	}

	@GetMapping(value = "/user/books/search", produces = "application/json")
	public ResponseEntity<ApiResponseBook> searchBooks(@RequestParam(required = false) String title,
			@RequestParam(required = false) String isbn, @RequestParam(required = false) String authorName,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

		Page<Book> bookPage = bookService.searchBooks(title, isbn, authorName, PageRequest.of(page, size));
		List<BookResponseDTO> dtoList = bookPage.getContent().stream().map(bookService::convertToDto).toList();

		ApiResponseBook response = new ApiResponseBook(HttpStatus.OK.value(), "Books fetched successfully", dtoList,
				bookPage.getTotalPages(), bookPage.getTotalElements());
		return ResponseEntity.ok(response);
	}

	@PutMapping("/librarian/books/{id}")
	@PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
	public ResponseEntity<BookDetails> updateBook(@PathVariable Long id, @RequestBody Book bookDetails) {
		Book updatedBook = bookService.updateBook(id, bookDetails);
		BookDetails response = new BookDetails(HttpStatus.OK.value(), "Book updated successfully",
				bookService.convertToDto(updatedBook));
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/admin/books/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<BookDetails> deleteBook(@PathVariable Long id) {
		bookService.deleteBook(id);
		BookDetails response = new BookDetails(HttpStatus.OK.value(), "Book deleted successfully", null);
		return ResponseEntity.ok(response);
	}
}