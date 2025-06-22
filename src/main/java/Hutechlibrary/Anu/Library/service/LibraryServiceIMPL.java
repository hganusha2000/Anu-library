package Hutechlibrary.Anu.Library.service;

import Hutechlibrary.Anu.Library.dto.*;
import Hutechlibrary.Anu.Library.entity.Book;
import Hutechlibrary.Anu.Library.entity.Borrow;
import Hutechlibrary.Anu.Library.entity.Library;
import Hutechlibrary.Anu.Library.entity.User;
import Hutechlibrary.Anu.Library.repository.BookRepository;
import Hutechlibrary.Anu.Library.repository.BorrowRepository;
import Hutechlibrary.Anu.Library.repository.LibraryRepository;
import Hutechlibrary.Anu.Library.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LibraryServiceIMPL implements LibraryService {

    @Autowired
    private LibraryRepository libraryRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BorrowRepository borrowRepository;

    @Override
    public ApiResponse createLibrary(LibraryDetails libraryDetails) {
        Library library = new Library();
        library.setName(libraryDetails.getName());
        library.setAddress(libraryDetails.getAddress());

        library = libraryRepository.save(library);

        return successResponse("Library created successfully", mapToDetails(library));
    }

    @Override
    public ApiResponse getLibraryById(Long id) {
        Library library = libraryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Library not found"));
        return successResponse("Library fetched successfully", mapToDetails(library));
    }

    @Override
    public ApiResponse getAllLibraries() {
        List<LibraryDetails> libraries = libraryRepository.findAll()
                .stream()
                .map(this::mapToDetails)
                .collect(Collectors.toList());
        return successResponse("All libraries fetched successfully", libraries);
    }

    @Override
    public ApiResponse updateLibrary(Long id, LibraryDetails libraryDetails) {
        Library library = libraryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Library not found"));

        library.setName(libraryDetails.getName());
        library.setAddress(libraryDetails.getAddress());
        library = libraryRepository.save(library);

        return successResponse("Library updated successfully", mapToDetails(library));
    }

    @Override
    public ApiResponse deleteLibrary(Long id) {
        Library library = libraryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Library not found"));
        libraryRepository.delete(library);
        return successResponse("Library deleted successfully", null);
    }

    @Override
    public ApiResponse getBooksInLibrary(Long libraryId) {
        Library library = libraryRepository.findById(libraryId)
                .orElseThrow(() -> new EntityNotFoundException("Library not found"));

        List<BookDetails> books = library.getBooks().stream().map(book -> {
            BookDetails dto = new BookDetails();
            dto.setId(book.getId());
            dto.setTitle(book.getTitle());
            // ✅ Extract author name safely
            dto.setAuthor(book.getAuthor() != null ? book.getAuthor().getName() : null);
            dto.setIsbn(book.getIsbn());
            dto.setAvailable(book.getAvailableCopies() != null && book.getAvailableCopies() > 0); // ✅ Inline check
            return dto;
        }).collect(Collectors.toList());

        return successResponse("Books in library fetched successfully", books);
    }


    @Override
    public ApiResponse getUsersInLibrary(Long libraryId) {
        Library library = libraryRepository.findById(libraryId)
                .orElseThrow(() -> new EntityNotFoundException("Library not found"));

        List<UserDetails> users = library.getUsers().stream().map(user -> {
            UserDetails dto = new UserDetails();
            dto.setId(user.getId());
            dto.setUsername(user.getUsername());
            dto.setEmail(user.getEmail());
            dto.setActivated(user.isActivated());   // ✅ include activation status
            dto.setCreatedAt(user.getCreatedAt());  // ✅ include account creation time
            return dto;
        }).collect(Collectors.toList());

        return successResponse("Users in library fetched successfully", users);
    }

    @Override
    public ApiResponse getBorrowsInLibrary(Long libraryId) {
        Library library = libraryRepository.findById(libraryId)
                .orElseThrow(() -> new EntityNotFoundException("Library not found"));

        List<BorrowDetails> borrows = library.getBorrows().stream().map(borrow -> {
            BorrowDetails dto = new BorrowDetails();
            dto.setId(borrow.getId());
            dto.setBorrowDate(borrow.getBorrowDate());
            dto.setReturnDate(borrow.getReturnDate().toLocalDate());
            dto.setMemberId(borrow.getMember().getId());
            dto.setBookId(borrow.getBook().getId());
            return dto;
        }).collect(Collectors.toList());

        return successResponse("Borrow records fetched successfully", borrows);
    }

    // Maps entity to LibraryDetails DTO
    private LibraryDetails mapToDetails(Library library) {
        LibraryDetails dto = new LibraryDetails();
        dto.setId(library.getId());
        dto.setName(library.getName());
        dto.setAddress(library.getAddress());

        dto.setBookIds(library.getBooks() != null ?
                library.getBooks().stream().map(Book::getId).collect(Collectors.toList()) : List.of());

        dto.setUserIds(library.getUsers() != null ?
                library.getUsers().stream().map(User::getId).collect(Collectors.toList()) : List.of());

        dto.setBorrowIds(library.getBorrows() != null ?
                library.getBorrows().stream().map(Borrow::getId).collect(Collectors.toList()) : List.of());

        return dto;
    }

    // Wraps data in an ApiResponse
    private ApiResponse successResponse(String message, Object data) {
        DataResponse response = new DataResponse();
        response.setStatus(200);
        response.setMessage(message);
        response.setData(data);
        return new ApiResponse(response);
    }
}
