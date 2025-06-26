package Hutechlibrary.Anu.Library.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import Hutechlibrary.Anu.Library.dto.ApiResponseBorrow;
import Hutechlibrary.Anu.Library.dto.BorrowDetails;
import Hutechlibrary.Anu.Library.dto.BorrowResponseDTO;
import Hutechlibrary.Anu.Library.dto.DataResponse;
import Hutechlibrary.Anu.Library.entity.Borrow;
import Hutechlibrary.Anu.Library.entity.User;
import Hutechlibrary.Anu.Library.repository.BorrowRepository;
import Hutechlibrary.Anu.Library.repository.UserRepository;
import Hutechlibrary.Anu.Library.service.BorrowService;

@RestController
@RequestMapping("/api")
public class BorrowController {

    @Autowired
    private BorrowService borrowService;
    
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/librarian/borrows")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<BorrowResponseDTO> createBorrow(@RequestBody Borrow borrow) {
        // Get the authenticated user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // Set the user from DB using the username
        
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        borrow.setUser(user); // inject actual user from token

        Borrow saved = borrowService.createBorrow(borrow);
        BorrowResponseDTO dto = convertToDTO(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    // ✅ Get All
    @GetMapping("/librarian/borrows")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponseBorrow> getAllBorrows(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        Page<Borrow> borrowPage = borrowService.getAllBorrowRecords(PageRequest.of(page, size));

        List<BorrowResponseDTO> dtoList = borrowPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        ApiResponseBorrow response = new ApiResponseBorrow(HttpStatus.OK.value(), "fetched successfully",
                dtoList, borrowPage.getTotalPages(), borrowPage.getTotalElements());

        return ResponseEntity.ok(response);
    }

    // ✅ Get by ID
    @GetMapping("/librarian/borrows/{id}")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<BorrowResponseDTO> getBorrowById(@PathVariable("id") Long id) {
        Borrow borrow = borrowService.getBorrowById(id);
        return ResponseEntity.ok(convertToDTO(borrow));
    }

    // ✅ Search
    @GetMapping("/librarian/borrows/search")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponseBorrow> searchBorrows(@RequestParam(required = false) Long memberId,
                                                            @RequestParam(required = false) Long bookId,
                                                            @RequestParam(required = false) Boolean returned,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {

        Page<Borrow> borrowPage = borrowService.searchBorrows(memberId, bookId, returned, PageRequest.of(page, size));

        List<BorrowResponseDTO> dtoList = borrowPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        ApiResponseBorrow response = new ApiResponseBorrow(HttpStatus.OK.value(), "fetched successfully",
                dtoList, borrowPage.getTotalPages(), borrowPage.getTotalElements());

        return ResponseEntity.ok(response);
    }

    // ✅ Update
    @PutMapping("/librarian/borrows/{id}")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<BorrowResponseDTO> updateBorrow(@PathVariable Long id, @RequestBody Borrow updatedBorrow) {
        Borrow updated = borrowService.updateBorrow(id, updatedBorrow);
        return ResponseEntity.ok(convertToDTO(updated));
    }

    // ✅ Delete
    @DeleteMapping("/admin/borrows/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteBorrow(@PathVariable("id") Long id) {
        borrowService.deleteBorrow(id);
        return ResponseEntity.ok("Borrow record deleted successfully.");
    }

    private BorrowResponseDTO convertToDTO(Borrow borrow) {
        BorrowResponseDTO dto = new BorrowResponseDTO();

        dto.setId(borrow.getId());
        dto.setBookTitle(
            borrow.getBook() != null ? borrow.getBook().getTitle() : "Unknown Book"
        );
        dto.setBorrowDate(borrow.getBorrowDate());
        dto.setReturnDate(borrow.getReturnDate());
        dto.setReturned(borrow.isReturned());

        if (borrow.getUser() != null) {
            dto.setUserId(borrow.getUser().getId());
            dto.setUsername(borrow.getUser().getUsername());
        } else {
            dto.setUserId(null);
            dto.setUsername("Unknown User");
        }

        if (borrow.getLibrary() != null) {
            dto.setLibraryId(borrow.getLibrary().getId());
        } else {
            dto.setLibraryId(null);
        }

        return dto;
    }

}