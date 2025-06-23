package Hutechlibrary.Anu.Library.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import Hutechlibrary.Anu.Library.dto.ApiResponse;
import Hutechlibrary.Anu.Library.dto.ApiResponseLibrary;
import Hutechlibrary.Anu.Library.dto.LibraryDetails;
import Hutechlibrary.Anu.Library.dto.LibraryRequestDTO;
import Hutechlibrary.Anu.Library.dto.LibraryResponseDTO;
import Hutechlibrary.Anu.Library.entity.Library;

public interface LibraryService {

	Library createLibrary(LibraryRequestDTO req);
    Page<Library> listLibraries(Pageable pageable);
    Library getLibrary(Long id);
    Library updateLibrary(Long id, LibraryRequestDTO req);
    void deleteLibrary(Long id);

    LibraryResponseDTO toResponseDTO(Library library);
    
}