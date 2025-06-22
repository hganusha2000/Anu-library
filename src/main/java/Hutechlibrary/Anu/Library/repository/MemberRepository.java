package Hutechlibrary.Anu.Library.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import Hutechlibrary.Anu.Library.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	boolean existsByEmail(String email);

	boolean existsByPhone(String phone);
	
	 Page<Member> findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCaseAndEmailContainingIgnoreCaseAndPhoneContainingIgnoreCase(
		        String firstName, String lastName, String email, String phone, Pageable pageable);
		
}