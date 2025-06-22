package Hutechlibrary.Anu.Library.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Hutechlibrary.Anu.Library.dto.ApiResponse;
import Hutechlibrary.Anu.Library.dto.BookDetails;
import Hutechlibrary.Anu.Library.dto.BorrowDetails;
import Hutechlibrary.Anu.Library.dto.LibraryDetails;
import Hutechlibrary.Anu.Library.dto.UserDetails;
import Hutechlibrary.Anu.Library.service.LibraryService;

@RestController
@RequestMapping("/api/libraries")

public class LibraryController {

	    @Autowired
	    private LibraryService libraryService;

	    @PostMapping("/create")
	    @PreAuthorize("hasRole('ADMIN')")
	    public ResponseEntity<ApiResponse> createLibrary(@RequestBody LibraryDetails libraryDTO) {
	        return ResponseEntity.ok(libraryService.createLibrary(libraryDTO));
	    }

	    @GetMapping
	    public ResponseEntity<ApiResponse> getAllLibraries() {
	        return ResponseEntity.ok(libraryService.getAllLibraries());
	    }

	    @GetMapping("/{id}")
	    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	    public ResponseEntity<ApiResponse> getLibraryById(@PathVariable Long id) {
	        return ResponseEntity.ok(libraryService.getLibraryById(id));
	    }

	    @PutMapping("/{id}")
	    @PreAuthorize("hasRole('ADMIN')")
	    public ResponseEntity<ApiResponse> updateLibrary(@PathVariable Long id, @RequestBody LibraryDetails libraryDTO) {
	        return ResponseEntity.ok(libraryService.updateLibrary(id, libraryDTO));
	    }

	    @DeleteMapping("/{id}")
	    @PreAuthorize("hasRole('ADMIN')")
	    public ResponseEntity<ApiResponse> deleteLibrary(@PathVariable Long id) {
	        return ResponseEntity.ok(libraryService.deleteLibrary(id));
	    }

	    @GetMapping("/{libraryId}/books")
	    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	    public ResponseEntity<ApiResponse> getBooksInLibrary(@PathVariable Long libraryId) {
	        return ResponseEntity.ok(libraryService.getBooksInLibrary(libraryId));
	    }

	    @GetMapping("/{libraryId}/users")
	    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	    public ResponseEntity<ApiResponse> getUsersInLibrary(@PathVariable Long libraryId) {
	        return ResponseEntity.ok(libraryService.getUsersInLibrary(libraryId));
	    }

	    @GetMapping("/{libraryId}/borrows")
	    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	    public ResponseEntity<ApiResponse> getBorrowsInLibrary(@PathVariable Long libraryId) {
	        return ResponseEntity.ok(libraryService.getBorrowsInLibrary(libraryId));
	    }
}
	    