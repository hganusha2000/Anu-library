package Hutechlibrary.Anu.Library.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Hutechlibrary.Anu.Library.entity.Book;
import Hutechlibrary.Anu.Library.entity.Borrow;
import Hutechlibrary.Anu.Library.entity.Member;
import Hutechlibrary.Anu.Library.exception.ResourceNotFoundException;
import Hutechlibrary.Anu.Library.repository.BookRepository;
import Hutechlibrary.Anu.Library.repository.BorrowRepository;
import Hutechlibrary.Anu.Library.repository.MemberRepository;

@Service
public class BorrowService {

    @Autowired
    private BorrowRepository borrowRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MemberRepository memberRepository;

    public List<Borrow> getAllBorrows() {
        return borrowRepository.findAll();
    }

    public Borrow getBorrowById(Long id) {
        return borrowRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Borrow not found with id: " + id));
    }

    // ✅ Return all borrow records
    public List<Borrow> getAllBorrowRecords() {
        return borrowRepository.findAll();
    }

    // ✅ Save borrow record using full Borrow object from POST body
    public Borrow createBorrow(Borrow borrow) {
        Long bookId = borrow.getBook().getId();
        Long memberId = borrow.getMember().getId();

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + memberId));

        if (!book.isAvailable()) {
            throw new IllegalStateException("Book is not available");
        }

        borrow.setBook(book);
        borrow.setMember(member);
        borrow.setBorrowDate(LocalDate.now());
        borrow.setReturned(false);

        book.setAvailable(false);
        bookRepository.save(book);

        return borrowRepository.save(borrow);
    }

    // ✅ Update borrow record
    public Borrow updateBorrow(Long id, Borrow updatedBorrow) {
        Borrow existing = borrowRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Borrow not found with id: " + id));

        Book book = bookRepository.findById(updatedBorrow.getBook().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + updatedBorrow.getBook().getId()));
        Member member = memberRepository.findById(updatedBorrow.getMember().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + updatedBorrow.getMember().getId()));

        existing.setBook(book);
        existing.setMember(member);
        existing.setBorrowDate(updatedBorrow.getBorrowDate());
        existing.setReturnDate(updatedBorrow.getReturnDate());
        existing.setReturned(updatedBorrow.isReturned());

        return borrowRepository.save(existing);
    }

    // ✅ Delete borrow record
    public void deleteBorrow(Long id) {
        Borrow borrow = borrowRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Borrow not found with id: " + id));

        // Make book available again on deletion
        Book book = borrow.getBook();
        if (book != null) {
            book.setAvailable(true);
            bookRepository.save(book);
        }

        borrowRepository.deleteById(id);
    }

    // Optional: Return book by updating the `returned` flag
    public Borrow returnBook(Long borrowId) {
        Borrow borrow = getBorrowById(borrowId);
        if (borrow.isReturned()) {
            throw new IllegalStateException("Book already returned");
        }
        borrow.setReturned(true);
        borrow.setReturnDate(LocalDate.now());

        Book book = borrow.getBook();
        book.setAvailable(true);
        bookRepository.save(book);

        return borrowRepository.save(borrow);
    }
}
