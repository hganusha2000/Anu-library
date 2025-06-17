package Hutechlibrary.Anu.Library.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Hutechlibrary.Anu.Library.entity.Publisher;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {

	boolean existsByName(String name);
}