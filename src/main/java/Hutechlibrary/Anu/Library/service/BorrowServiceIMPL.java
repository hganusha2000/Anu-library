package Hutechlibrary.Anu.Library.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import Hutechlibrary.Anu.Library.dto.BorrowResponseDTO;
import Hutechlibrary.Anu.Library.entity.Book;
import Hutechlibrary.Anu.Library.entity.Borrow;
import Hutechlibrary.Anu.Library.entity.User;
import Hutechlibrary.Anu.Library.enums.UserStatus;
import Hutechlibrary.Anu.Library.exception.ResourceNotFoundException;
import Hutechlibrary.Anu.Library.repository.BookRepository;
import Hutechlibrary.Anu.Library.repository.BorrowRepository;
import Hutechlibrary.Anu.Library.repository.UserRepository;

@Service
public class BorrowServiceIMPL implements BorrowService {

    @Autowired
    private BorrowRepository borrowRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    // ✅ Create Borrow
    @Override
    public Borrow createBorrow(Borrow borrow) {
        Long bookId = borrow.getBook().getId();
        Long userId = borrow.getUser().getId();

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // ✅ Check user status
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new IllegalStateException("User is not active and cannot borrow books");
        }

        List<String> allowedRoles = List.of("ROLE_USER", "ROLE_LIBRARIAN", "ROLE_ADMIN");
        boolean isAllowed = user.getRoles().stream()
            .map(role -> role.getName().toUpperCase())
            .anyMatch(allowedRoles::contains);

        if (!isAllowed) {
            throw new IllegalStateException("Only USER, LIBRARIAN, or ADMIN roles are allowed to borrow books");
        }

        // ✅ Check available copies
        if (book.getAvailableCopies() == null || book.getAvailableCopies() <= 0) {
            throw new IllegalStateException("No available copies for this book");
        }

        // ✅ Set borrow details
        borrow.setBook(book);
        borrow.setUser(user);
        borrow.setBorrowDate(LocalDate.now());
        borrow.setReturned(false);

        // ✅ Decrease book copies
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        return borrowRepository.save(borrow);
    }
    

    // ✅ Get all records
    @Override
    public Page<Borrow> getAllBorrowRecords(Pageable pageable) {
        return borrowRepository.findAll(pageable);
    }

    // ✅ Get one
    @Override
    public Borrow getBorrowById(Long id) {
        return borrowRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Borrow not found with id: " + id));
    }

    // ✅ Search
    @Override
    public Page<Borrow> searchBorrows(Long userId, Long bookId, Boolean returned, Pageable pageable) {
        return borrowRepository.findByFilters(userId, bookId, returned, pageable);
    }

    // ✅ Update
    @Override
    public Borrow updateBorrow(Long id, Borrow updatedBorrow) {
        Borrow existing = borrowRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Borrow not found with id: " + id));

        Book book = bookRepository.findById(updatedBorrow.getBook().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + updatedBorrow.getBook().getId()));

        existing.setBook(book);
        existing.setBorrowDate(updatedBorrow.getBorrowDate());
        existing.setReturnDate(updatedBorrow.getReturnDate());
        existing.setReturned(updatedBorrow.isReturned());

        return borrowRepository.save(existing);
    }

    // ✅ Delete
    @Override
    public void deleteBorrow(Long id) {
        Borrow borrow = borrowRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Borrow not found with id: " + id));

        Book book = borrow.getBook();
        if (book != null) {
            book.setAvailableCopies(book.getAvailableCopies() + 1);
            bookRepository.save(book);
        }

        borrowRepository.deleteById(id);
    }

    // ✅ Return Book
    public Borrow returnBook(Long borrowId) {
        Borrow borrow = getBorrowById(borrowId);

        if (borrow.isReturned()) {
            throw new IllegalStateException("Book already returned");
        }

        borrow.setReturned(true);
        borrow.setReturnDate(LocalDate.now());

        Book book = borrow.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        return borrowRepository.save(borrow);
    }

//    // ✅ Alias to support multiple names
//    @Override
//    public Page<Borrow> findByFilters(Long userId, Long bookId, Boolean returned, Pageable pageable) {
//        return borrowRepository.findByFilters(userId, bookId, returned, pageable);
//    }

    // This method is redundant if findByFilters exists
    @Override
    public Page<Borrow> filterBorrows(Long userId, Long bookId, Boolean returned, Pageable pageable) {
        return borrowRepository.findByFilters(userId, bookId, returned, pageable);
    }
    
    public BorrowResponseDTO convertToDto(Borrow borrow) {
        return new BorrowResponseDTO(
            borrow.getId(),
            borrow.getBook() != null ? borrow.getBook().getId() : null,
            borrow.getBook() != null ? borrow.getBook().getTitle() : null,
            borrow.getUser() != null ? borrow.getUser().getId() : null,
            borrow.getUser() != null ? borrow.getUser().getUsername() : null,
            borrow.getRole(),  
            borrow.getBorrowDate(),
            borrow.getReturnDate(),
            borrow.isReturned(),
            borrow.getLibrary() != null ? borrow.getLibrary().getId() : null,
            borrow.getCreatedAt()
        );
    }



}