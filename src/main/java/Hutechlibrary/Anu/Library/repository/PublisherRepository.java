package Hutechlibrary.Anu.Library.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import Hutechlibrary.Anu.Library.entity.Publisher;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {

	boolean existsByName(String name);
	
	 Page<Publisher> findByNameContainingIgnoreCase(String name, Pageable pageable);
	    
	    Page<Publisher> findByAddressContainingIgnoreCase(String address, Pageable pageable);

	    // Optional: if you want to search by both
	    Page<Publisher> findByNameContainingIgnoreCaseOrAddressContainingIgnoreCase(String name, String address, Pageable pageable);
	}
