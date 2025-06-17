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
import Hutechlibrary.Anu.Library.dto.BorrowDetails;
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
    
    @PostMapping("/librarian/borrows")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createBorrow(@RequestBody Borrow borrow) {
        Borrow saved = borrowService.createBorrow(borrow);
        DataResponse dataResponse = new DataResponse(HttpStatus.CREATED.value(), "Borrow record created successfully", saved);
        ApiResponse response = new ApiResponse(dataResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/librarian/borrows")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getAllBorrowRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Borrow> borrowPage = borrowService.getAllBorrowRecords(PageRequest.of(page, size));

        List<DataResponse> borrowDataList = borrowPage.getContent().stream()
                .map(borrow -> new DataResponse(HttpStatus.OK.value(), "Borrow data", borrow))
                .collect(Collectors.toList());

        BorrowDetails details = new BorrowDetails();
        details.setData(borrowDataList);
        details.setTotalPages(borrowPage.getTotalPages());
        details.setTotalElements(borrowPage.getTotalElements());

        DataResponse wrapper = new DataResponse(HttpStatus.OK.value(), "Borrow records fetched successfully", details);
        ApiResponse response = new ApiResponse(wrapper);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/librarian/borrows/{id}")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getBorrowById(@PathVariable("id") Long id) {
        Borrow borrow = borrowService.getBorrowById(id);
        DataResponse dataResponse = new DataResponse(HttpStatus.OK.value(), "Borrow record fetched", borrow);
        ApiResponse response = new ApiResponse(dataResponse);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/librarian/borrows/{id}")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateBorrow(@PathVariable Long id,
                                                    @RequestBody Borrow updatedBorrow) {
        Borrow updated = borrowService.updateBorrow(id, updatedBorrow);
        DataResponse dataResponse = new DataResponse(HttpStatus.OK.value(), "Borrow record updated successfully", updated);
        ApiResponse response = new ApiResponse(dataResponse);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/admin/borrows/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteBorrow(@PathVariable("id") Long id) {
        borrowService.deleteBorrow(id);
        DataResponse dataResponse = new DataResponse(HttpStatus.OK.value(), "Borrow record deleted successfully", null);
        ApiResponse response = new ApiResponse(dataResponse);
        return ResponseEntity.ok(response);
    }
}