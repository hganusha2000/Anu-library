package Hutechlibrary.Anu.Library.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Hutechlibrary.Anu.Library.entity.Borrow;

public interface BorrowRepository extends JpaRepository<Borrow, Long> {
}