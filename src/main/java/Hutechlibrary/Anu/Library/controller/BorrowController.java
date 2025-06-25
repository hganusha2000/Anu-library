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
import Hutechlibrary.Anu.Library.entity.Member;
import Hutechlibrary.Anu.Library.repository.BorrowRepository;
import Hutechlibrary.Anu.Library.service.BorrowService;
import Hutechlibrary.Anu.Library.service.MemberService;

@RestController
@RequestMapping("/api")
public class BorrowController {

    @Autowired
    private BorrowService borrowService;

    // ✅ Create
    @PostMapping("/librarian/borrows")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<BorrowResponseDTO> createBorrow(@RequestBody Borrow borrow) {
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

    // ✅ Helper Method
    private BorrowResponseDTO convertToDTO(Borrow borrow) {
        return new BorrowResponseDTO(
                borrow.getId(),
                borrow.getBook().getId(),
                borrow.getBook().getTitle(),
                borrow.getMember().getId(),
                borrow.getBorrowDate(),
                borrow.getReturnDate(),
                borrow.isReturned()
        );
    }
}