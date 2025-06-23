package Hutechlibrary.Anu.Library.controller;

import java.util.List;
import java.util.stream.Collectors;

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
import Hutechlibrary.Anu.Library.dto.AuthorDetails;
import Hutechlibrary.Anu.Library.dto.AuthorResponseDTO;
import Hutechlibrary.Anu.Library.dto.DataResponse;
import Hutechlibrary.Anu.Library.entity.Author;
import Hutechlibrary.Anu.Library.service.AuthorService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @PostMapping("/librarian/authors")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<AuthorDetails> createAuthor(@Valid @RequestBody Author author) {
        Author createdAuthor = authorService.createAuthorEntity(author);
        AuthorResponseDTO dto = authorService.convertToDto(createdAuthor);

        AuthorDetails response = new AuthorDetails(
                HttpStatus.CREATED.value(),
                "Author created successfully",
                dto
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/user/authors")
    public ResponseEntity<ApiResponse> getAllAuthorsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Author> authorPage = authorService.getAllAuthorsPaginated(PageRequest.of(page, size));

        List<AuthorDetails> authorDetailsList = authorPage.getContent().stream()
                .map(author -> new AuthorDetails(
                        HttpStatus.OK.value(),
                        "Author fetched successfully",
                        authorService.convertToDto(author)
                ))
                .collect(Collectors.toList());

        ApiResponse response = new ApiResponse(
                authorDetailsList,
                authorPage.getTotalPages(),
                authorPage.getTotalElements()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/authors/{id}")
    public ResponseEntity<AuthorDetails> getAuthorById(@PathVariable Long id) {
        Author author = authorService.getAuthorById(id);
        AuthorResponseDTO dto = authorService.convertToDto(author);

        AuthorDetails response = new AuthorDetails(
                HttpStatus.OK.value(),
                "Author fetched successfully",
                dto
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/authors/search")
    public ResponseEntity<ApiResponse> searchAuthors(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String biography,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Author> authorPage = authorService.searchAuthors(name, biography, PageRequest.of(page, size));

        List<AuthorDetails> authorDetailsList = authorPage.getContent().stream()
                .map(author -> new AuthorDetails(
                        HttpStatus.OK.value(),
                        "Filtered author",
                        authorService.convertToDto(author)
                ))
                .collect(Collectors.toList());

        ApiResponse response = new ApiResponse(
                authorDetailsList,
                authorPage.getTotalPages(),
                authorPage.getTotalElements()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/librarian/authors/{id}")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<AuthorDetails> updateAuthor(@PathVariable Long id, @RequestBody Author authorDetails) {
        Author updatedAuthor = authorService.updateAuthor(id, authorDetails);
        AuthorResponseDTO dto = authorService.convertToDto(updatedAuthor);

        AuthorDetails response = new AuthorDetails(
                HttpStatus.OK.value(),
                "Author updated successfully",
                dto
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/admin/authors/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthorDetails> deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        AuthorDetails response = new AuthorDetails(
                HttpStatus.OK.value(),
                "Author deleted successfully",
                null
        );
        return ResponseEntity.ok(response);
    }
}