package Hutechlibrary.Anu.Library.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RestController;

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

    // LIST all borrow records – LIBRARIAN or ADMIN
    @GetMapping("/librarian/borrows")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<Object> getAllBorrowRecords() {
        return ResponseEntity.ok(borrowService.getAllBorrowRecords());
    }

    // GET a single borrow record by ID – LIBRARIAN or ADMIN
    @GetMapping("/librarian/borrows/{id}")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<Borrow> getBorrowById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(borrowService.getBorrowById(id));
    }

    // CREATE a new borrow record – LIBRARIAN or ADMIN
    @PostMapping("/librarian/borrows")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<Borrow> createBorrow(@RequestBody Borrow borrow) {
        Borrow saved = borrowService.createBorrow(borrow);
        return ResponseEntity.ok(saved);
    }

    // UPDATE an existing borrow record – LIBRARIAN or ADMIN
    @PutMapping("/librarian/borrows/{id}")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<Borrow> updateBorrow(@PathVariable Long id,
                                               @RequestBody Borrow updatedBorrow) {
        Borrow updated = borrowService.updateBorrow(id, updatedBorrow);
        return ResponseEntity.ok(updated);
    }

    // DELETE a borrow record – ADMIN only
    @DeleteMapping("/admin/borrows/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteBorrow(@PathVariable("id") Long id) {
        borrowService.deleteBorrow(id);
        return ResponseEntity.ok("Borrow record deleted successfully");
    }
}