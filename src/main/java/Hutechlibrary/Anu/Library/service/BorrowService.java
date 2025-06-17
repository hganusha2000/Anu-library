package Hutechlibrary.Anu.Library.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import Hutechlibrary.Anu.Library.entity.Borrow;

public interface BorrowService {

	Borrow createBorrow(Borrow borrow);

	Borrow getBorrowById(Long id);

	Borrow updateBorrow(Long id, Borrow updatedBorrow);

	void deleteBorrow(Long id);
	
	Page<Borrow> getAllBorrowRecords(Pageable pageable);


}
