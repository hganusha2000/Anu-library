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
import Hutechlibrary.Anu.Library.dto.BookDetails;
import Hutechlibrary.Anu.Library.dto.DataResponse;
import Hutechlibrary.Anu.Library.dto.PageBookDetails;
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
    public ResponseEntity<ApiResponse> createBook(@Valid @RequestBody Book book) {
        DataResponse createdBookResponse = bookService.createBook(book);
        ApiResponse response = new ApiResponse(createdBookResponse);

        if (createdBookResponse.getStatus() == HttpStatus.CONFLICT.value()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/user/books")
    public ResponseEntity<ApiResponse> getAllBooksPaginated(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        Page<Book> bookPage = bookService.getAllBooksPaginated(PageRequest.of(page, size));

        List<BookDetails> bookList = bookPage.getContent().stream()
                .map(book -> {
                    BookDetails dto = new BookDetails();
                    dto.setId(book.getId());
                    dto.setTitle(book.getTitle());
                    dto.setIsbn(book.getIsbn());
                    dto.setAvailable(book.getAvailableCopies() != null && book.getAvailableCopies() > 0); // ✅ Inline check
                    dto.setAuthor(book.getAuthor() != null ? book.getAuthor().getName() : ""); // Avoid NullPointerException
                    return dto;
                })
                .toList();

        PageBookDetails details = new PageBookDetails();
        details.setBooks(bookList);
        details.setTotalPages(bookPage.getTotalPages());
        details.setTotalElements(bookPage.getTotalElements());

        DataResponse dataResponse = new DataResponse(
                HttpStatus.OK.value(),
                "Books fetched successfully",
                details
        );

        return ResponseEntity.ok(new ApiResponse(dataResponse));
    }



    @GetMapping("/user/books/{id}")
    public ResponseEntity<ApiResponse> getBookById(@PathVariable("id") Long id) {
        Book book = bookService.getBookById(id);

        DataResponse dataResponse = new DataResponse();
        dataResponse.setStatus(HttpStatus.OK.value());
        dataResponse.setMessage("Book fetched successfully");
        dataResponse.setData(book);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(dataResponse);

        return ResponseEntity.ok(apiResponse);
    }
    
    @GetMapping("/user/books/search")
    public ResponseEntity<ApiResponse> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) String authorName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Book> bookPage = bookService.searchBooks(title, isbn, authorName, PageRequest.of(page, size));

        List<BookDetails> bookList = bookPage.getContent().stream()
                .map(book -> {
                    BookDetails dto = new BookDetails();
                    dto.setId(book.getId());
                    dto.setTitle(book.getTitle());
                    dto.setIsbn(book.getIsbn());
                    dto.setAvailable(book.getAvailableCopies() != null && book.getAvailableCopies() > 0);
                    dto.setAuthor(book.getAuthor() != null ? book.getAuthor().getName() : "");
                    return dto;
                }).toList();

        PageBookDetails details = new PageBookDetails();
        details.setBooks(bookList);
        details.setTotalPages(bookPage.getTotalPages());
        details.setTotalElements(bookPage.getTotalElements());

        DataResponse dataResponse = new DataResponse(HttpStatus.OK.value(), "Books filtered successfully", details);
        return ResponseEntity.ok(new ApiResponse(dataResponse));
    }



  
    @PutMapping("/librarian/books/{id}")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateBook(@PathVariable("id") Long id, @RequestBody Book bookDetails) {
        Book updatedBook = bookService.updateBook(id, bookDetails);

        DataResponse dataResponse = new DataResponse();
        dataResponse.setStatus(HttpStatus.OK.value());
        dataResponse.setMessage("Book updated successfully");
        dataResponse.setData(updatedBook);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(dataResponse);

        return ResponseEntity.ok(apiResponse);
    }


    @DeleteMapping("/admin/books/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteBook(@PathVariable("id") Long id) {
        bookService.deleteBook(id);

        DataResponse dataResponse = new DataResponse();
        dataResponse.setStatus(HttpStatus.OK.value());
        dataResponse.setMessage("Book deleted successfully");
        dataResponse.setData(null); // No additional data

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(dataResponse);

        return ResponseEntity.ok(apiResponse);
    }

}
