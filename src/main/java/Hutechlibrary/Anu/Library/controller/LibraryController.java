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
import Hutechlibrary.Anu.Library.dto.ApiResponseLibrary;
import Hutechlibrary.Anu.Library.dto.BookDetails;
import Hutechlibrary.Anu.Library.dto.BorrowDetails;
import Hutechlibrary.Anu.Library.dto.LibraryDetails;
import Hutechlibrary.Anu.Library.dto.LibraryRequestDTO;
import Hutechlibrary.Anu.Library.dto.LibraryResponseDTO;
import Hutechlibrary.Anu.Library.dto.UserDetails;
import Hutechlibrary.Anu.Library.entity.Library;
import Hutechlibrary.Anu.Library.service.LibraryService;

@RestController
@RequestMapping("/api/libraries")
public class LibraryController {

    @Autowired
    private LibraryService libraryService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LibraryDetails> createLibrary(@RequestBody LibraryRequestDTO req) {
        Library lib = libraryService.createLibrary(req);
        LibraryResponseDTO dto = libraryService.toResponseDTO(lib);
        LibraryDetails resp = new LibraryDetails(
            HttpStatus.CREATED.value(),
            "Library created successfully",
            dto
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @GetMapping
    public ResponseEntity<ApiResponseLibrary> listLibraries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Library> libs = libraryService.listLibraries(PageRequest.of(page, size));
        List<LibraryDetails> details = libs.getContent().stream()
            .map(lib -> new LibraryDetails(
                HttpStatus.OK.value(),
                "Library fetched",
                libraryService.toResponseDTO(lib)
            )).collect(Collectors.toList());

        ApiResponseLibrary resp = new ApiResponseLibrary(details, libs.getTotalPages(), libs.getTotalElements());
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<LibraryDetails> getLibrary(@PathVariable Long id) {
        Library lib = libraryService.getLibrary(id);
        LibraryDetails resp = new LibraryDetails(
            HttpStatus.OK.value(),
            "Library fetched successfully",
            libraryService.toResponseDTO(lib)
        );
        return ResponseEntity.ok(resp);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LibraryDetails> updateLibrary(@PathVariable Long id,
                                                        @RequestBody LibraryRequestDTO req) {
        Library lib = libraryService.updateLibrary(id, req);
        LibraryDetails resp = new LibraryDetails(
            HttpStatus.OK.value(),
            "Library updated successfully",
            libraryService.toResponseDTO(lib)
        );
        return ResponseEntity.ok(resp);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LibraryDetails> deleteLibrary(@PathVariable Long id) {
        libraryService.deleteLibrary(id);
        LibraryDetails resp = new LibraryDetails(
            HttpStatus.OK.value(),
            "Library deleted successfully",
            null
        );
        return ResponseEntity.ok(resp);
    }
}