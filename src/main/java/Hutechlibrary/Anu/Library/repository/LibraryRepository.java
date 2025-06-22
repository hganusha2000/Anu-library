package Hutechlibrary.Anu.Library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import Hutechlibrary.Anu.Library.entity.Library;

@Repository
public interface LibraryRepository extends JpaRepository<Library, Long> {

}