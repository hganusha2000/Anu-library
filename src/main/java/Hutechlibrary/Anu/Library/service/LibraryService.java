package Hutechlibrary.Anu.Library.service;

import Hutechlibrary.Anu.Library.dto.ApiResponse;
import Hutechlibrary.Anu.Library.dto.LibraryDetails;

public interface LibraryService {

	 ApiResponse createLibrary(LibraryDetails libraryDetails);

	    ApiResponse getLibraryById(Long id);

	    ApiResponse getAllLibraries();

	    ApiResponse updateLibrary(Long id, LibraryDetails libraryDetails);

	    ApiResponse deleteLibrary(Long id);

	    ApiResponse getBooksInLibrary(Long libraryId);

	    ApiResponse getUsersInLibrary(Long libraryId);

	    ApiResponse getBorrowsInLibrary(Long libraryId);
    
}