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
import Hutechlibrary.Anu.Library.dto.AuthorDetails;
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
    public ResponseEntity<ApiResponse> createAuthor(@Valid @RequestBody Author author) {
        DataResponse createdAuthorResponse = authorService.createAuthor(author);
        ApiResponse response = new ApiResponse(createdAuthorResponse);

        if (createdAuthorResponse.getStatus() == HttpStatus.CONFLICT.value()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    

    @GetMapping("/user/authors")
    public ResponseEntity<ApiResponse> getAllAuthorsPaginated(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        Page<Author> authorPage = authorService.getAllAuthorsPaginated(PageRequest.of(page, size));

        List<DataResponse> authorDataList = authorPage.getContent().stream()
                .map(author -> new DataResponse(
                        HttpStatus.OK.value(),
                        "Author data",
                        author
                ))
                .toList();

        AuthorDetails details = new AuthorDetails();
        details.setData(authorDataList);
        details.setTotalPages(authorPage.getTotalPages());
        details.setTotalElements(authorPage.getTotalElements());

        DataResponse dataResponse = new DataResponse(
                HttpStatus.OK.value(),
                "Authors fetched successfully",
                details
        );

        ApiResponse response = new ApiResponse(dataResponse);

        return ResponseEntity.ok(response);
    }
    

    @GetMapping("/user/authors/{id}")
    public ResponseEntity<ApiResponse> getAuthorById(@PathVariable("id") Long id) {
        Author author = authorService.getAuthorById(id);
        
        DataResponse dataResponse = new DataResponse();
        dataResponse.setStatus(HttpStatus.OK.value());
        dataResponse.setMessage("Author fetched successfully");
        dataResponse.setData(author);
        
        ApiResponse response = new ApiResponse();
        response.setData(dataResponse);

        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/user/authors/search")
    public ResponseEntity<ApiResponse> searchAuthors(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String biography,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Author> authorPage = authorService.searchAuthors(name, biography, PageRequest.of(page, size));

        List<DataResponse> authorDataList = authorPage.getContent().stream()
                .map(author -> new DataResponse(
                        HttpStatus.OK.value(),
                        "Filtered author data",
                        author
                ))
                .toList();

        AuthorDetails details = new AuthorDetails();
        details.setData(authorDataList);
        details.setTotalPages(authorPage.getTotalPages());
        details.setTotalElements(authorPage.getTotalElements());

        DataResponse dataResponse = new DataResponse(
                HttpStatus.OK.value(),
                "Filtered authors fetched successfully",
                details
        );

        ApiResponse response = new ApiResponse(dataResponse);
        return ResponseEntity.ok(response);
    }



    @PutMapping("/librarian/authors/{id}")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateAuthor(@PathVariable("id") Long id, @RequestBody Author authorDetails) {
        Author updatedAuthor = authorService.updateAuthor(id, authorDetails);

        DataResponse dataResponse = new DataResponse();
        dataResponse.setStatus(HttpStatus.OK.value());
        dataResponse.setMessage("Author updated successfully");
        dataResponse.setData(updatedAuthor);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(dataResponse);

        return ResponseEntity.ok(apiResponse);
    }


    @DeleteMapping("/admin/authors/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteAuthor(@PathVariable("id") Long id) {
        authorService.deleteAuthor(id);

        DataResponse dataResponse = new DataResponse();
        dataResponse.setStatus(HttpStatus.OK.value());
        dataResponse.setMessage("Author deleted successfully");
        dataResponse.setData(null); // No body needed

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(dataResponse);

        return ResponseEntity.ok(apiResponse);
    }

}
