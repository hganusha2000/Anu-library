package Hutechlibrary.Anu.Library.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import Hutechlibrary.Anu.Library.entity.Borrow;


@Repository
public interface BorrowRepository extends JpaRepository<Borrow, Long> {

    @Query("SELECT b FROM Borrow b WHERE " +
           "(:userId IS NULL OR b.user.id = :userId) AND " +
           "(:bookId IS NULL OR b.book.id = :bookId) AND " +
           "(:returned IS NULL OR b.returned = :returned)")
    Page<Borrow> findByFilters(@Param("userId") Long userId,
                               @Param("bookId") Long bookId,
                               @Param("returned") Boolean returned,
                               Pageable pageable);
}